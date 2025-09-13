package com.cleveloper.utilities.jsonschema.generation;

import com.cleveloper.utilities.jsonschema.SchemaGenerator;
import com.cleveloper.utilities.jsonschema.exception.SchemaGenerationException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.victools.jsonschema.generator.OptionPreset;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfig;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.generator.SchemaVersion;

/**
 * Schema generator backed by VicTools jsonschema-generator targeting Draft 2020-12.
 *
 * <p>This implementation focuses on POJO-to-JSON-Schema generation. OpenAPI component conversion is
 * handled separately by converter utilities.
 */
public class VicToolsSchemaGenerator implements SchemaGenerator {

  private final ObjectMapper objectMapper;
  private final com.github.victools.jsonschema.generator.SchemaGenerator delegate;

  /** Create a generator with default modules and configuration. */
  public VicToolsSchemaGenerator() {
    this(new ObjectMapper(), defaultConfig());
  }

  /** Create a generator with a custom ObjectMapper and configuration. */
  public VicToolsSchemaGenerator(ObjectMapper objectMapper, SchemaGeneratorConfig config) {
    this.objectMapper = objectMapper == null ? new ObjectMapper() : objectMapper;
    this.delegate = new com.github.victools.jsonschema.generator.SchemaGenerator(config);
  }

  private static SchemaGeneratorConfig defaultConfig() {
    SchemaGeneratorConfigBuilder builder =
        new SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_2020_12, OptionPreset.PLAIN_JSON);
    // Best-effort: try to register optional modules if present on classpath
    tryRegisterJacksonModule(builder);
    tryRegisterJavaxValidationModule(builder);
    return builder.build();
  }

  private static void tryRegisterJacksonModule(SchemaGeneratorConfigBuilder builder) {
    try {
      com.github.victools.jsonschema.generator.Module module =
          (com.github.victools.jsonschema.generator.Module)
              Class.forName("com.github.victools.jsonschema.module.jackson.JacksonModule")
                  .getConstructor()
                  .newInstance();
      builder.with(module);
    } catch (Throwable ignored) {
      // Module not present or failed to load; proceed without it
    }
  }

  private static void tryRegisterJavaxValidationModule(SchemaGeneratorConfigBuilder builder) {
    try {
      com.github.victools.jsonschema.generator.Module module =
          (com.github.victools.jsonschema.generator.Module)
              Class.forName(
                      "com.github.victools.jsonschema.module.javax.validation.JavaxValidationModule")
                  .getConstructor()
                  .newInstance();
      builder.with(module);
    } catch (Throwable ignored) {
      // Module not present or failed to load; proceed without it
    }
  }

  /** {@inheritDoc} */
  @Override
  public String generateFor(Class<?> type) {
    if (type == null) {
      throw new SchemaGenerationException("Type must not be null", null);
    }
    try {
      JsonNode node = delegate.generateSchema(type);
      return objectMapper.writeValueAsString(node);
    } catch (Exception ex) {
      throw new SchemaGenerationException("Failed to generate schema", type, ex);
    }
  }
}
