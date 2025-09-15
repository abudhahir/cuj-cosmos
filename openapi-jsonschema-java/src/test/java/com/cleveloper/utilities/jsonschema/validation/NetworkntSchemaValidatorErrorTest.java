package com.cleveloper.utilities.jsonschema.validation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NetworkntSchemaValidatorErrorTest {

  @Test
  @DisplayName("should return failure message when schema JSON is malformed")
  void shouldReturnFailureMessageWhenSchemaMalformed() {
    String badSchema = "{ this is not valid json }";
    String instance = "{}";
    SchemaValidator validator = new NetworkntSchemaValidator();
    var errors = validator.validate(badSchema, instance);
    assertThat(errors).isNotEmpty();
    assertThat(String.join(";", errors)).contains("Validation failed to execute");
  }

  @Test
  @DisplayName("should return failure message when instance JSON is malformed")
  void shouldReturnFailureMessageWhenInstanceMalformed() {
    String schema =
        """
        {
          "$schema": "https://json-schema.org/draft/2020-12/schema",
          "$id": "https://example.org/schemas/Thing",
          "type": "object"
        }
        """;
    String badInstance = "{ not-json }";
    SchemaValidator validator = new NetworkntSchemaValidator();
    var errors = validator.validate(schema, badInstance);
    assertThat(errors).isNotEmpty();
    assertThat(String.join(";", errors)).contains("Validation failed to execute");
  }
}

