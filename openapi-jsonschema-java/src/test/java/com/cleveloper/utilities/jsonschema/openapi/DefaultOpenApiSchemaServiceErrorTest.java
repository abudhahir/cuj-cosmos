package com.cleveloper.utilities.jsonschema.openapi;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.cleveloper.utilities.jsonschema.cache.MapSpecCache;
import com.cleveloper.utilities.jsonschema.openapi.convert.OpenApiToJsonSchemaConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.OpenAPI;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DefaultOpenApiSchemaServiceErrorTest {

  @Test
  @DisplayName("should throw on null OpenAPI or blank/missing component names")
  void shouldThrowOnInvalidInputs() throws Exception {
    OpenApiSpecRegistry reg = new OpenApiSpecRegistry(new OpenApiParser(), new MapSpecCache());
    DefaultOpenApiSchemaService svc =
        new DefaultOpenApiSchemaService(
            reg, new OpenApiToJsonSchemaConverter(true), new ObjectMapper(), null, null);

    assertThatThrownBy(() -> svc.generateFromOpenApi(null, "Pet"))
        .isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> svc.generateFromOpenApi(new OpenAPI(), " "))
        .isInstanceOf(IllegalArgumentException.class);

    String yaml = Files.readString(Path.of("src/test/resources/specs/petstore-min.yml"));
    OpenAPI api = new OpenApiParser().parse(yaml);
    assertThatThrownBy(() -> svc.generateFromOpenApi(api, "DoesNotExist"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Component not found");
  }

  @Test
  @DisplayName("should throw when spec id is not found in registry")
  void shouldThrowWhenSpecIdNotFound() {
    OpenApiSpecRegistry reg = new OpenApiSpecRegistry(new OpenApiParser(), new MapSpecCache());
    DefaultOpenApiSchemaService svc = new DefaultOpenApiSchemaService(reg);
    assertThatThrownBy(() -> svc.generateFromRegistry("unknown", "Pet"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Spec not found");
  }
}

