package com.cleveloper.utilities.jsonschema.openapi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.swagger.v3.oas.models.OpenAPI;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class OpenApiLoaderTest {

  @Test
  void parsePathShouldResolveExternalRefs() {
    Path base = Path.of("src/test/resources/specs/petstore-with-external.yml");
    String uri = base.toAbsolutePath().toUri().toString();
    OpenAPI api = new OpenApiParser().parseUrl(uri);
    assertThat(api).isNotNull();
    assertThat(api.getComponents()).isNotNull();
    assertThat(api.getComponents().getSchemas()).isNotNull();
    var schemas = api.getComponents().getSchemas();
    assertThat(schemas.size()).isGreaterThan(0);
  }

  @Test
  void parseShouldFailForInvalidContent() {
    String invalid = "openapi: not-a-version";
    assertThatThrownBy(() -> new OpenApiParser().parse(invalid))
        .isInstanceOf(IllegalArgumentException.class);
  }
}
