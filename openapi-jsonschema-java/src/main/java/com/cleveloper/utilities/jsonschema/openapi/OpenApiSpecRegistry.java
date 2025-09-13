package com.cleveloper.utilities.jsonschema.openapi;

import com.cleveloper.utilities.jsonschema.cache.SpecCache;
import com.cleveloper.utilities.jsonschema.config.OpenApiSpecsProperties;
import io.swagger.v3.oas.models.OpenAPI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/** Registry that loads and stores OpenAPI specs by id, backed by a SpecCache. */
public class OpenApiSpecRegistry {
  private final OpenApiParser parser;
  private final SpecCache cache;
  private final Map<String, OpenAPI> loaded = new ConcurrentHashMap<>();

  public OpenApiSpecRegistry(OpenApiParser parser, SpecCache cache) {
    this.parser = parser;
    this.cache = cache;
  }

  public void loadFromProperties(OpenApiSpecsProperties props, ResourceLoader resourceLoader) {
    if (props == null || props.getSpecs() == null) return;
    for (OpenApiSpecsProperties.Spec spec : props.getSpecs()) {
      if (spec.getId() == null || spec.getId().isBlank()) continue;
      String loc = spec.getLocation();
      if (loc == null || loc.isBlank()) continue;
      OpenAPI api = loadByLocation(loc, resourceLoader);
      if (api != null) {
        loaded.put(spec.getId(), api);
        cache.put(spec.getId(), api);
      }
    }
  }

  public OpenAPI get(String id) {
    OpenAPI api = loaded.get(id);
    if (api != null) return api;
    return cache.get(id);
  }

  private OpenAPI loadByLocation(String location, ResourceLoader resourceLoader) {
    try {
      if (location.startsWith("classpath:")) {
        Resource r = resourceLoader.getResource(location);
        if (r.exists()) {
          URL url = r.getURL();
          return parser.parseUrl(url.toString());
        }
      }
      // Try as URL
      if (location.startsWith("http://")
          || location.startsWith("https://")
          || location.startsWith("file:")) {
        return parser.parseUrl(location);
      }
      // Try as filesystem path
      Path p = Path.of(location);
      if (Files.exists(p)) {
        return parser.parsePath(p);
      }
      // Fallback: try reading as contents
      return parser.parse(location);
    } catch (Exception ex) {
      return null;
    }
  }
}
