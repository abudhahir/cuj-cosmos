package com.cleveloper.utilities.jsonschema.openapi;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

class OpenApiGoldenSchemaTest {

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
  void petComponentShouldMatchGolden() throws Exception {
    runner.run(
        ctx -> {
          OpenApiSchemaService svc = ctx.getBean(OpenApiSchemaService.class);
          String actual = svc.generateFromRegistry("pet", "Pet");
          String expected = Files.readString(Path.of("src/test/resources/golden/pet-schema.json"));
          ObjectMapper mapper = new ObjectMapper();
          JsonNode act = mapper.readTree(actual);
          JsonNode exp = mapper.readTree(expected);
          assertThat(act).isEqualTo(exp);
        });
  }
}
