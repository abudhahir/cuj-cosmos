package com.cleveloper.utilities.jsonschema.openapi;

import static org.assertj.core.api.Assertions.assertThat;

import io.swagger.v3.oas.models.OpenAPI;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class OpenApiParserTest {

  @Test
  void shouldParseYamlContent() throws Exception {
    String yaml = Files.readString(Path.of("src/test/resources/specs/petstore-min.yml"));
    OpenAPI api = new OpenApiParser().parse(yaml);
    assertThat(api).isNotNull();
    assertThat(api.getComponents().getSchemas()).containsKeys("Pet", "PetRef");
  }
}
