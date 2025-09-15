package com.cleveloper.utilities.jsonschema.openapi;

import static org.assertj.core.api.Assertions.assertThat;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SchemaExtractorEdgeCasesTest {

  @Test
  @DisplayName("should return empty map when OpenAPI is null or has no components")
  void shouldReturnEmptyMapForNullOrMissingComponents() {
    SchemaExtractor extractor = new SchemaExtractor();
    Map<String, Schema> empty1 = extractor.extractSchemas(null);
    assertThat(empty1).isEmpty();

    Map<String, Schema> empty2 = extractor.extractSchemas(new OpenAPI());
    assertThat(empty2).isEmpty();
  }

  @Test
  @DisplayName("dereference should return null when schema is null and same instance when OpenAPI is null")
  void dereferenceNulls() {
    SchemaExtractor extractor = new SchemaExtractor();
    // null schema
    Schema derefNull = extractor.dereference(null, new OpenAPI());
    assertThat(derefNull).isNull();

    // $ref with null OpenAPI should return the same instance
    Schema<?> s = new Schema<>().$ref("#/components/schemas/Thing");
    Schema<?> out = extractor.dereference(s, null);
    assertThat(out).isSameAs(s);
  }
}

