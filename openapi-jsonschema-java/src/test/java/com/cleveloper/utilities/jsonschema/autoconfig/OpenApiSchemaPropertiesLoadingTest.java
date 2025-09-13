package com.cleveloper.utilities.jsonschema.autoconfig;

import static org.assertj.core.api.Assertions.assertThat;

import com.cleveloper.utilities.jsonschema.openapi.OpenApiSchemaService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

class OpenApiSchemaPropertiesLoadingTest {

  private final ApplicationContextRunner runner =
      new ApplicationContextRunner()
          .withConfiguration(AutoConfigurations.of(JsonUtilityAutoConfiguration.class))
          .withPropertyValues(
              "cosmos.openapi.specs[0].id=pet",
              "cosmos.openapi.specs[0].location=classpath:specs/petstore-min.yml",
              "cosmos.schema.uri=https://example.org/custom-dialect",
              "cosmos.schema.id-prefix=urn:test:");

  @Test
  void shouldApplySchemaProperties() {
    runner.run(
        ctx -> {
          OpenApiSchemaService svc = ctx.getBean(OpenApiSchemaService.class);
          String json = svc.generateFromRegistry("pet", "Pet");
          assertThat(json).contains("\"$schema\":\"https://example.org/custom-dialect\"");
          assertThat(json).contains("\"$id\":\"urn:test:Pet\"");
        });
  }
}
