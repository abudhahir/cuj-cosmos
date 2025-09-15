package com.cleveloper.utilities.jsonschema.openapi.convert;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.models.media.Schema;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OpenApiToJsonSchemaConverterEdgeCasesTest {

  @Test
  @DisplayName("should emit type [\"null\"] when nullable without explicit type")
  void shouldEmitNullTypeWhenNullableOnly() {
    Schema<?> s = new Schema<>().nullable(true);
    OpenApiToJsonSchemaConverter conv = new OpenApiToJsonSchemaConverter();
    JsonNode node = conv.convert(null, s);
    assertThat(node.has("type")).isTrue();
    assertThat(node.get("type").isArray()).isTrue();
    assertThat(node.get("type").toString()).contains("\"null\"");
  }

  @Test
  @DisplayName("should place $ref under additionalProperties using $defs prefix")
  void shouldRefInAdditionalProperties() {
    Schema<Object> addl = new Schema<>().$ref("#/components/schemas/Other");
    Schema<Object> wrapper = new Schema<>()
        .type("object")
        .additionalProperties(addl);
    OpenApiToJsonSchemaConverter conv = new OpenApiToJsonSchemaConverter(true);
    JsonNode node = conv.convert(null, wrapper);
    assertThat(node.get("additionalProperties").get("$ref").asText()).isEqualTo("#/$defs/Other");
  }
}

