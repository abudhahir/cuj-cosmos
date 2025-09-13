package com.cleveloper.utilities.jsonschema.autoconfig;

import com.cleveloper.utilities.jsonschema.DefaultValidationEngine;
import com.cleveloper.utilities.jsonschema.SchemaGenerator;
import com.cleveloper.utilities.jsonschema.ValidationEngine;
import com.cleveloper.utilities.jsonschema.cache.CaffeineSpecCache;
import com.cleveloper.utilities.jsonschema.cache.SpecCache;
import com.cleveloper.utilities.jsonschema.config.OpenApiSchemaProperties;
import com.cleveloper.utilities.jsonschema.config.OpenApiSpecsProperties;
import com.cleveloper.utilities.jsonschema.generation.VicToolsSchemaGenerator;
import com.cleveloper.utilities.jsonschema.openapi.DefaultOpenApiSchemaService;
import com.cleveloper.utilities.jsonschema.openapi.OpenApiParser;
import com.cleveloper.utilities.jsonschema.openapi.OpenApiSchemaService;
import com.cleveloper.utilities.jsonschema.openapi.OpenApiSpecRegistry;
import com.cleveloper.utilities.jsonschema.validation.NetworkntSchemaValidator;
import com.cleveloper.utilities.jsonschema.validation.SchemaValidator;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ResourceLoader;

/** Minimal Spring Boot auto-configuration for COSMOS utilities. */
@AutoConfiguration
@EnableConfigurationProperties({OpenApiSpecsProperties.class, OpenApiSchemaProperties.class})
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

  @Bean
  @ConditionalOnMissingBean(SchemaGenerator.class)
  public SchemaGenerator schemaGenerator() {
    return new VicToolsSchemaGenerator();
  }

  @Bean
  @ConditionalOnMissingBean(OpenApiSchemaService.class)
  public OpenApiSchemaService openApiSchemaService(
      OpenApiSpecRegistry registry, OpenApiSchemaProperties schemaProps) {
    String uri = schemaProps != null ? schemaProps.getUri() : null;
    String idPrefix = schemaProps != null ? schemaProps.getIdPrefix() : null;
    return new DefaultOpenApiSchemaService(registry, null, null, uri, idPrefix);
  }

  @Bean
  @ConditionalOnMissingBean(SchemaValidator.class)
  public SchemaValidator schemaValidator() {
    return new NetworkntSchemaValidator();
  }
}
