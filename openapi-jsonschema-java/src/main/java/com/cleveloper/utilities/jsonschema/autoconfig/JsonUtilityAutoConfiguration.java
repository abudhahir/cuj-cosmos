package com.cleveloper.utilities.jsonschema.autoconfig;

import com.cleveloper.utilities.jsonschema.DefaultValidationEngine;
import com.cleveloper.utilities.jsonschema.ValidationEngine;
import com.cleveloper.utilities.jsonschema.cache.CaffeineSpecCache;
import com.cleveloper.utilities.jsonschema.cache.SpecCache;
import com.cleveloper.utilities.jsonschema.config.OpenApiSpecsProperties;
import com.cleveloper.utilities.jsonschema.openapi.OpenApiParser;
import com.cleveloper.utilities.jsonschema.openapi.OpenApiSpecRegistry;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ResourceLoader;

/** Minimal Spring Boot auto-configuration for COSMOS utilities. */
@AutoConfiguration
@EnableConfigurationProperties(OpenApiSpecsProperties.class)
public class JsonUtilityAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(ValidationEngine.class)
  public ValidationEngine validationEngine() {
    return new DefaultValidationEngine();
  }

  @Bean
  @ConditionalOnMissingBean
  public OpenApiParser openApiParser() {
    return new OpenApiParser();
  }

  @Bean
  @ConditionalOnMissingBean(SpecCache.class)
  public SpecCache specCache() {
    return new CaffeineSpecCache();
  }

  @Bean
  @ConditionalOnMissingBean
  public OpenApiSpecRegistry openApiSpecRegistry(
      OpenApiParser parser,
      SpecCache cache,
      OpenApiSpecsProperties properties,
      ResourceLoader resourceLoader) {
    OpenApiSpecRegistry registry = new OpenApiSpecRegistry(parser, cache);
    registry.loadFromProperties(properties, resourceLoader);
    return registry;
  }
}
