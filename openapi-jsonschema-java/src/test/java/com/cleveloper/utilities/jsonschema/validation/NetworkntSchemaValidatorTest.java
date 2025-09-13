package com.cleveloper.utilities.jsonschema.validation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class NetworkntSchemaValidatorTest {

  @Test
  void shouldReturnErrorsForInvalidInstance() {
    String schema =
        """
        {
          "$schema": "https://json-schema.org/draft/2020-12/schema",
          "$id": "https://example.org/schemas/User",
          "type": "object",
          "properties": {"name": {"type": "string"}},
          "required": ["name"]
        }
        """;
    String instance = "{}";
    SchemaValidator validator = new NetworkntSchemaValidator();
    var errors = validator.validate(schema, instance);
    assertThat(errors).isNotEmpty();
    assertThat(String.join(";", errors)).contains("name");
  }

  @Test
  void shouldSucceedForValidInstance() {
    String schema =
        """
        {
          "$schema": "https://json-schema.org/draft/2020-12/schema",
          "$id": "https://example.org/schemas/User",
          "type": "object",
          "properties": {"name": {"type": "string"}},
          "required": ["name"]
        }
        """;
    String instance = "{" + "\"name\":\"Alice\"" + "}";
    SchemaValidator validator = new NetworkntSchemaValidator();
    var errors = validator.validate(schema, instance);
    assertThat(errors).isEmpty();
  }
}
