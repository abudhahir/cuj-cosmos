package com.cleveloper.utilities.jsonschema.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/** Extracts schemas and resolves simple $ref indirections. */
public class SchemaExtractor {

  public Map<String, Schema> extractSchemas(OpenAPI openAPI) {
    if (openAPI == null
        || openAPI.getComponents() == null
        || openAPI.getComponents().getSchemas() == null) {
      return Collections.emptyMap();
    }
    return new LinkedHashMap<>(openAPI.getComponents().getSchemas());
  }

  /** If the given schema is a $ref, return the dereferenced schema; otherwise the same instance. */
  public Schema dereference(Schema schema, OpenAPI openAPI) {
    if (schema == null) return null;
    String ref = schema.get$ref();
    if (ref == null || ref.isBlank()) return schema;
    // Expected format: #/components/schemas/Name
    String name = ref.substring(ref.lastIndexOf('/') + 1);
    return Optional.ofNullable(openAPI)
        .map(OpenAPI::getComponents)
        .map(c -> c.getSchemas() != null ? c.getSchemas().get(name) : null)
        .orElse(schema);
  }
}
