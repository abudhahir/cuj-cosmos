package com.cleveloper.utilities.jsonschema.autoconfig;

import static org.assertj.core.api.Assertions.assertThat;

import com.cleveloper.utilities.jsonschema.openapi.OpenApiSchemaService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

class OpenApiInlineRefsPropertiesLoadingTest {

  private final ApplicationContextRunner runner =
      new ApplicationContextRunner()
          .withConfiguration(AutoConfigurations.of(JsonUtilityAutoConfiguration.class))
          .withPropertyValues(
              "cosmos.openapi.specs[0].id=comp",
              "cosmos.openapi.specs[0].location=classpath:specs/composition.yml",
              "cosmos.schema.inline-refs=true");

  @Test
  void shouldInlineRefsWhenPropertyEnabled() {
    runner.run(
        ctx -> {
          OpenApiSchemaService svc = ctx.getBean(OpenApiSchemaService.class);
          String json = svc.generateFromRegistry("comp", "Composed");
          ObjectMapper mapper = new ObjectMapper();
          JsonNode root = mapper.readTree(json);
          assertThat(root.get("$defs").size()).isEqualTo(0);
          assertThat(root.get("allOf").get(0).has("$ref")).isFalse();
          assertThat(root.get("allOf").get(1).has("$ref")).isFalse();
          assertThat(root.get("allOf").get(0).get("properties").has("a")).isTrue();
          assertThat(root.get("allOf").get(1).get("properties").has("b")).isTrue();
        });
  }
}
