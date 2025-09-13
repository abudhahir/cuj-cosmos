package com.cleveloper.utilities.jsonschema.openapi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

  @Test
  void shouldParseFromPath() {
    OpenAPI api =
        new OpenApiParser().parsePath(Path.of("src/test/resources/specs/petstore-min.yml"));
    assertThat(api).isNotNull();
    assertThat(api.getComponents().getSchemas()).containsKey("Pet");
  }

  @Test
  void shouldParseFromUrl() {
    String url =
        Path.of("src/test/resources/specs/petstore-min.yml").toAbsolutePath().toUri().toString();
    OpenAPI api = new OpenApiParser().parseUrl(url);
    assertThat(api).isNotNull();
    assertThat(api.getComponents().getSchemas()).containsKey("Pet");
  }

  @Test
  void shouldFailOnEmptyInputs() {
    OpenApiParser p = new OpenApiParser();
    assertThatThrownBy(() -> p.parse("   ")).isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> p.parseUrl(" ")).isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> p.parsePath(null)).isInstanceOf(IllegalArgumentException.class);
  }
}
