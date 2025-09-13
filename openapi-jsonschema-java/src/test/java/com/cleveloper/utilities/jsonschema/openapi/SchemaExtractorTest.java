package com.cleveloper.utilities.jsonschema.openapi;

import static org.assertj.core.api.Assertions.assertThat;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import org.junit.jupiter.api.Test;

class SchemaExtractorTest {

  @Test
  void shouldExtractSchemasAndDereference() throws Exception {
    String yaml = Files.readString(Path.of("src/test/resources/specs/petstore-min.yml"));
    OpenAPI api = new OpenApiParser().parse(yaml);

    SchemaExtractor extractor = new SchemaExtractor();
    Map<String, Schema> schemas = extractor.extractSchemas(api);
    assertThat(schemas).containsKeys("Pet", "PetRef");

    Schema ref = schemas.get("PetRef");
    Schema deref = extractor.dereference(ref, api);
    assertThat(deref).isNotNull();
    assertThat(deref.getProperties()).containsKeys("id", "name");
  }
}
