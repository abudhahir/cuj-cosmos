package com.cleveloper.utilities.jsonschema.openapi;

import com.cleveloper.utilities.jsonschema.openapi.convert.OpenApiToJsonSchemaConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import java.util.Map;

/** Default implementation that assembles a full JSON Schema document with $defs. */
public class DefaultOpenApiSchemaService implements OpenApiSchemaService {

  private static final String SCHEMA_DRAFT_2020_12 = "https://json-schema.org/draft/2020-12/schema";

  private final OpenApiSpecRegistry registry;
  private final OpenApiToJsonSchemaConverter converter;
  private final ObjectMapper mapper;
  private final String schemaUri;
  private final String idPrefix;

  public DefaultOpenApiSchemaService(
      OpenApiSpecRegistry registry,
      OpenApiToJsonSchemaConverter converter,
      ObjectMapper mapper,
      String schemaUri,
      String idPrefix) {
    this.registry = registry;
    this.converter = converter == null ? new OpenApiToJsonSchemaConverter(true) : converter;
    this.mapper = mapper == null ? new ObjectMapper() : mapper;
    this.schemaUri = (schemaUri == null || schemaUri.isBlank()) ? SCHEMA_DRAFT_2020_12 : schemaUri;
    this.idPrefix = (idPrefix == null || idPrefix.isBlank()) ? "urn:cosmos:schema:" : idPrefix;
  }

  public DefaultOpenApiSchemaService(OpenApiSpecRegistry registry) {
    this(registry, new OpenApiToJsonSchemaConverter(true), new ObjectMapper(), null, null);
  }

  @Override
  public String generateFromOpenApi(OpenAPI openApi, String componentName) {
    if (openApi == null) throw new IllegalArgumentException("openApi must not be null");
    if (componentName == null || componentName.isBlank())
      throw new IllegalArgumentException("componentName must not be empty");

    ObjectNode doc = mapper.createObjectNode();
    doc.put("$schema", schemaUri);
    doc.put("$id", idPrefix + componentName);

    // Root schema
    Schema<?> rootSchema = openApi.getComponents().getSchemas().get(componentName);
    if (rootSchema == null)
      throw new IllegalArgumentException("Component not found: " + componentName);
    ObjectNode rootNode = (ObjectNode) converter.convert(openApi, rootSchema);
    doc.setAll(rootNode);

    // $defs: include only referenced component schemas (closure)
    ObjectNode defs = doc.putObject("$defs");
    Map<String, Schema> components = openApi.getComponents().getSchemas();

    java.util.Set<String> toProcess = new java.util.LinkedHashSet<>();
    java.util.Set<String> processed = new java.util.HashSet<>();

    toProcess.addAll(collectDefRefs(rootNode));

    while (!toProcess.isEmpty()) {
      String name = toProcess.iterator().next();
      toProcess.remove(name);
      if (!processed.add(name)) continue;
      Schema<?> s = components.get(name);
      if (s == null) continue;
      ObjectNode defNode = (ObjectNode) converter.convert(openApi, s);
      defs.set(name, defNode);
      toProcess.addAll(collectDefRefs(defNode));
    }

    try {
      return mapper.writeValueAsString(doc);
    } catch (Exception e) {
      throw new IllegalStateException("Failed to serialize schema document", e);
    }
  }

  private java.util.Set<String> collectDefRefs(com.fasterxml.jackson.databind.JsonNode node) {
    java.util.Set<String> names = new java.util.LinkedHashSet<>();
    if (node == null) return names;
    if (node.isObject()) {
      node.fields()
          .forEachRemaining(
              e -> {
                if ("$ref".equals(e.getKey()) && e.getValue().isTextual()) {
                  String v = e.getValue().asText();
                  String prefix = "#/$defs/";
                  if (v.startsWith(prefix)) {
                    names.add(v.substring(prefix.length()));
                  }
                } else {
                  names.addAll(collectDefRefs(e.getValue()));
                }
              });
    } else if (node.isArray()) {
      node.forEach(child -> names.addAll(collectDefRefs(child)));
    }
    return names;
  }

  @Override
  public String generateFromRegistry(String specId, String componentName) {
    if (specId == null || specId.isBlank())
      throw new IllegalArgumentException("specId must not be empty");
    OpenAPI api = registry.get(specId);
    if (api == null) throw new IllegalArgumentException("Spec not found: " + specId);
    return generateFromOpenApi(api, componentName);
  }
}
