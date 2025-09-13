package com.cleveloper.utilities.jsonschema.autoconfig;

import com.cleveloper.utilities.jsonschema.DefaultValidationEngine;
import com.cleveloper.utilities.jsonschema.ValidationEngine;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/** Minimal Spring Boot auto-configuration for COSMOS utilities. */
@AutoConfiguration
public class JsonUtilityAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(ValidationEngine.class)
  public ValidationEngine validationEngine() {
    return new DefaultValidationEngine();
  }
}
