package com.cleveloper.utilities.jsonschema.autoconfig;

import static org.assertj.core.api.Assertions.assertThat;

import com.cleveloper.utilities.jsonschema.ValidationEngine;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

class JsonUtilityAutoConfigurationTest {

  private final ApplicationContextRunner contextRunner =
      new ApplicationContextRunner()
          .withConfiguration(AutoConfigurations.of(JsonUtilityAutoConfiguration.class));

  @Test
  void shouldProvideDefaultValidationEngineBean() {
    contextRunner.run(
        ctx -> {
          assertThat(ctx).hasSingleBean(ValidationEngine.class);
          assertThat(ctx.getBean(ValidationEngine.class)).isNotNull();
        });
  }
}
