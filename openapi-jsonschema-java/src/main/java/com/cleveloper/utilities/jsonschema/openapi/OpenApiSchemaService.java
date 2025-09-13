package com.cleveloper.utilities.jsonschema.openapi;

import io.swagger.v3.oas.models.OpenAPI;

/** API for generating JSON Schema documents from OpenAPI components. */
public interface OpenApiSchemaService {

  /**
   * Generate a standalone JSON Schema document (Draft 2020-12) for a component. Includes $schema,
   * $id, and $defs for components in the OpenAPI document.
   *
   * @param openApi parsed OpenAPI document
   * @param componentName component schema name under components/schemas
   * @return JSON string of the schema document
   */
  String generateFromOpenApi(OpenAPI openApi, String componentName);

  /**
   * Generate a schema document by looking up the OpenAPI spec in the registry by id.
   *
   * @param specId id registered in OpenApiSpecRegistry
   * @param componentName component schema name under components/schemas
   * @return JSON string of the schema document
   */
  String generateFromRegistry(String specId, String componentName);
}
