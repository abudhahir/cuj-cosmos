package com.cleveloper.utilities.jsonschema.generation;

import static org.assertj.core.api.Assertions.assertThat;

import com.cleveloper.utilities.jsonschema.generation.VicToolsSchemaGeneratorTest.Person;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class VicToolsGoldenSchemaTest {

  @Test
  void shouldMatchGoldenForPersonPojo() throws Exception {
    String expected = Files.readString(Path.of("src/test/resources/golden/person-schema.json"));
    String actual = new VicToolsSchemaGenerator().generateFor(Person.class);
    ObjectMapper mapper = new ObjectMapper();
    JsonNode exp = mapper.readTree(expected);
    JsonNode act = mapper.readTree(actual);
    assertThat(act).isEqualTo(exp);
  }
}
