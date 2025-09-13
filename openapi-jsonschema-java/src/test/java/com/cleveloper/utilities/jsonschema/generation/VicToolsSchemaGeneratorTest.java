package com.cleveloper.utilities.jsonschema.generation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.cleveloper.utilities.jsonschema.exception.SchemaGenerationException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

class VicToolsSchemaGeneratorTest {

  static class Person {
    public String name;
    public int age;
  }

  @Test
  void shouldGenerateSchemaForPojo() throws Exception {
    VicToolsSchemaGenerator gen = new VicToolsSchemaGenerator();
    String json = gen.generateFor(Person.class);

    ObjectMapper mapper = new ObjectMapper();
    JsonNode node = mapper.readTree(json);
    assertThat(node.get("type").asText()).isEqualTo("object");
    assertThat(node.get("properties")).isNotNull();
    assertThat(node.get("properties").has("name")).isTrue();
    assertThat(node.get("properties").has("age")).isTrue();
  }

  @Test
  void shouldFailFastOnNullType() {
    VicToolsSchemaGenerator gen = new VicToolsSchemaGenerator();
    assertThatThrownBy(() -> gen.generateFor(null))
        .isInstanceOf(SchemaGenerationException.class)
        .hasMessageContaining("Type must not be null");
  }
}
