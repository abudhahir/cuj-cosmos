package com.cleveloper.utilities.jsonschema.autoconfig;

import static org.assertj.core.api.Assertions.assertThat;

import com.cleveloper.utilities.jsonschema.JsonComparator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

class JsonComparatorAutoConfigurationTest {

  private final ApplicationContextRunner runner =
      new ApplicationContextRunner()
          .withConfiguration(AutoConfigurations.of(JsonUtilityAutoConfiguration.class));

  @Test
  void shouldAutoConfigureJsonComparator() {
    runner.run(
        ctx -> {
          assertThat(ctx).hasSingleBean(JsonComparator.class);
          assertThat(ctx.getBean(JsonComparator.class)).isNotNull();
        });
  }
}

