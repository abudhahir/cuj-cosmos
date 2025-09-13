package com.cleveloper.utilities.jsonschema.openapi;

import static org.assertj.core.api.Assertions.assertThat;

import com.cleveloper.utilities.jsonschema.autoconfig.JsonUtilityAutoConfiguration;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

class OpenApiSchemaServiceDefsOptimizationTest {

  private final ApplicationContextRunner runner =
      new ApplicationContextRunner()
          .withConfiguration(AutoConfigurations.of(JsonUtilityAutoConfiguration.class))
          .withPropertyValues(
              "cosmos.openapi.specs[0].id=pet",
              "cosmos.openapi.specs[0].location=classpath:specs/petstore-min.yml");

  @Test
  void shouldOnlyIncludeReferencedComponentsInDefs() {
    runner.run(
        ctx -> {
          OpenApiSchemaService svc = ctx.getBean(OpenApiSchemaService.class);
          String json = svc.generateFromRegistry("pet", "Pet");
          ObjectMapper mapper = new ObjectMapper();
          JsonNode root = mapper.readTree(json);
          // Pet root should be object with properties, and $defs should not contain PetRef (unused)
          assertThat(root.get("type").asText()).isEqualTo("object");
          assertThat(root.get("$defs").has("Pet")).isFalse(); // root inlines, not in $defs
          assertThat(root.get("$defs").has("PetRef")).isFalse();
        });
  }
}
