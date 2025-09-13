package com.cleveloper.utilities.jsonschema.validation;

import java.util.List;

/** Validates JSON instances against JSON Schemas and returns detailed errors. */
public interface SchemaValidator {
  /**
   * Validate the given JSON instance against the provided JSON Schema (both as strings).
   *
   * @param schemaJson JSON Schema document (Draft 2020-12)
   * @param instanceJson JSON instance document
   * @return list of error messages (empty if valid)
   */
  List<String> validate(String schemaJson, String instanceJson);
}
