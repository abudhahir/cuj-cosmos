package com.cleveloper.utilities.jsonschema.openapi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

class PerformanceSmokeTest {

  private final ApplicationContextRunner runner =
      new ApplicationContextRunner()
          .withConfiguration(
              AutoConfigurations.of(
                  com.cleveloper.utilities.jsonschema.autoconfig.JsonUtilityAutoConfiguration
                      .class))
          .withPropertyValues(
              "cosmos.openapi.specs[0].id=pet",
              "cosmos.openapi.specs[0].location=classpath:specs/petstore-min.yml");

  @Test
  void generateFromRegistryShouldCompleteQuickly() {
    runner.run(
        ctx -> {
          OpenApiSchemaService svc = ctx.getBean(OpenApiSchemaService.class);
          assertTimeoutPreemptively(
              Duration.ofSeconds(2),
              () -> {
                String json = svc.generateFromRegistry("pet", "Pet");
                assertThat(json).isNotBlank();
              });
        });
  }
}

