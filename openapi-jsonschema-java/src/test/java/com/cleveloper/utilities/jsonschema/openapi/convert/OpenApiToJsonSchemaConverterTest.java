package com.cleveloper.utilities.jsonschema.openapi.convert;

import static org.assertj.core.api.Assertions.assertThat;

import com.cleveloper.utilities.jsonschema.openapi.OpenApiParser;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import org.junit.jupiter.api.Test;

class OpenApiToJsonSchemaConverterTest {

  @Test
  void shouldConvertSimpleObjectSchema() throws Exception {
    String yaml = Files.readString(Path.of("src/test/resources/specs/petstore-min.yml"));
    OpenAPI api = new OpenApiParser().parse(yaml);
    Schema<?> pet = api.getComponents().getSchemas().get("Pet");

    OpenApiToJsonSchemaConverter conv = new OpenApiToJsonSchemaConverter();
    JsonNode node = conv.convert(api, pet);

    assertThat(node.get("type").asText()).isEqualTo("object");
    assertThat(node.get("properties").has("id")).isTrue();
    assertThat(node.get("properties").get("id").get("type").asText()).isEqualTo("integer");
    assertThat(node.get("properties").has("name")).isTrue();
  }

  @Test
  void shouldDereferenceRefSchemas() throws Exception {
    String yaml = Files.readString(Path.of("src/test/resources/specs/petstore-min.yml"));
    OpenAPI api = new OpenApiParser().parse(yaml);
    Schema<?> petRef = api.getComponents().getSchemas().get("PetRef");

    OpenApiToJsonSchemaConverter conv = new OpenApiToJsonSchemaConverter();
    JsonNode node = conv.convert(api, petRef);
    assertThat(node.get("type").asText()).isEqualTo("object");
    assertThat(node.get("properties").has("id")).isTrue();
    assertThat(node.get("properties").has("name")).isTrue();
  }

  @Test
  void shouldHandleNullableAndEnum() throws Exception {
    String yaml = Files.readString(Path.of("src/test/resources/specs/nullable-enum.yml"));
    OpenAPI api = new OpenApiParser().parse(yaml);
    Map<String, Schema> schemas = api.getComponents().getSchemas();
    Schema<?> user = schemas.get("User");

    OpenApiToJsonSchemaConverter conv = new OpenApiToJsonSchemaConverter();
    JsonNode node = conv.convert(api, user);

    JsonNode nicknameType = node.get("properties").get("nickname").get("type");
    assertThat(nicknameType.isArray()).isTrue();
    assertThat(nicknameType.toString()).contains("\"string\"").contains("\"null\"");

    JsonNode roleEnum = node.get("properties").get("role").get("enum");
    assertThat(roleEnum.isArray()).isTrue();
    assertThat(roleEnum.toString()).contains("ADMIN").contains("USER");
  }

  @Test
  void shouldMapAllOfComposition() throws Exception {
    String yaml = Files.readString(Path.of("src/test/resources/specs/composition.yml"));
    OpenAPI api = new OpenApiParser().parse(yaml);
    Schema<?> comp = api.getComponents().getSchemas().get("Composed");
    OpenApiToJsonSchemaConverter conv = new OpenApiToJsonSchemaConverter();
    JsonNode node = conv.convert(api, comp);
    assertThat(node.has("allOf")).isTrue();
    assertThat(node.get("allOf").isArray()).isTrue();
    assertThat(node.get("allOf").get(0).get("properties").has("a")).isTrue();
    assertThat(node.get("allOf").get(1).get("properties").has("b")).isTrue();
  }

  @Test
  void shouldMapConstraintsAndItemsAndOneOf() throws Exception {
    String yaml = Files.readString(Path.of("src/test/resources/specs/constraints.yml"));
    OpenAPI api = new OpenApiParser().parse(yaml);
    OpenApiToJsonSchemaConverter conv = new OpenApiToJsonSchemaConverter();

    // Numbers: minimum/maximum
    JsonNode numbers = conv.convert(api, api.getComponents().getSchemas().get("Numbers"));
    assertThat(numbers.get("type").asText()).isEqualTo("integer");
    assertThat(numbers.get("minimum").asInt()).isEqualTo(1);
    assertThat(numbers.get("maximum").asInt()).isEqualTo(10);

    // Text: string constraints
    JsonNode text = conv.convert(api, api.getComponents().getSchemas().get("Text"));
    assertThat(text.get("type").asText()).isEqualTo("string");
    assertThat(text.get("minLength").asInt()).isEqualTo(3);
    assertThat(text.get("maxLength").asInt()).isEqualTo(5);
    assertThat(text.get("pattern").asText()).contains("^");

    // Choice: oneOf
    JsonNode choice = conv.convert(api, api.getComponents().getSchemas().get("Choice"));
    assertThat(choice.has("oneOf")).isTrue();
    assertThat(choice.get("oneOf").size()).isEqualTo(2);

    // Bag: array items
    JsonNode bag = conv.convert(api, api.getComponents().getSchemas().get("Bag"));
    assertThat(bag.get("type").asText()).isEqualTo("array");
    assertThat(bag.get("items").get("type").asText()).isEqualTo("string");
  }
}
