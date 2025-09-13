package com.cleveloper.utilities.jsonschema.autoconfig;

import static org.assertj.core.api.Assertions.assertThat;

import com.cleveloper.utilities.jsonschema.openapi.OpenApiSpecRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

class OpenApiPropertiesLoadingTest {

  private final ApplicationContextRunner runner =
      new ApplicationContextRunner()
          .withConfiguration(AutoConfigurations.of(JsonUtilityAutoConfiguration.class))
          .withPropertyValues(
              "cosmos.openapi.specs[0].id=pet1",
              "cosmos.openapi.specs[0].location=classpath:specs/petstore-min.yml",
              "cosmos.openapi.specs[1].id=pet2",
              "cosmos.openapi.specs[1].location=classpath:specs/petstore-with-external.yml");

  @Test
  void shouldLoadMultipleSpecsFromProperties() {
    runner.run(
        ctx -> {
          assertThat(ctx).hasSingleBean(OpenApiSpecRegistry.class);
          OpenApiSpecRegistry reg = ctx.getBean(OpenApiSpecRegistry.class);
          assertThat(reg.get("pet1")).isNotNull();
          assertThat(reg.get("pet2")).isNotNull();
        });
  }
}
