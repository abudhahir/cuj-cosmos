package com.cleveloper.utilities.jsonschema.openapi;

import static org.assertj.core.api.Assertions.assertThat;

import com.cleveloper.utilities.jsonschema.openapi.convert.OpenApiToJsonSchemaConverter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.OpenAPI;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Unit-style test constructing the service directly to verify $schema/$id customization. */
class DefaultOpenApiSchemaServiceUnitTest {

  @Test
  void shouldCustomizeSchemaUriAndIdPrefix() throws Exception {
    String yaml = Files.readString(Path.of("src/test/resources/specs/petstore-min.yml"));
    OpenAPI api = new OpenApiParser().parse(yaml);

    // Registry stub
    OpenApiSpecRegistry reg =
        new OpenApiSpecRegistry(
            new OpenApiParser(), new com.cleveloper.utilities.jsonschema.cache.MapSpecCache());
    // manually seed registry map via reflection-unsafe path: use properties loader is overkill, we
    // can call get/put via cache
    // Instead, call generateFromOpenApi directly.

    DefaultOpenApiSchemaService svc =
        new DefaultOpenApiSchemaService(
            reg,
            new OpenApiToJsonSchemaConverter(true),
            new ObjectMapper(),
            "https://example.org/schema",
            "urn:custom:");
    String json = svc.generateFromOpenApi(api, "Pet");

    ObjectMapper mapper = new ObjectMapper();
    JsonNode root = mapper.readTree(json);
    assertThat(root.get("$schema").asText()).isEqualTo("https://example.org/schema");
    assertThat(root.get("$id").asText()).startsWith("urn:custom:");
  }
}
