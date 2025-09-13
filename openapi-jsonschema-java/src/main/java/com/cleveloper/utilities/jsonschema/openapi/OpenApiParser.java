package com.cleveloper.utilities.jsonschema.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.ParseOptions;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import java.nio.file.Path;
import java.util.List;

/** Parses OpenAPI documents from raw String content, path, or URL. */
public class OpenApiParser {

  /**
   * Parse an OpenAPI specification provided as a String (YAML or JSON).
   *
   * @param content raw OpenAPI content
   * @return parsed OpenAPI model
   * @throws IllegalArgumentException if parsing fails or content is empty
   */
  public OpenAPI parse(String content) {
    if (content == null || content.trim().isEmpty()) {
      throw new IllegalArgumentException("OpenAPI content must not be empty");
    }
    ParseOptions options = new ParseOptions();
    options.setResolve(true);
    options.setResolveFully(true);
    options.setFlatten(false);

    SwaggerParseResult result = new OpenAPIV3Parser().readContents(content, null, options);
    List<String> messages = result.getMessages();
    if (result.getOpenAPI() == null) {
      String msg =
          (messages == null || messages.isEmpty())
              ? "Unable to parse OpenAPI"
              : String.join("; ", messages);
      throw new IllegalArgumentException(msg);
    }
    return result.getOpenAPI();
  }

  /** Parse from a filesystem path (resolves relative $ref using the path as base). */
  public OpenAPI parsePath(Path path) {
    if (path == null) throw new IllegalArgumentException("Path must not be null");
    ParseOptions options = new ParseOptions();
    options.setResolve(true);
    options.setResolveFully(true);
    options.setFlatten(false);
    SwaggerParseResult result =
        new OpenAPIV3Parser().readLocation(path.toAbsolutePath().toString(), null, options);
    List<String> messages = result.getMessages();
    if (result.getOpenAPI() == null) {
      String msg =
          (messages == null || messages.isEmpty())
              ? "Unable to parse OpenAPI"
              : String.join("; ", messages);
      throw new IllegalArgumentException(msg);
    }
    return result.getOpenAPI();
  }

  /** Parse from a URL or file URL string. */
  public OpenAPI parseUrl(String url) {
    if (url == null || url.isBlank()) throw new IllegalArgumentException("URL must not be empty");
    ParseOptions options = new ParseOptions();
    options.setResolve(true);
    options.setResolveFully(true);
    options.setFlatten(false);
    SwaggerParseResult result = new OpenAPIV3Parser().readLocation(url, null, options);
    List<String> messages = result.getMessages();
    if (result.getOpenAPI() == null) {
      String msg =
          (messages == null || messages.isEmpty())
              ? "Unable to parse OpenAPI"
              : String.join("; ", messages);
      throw new IllegalArgumentException(msg);
    }
    return result.getOpenAPI();
  }
}
