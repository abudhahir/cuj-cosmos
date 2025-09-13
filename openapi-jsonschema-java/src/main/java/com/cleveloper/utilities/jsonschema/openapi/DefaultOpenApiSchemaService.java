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

  public DefaultOpenApiSchemaService(
      OpenApiSpecRegistry registry, OpenApiToJsonSchemaConverter converter, ObjectMapper mapper) {
    this.registry = registry;
    this.converter = converter;
    this.mapper = mapper == null ? new ObjectMapper() : mapper;
  }

  public DefaultOpenApiSchemaService(OpenApiSpecRegistry registry) {
    this(registry, new OpenApiToJsonSchemaConverter(), new ObjectMapper());
  }

  @Override
  public String generateFromOpenApi(OpenAPI openApi, String componentName) {
    if (openApi == null) throw new IllegalArgumentException("openApi must not be null");
    if (componentName == null || componentName.isBlank())
      throw new IllegalArgumentException("componentName must not be empty");

    ObjectNode doc = mapper.createObjectNode();
    doc.put("$schema", SCHEMA_DRAFT_2020_12);
    doc.put("$id", "urn:cosmos:schema:" + componentName);

    // Root schema
    Schema<?> rootSchema = openApi.getComponents().getSchemas().get(componentName);
    if (rootSchema == null)
      throw new IllegalArgumentException("Component not found: " + componentName);
    doc.setAll((ObjectNode) converter.convert(openApi, rootSchema));

    // $defs: include all components as named definitions (best-effort initial approach)
    ObjectNode defs = doc.putObject("$defs");
    Map<String, Schema> components = openApi.getComponents().getSchemas();
    for (Map.Entry<String, Schema> entry : components.entrySet()) {
      String name = entry.getKey();
      Schema<?> s = entry.getValue();
      defs.set(name, converter.convert(openApi, s));
    }

    try {
      return mapper.writeValueAsString(doc);
    } catch (Exception e) {
      throw new IllegalStateException("Failed to serialize schema document", e);
    }
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
