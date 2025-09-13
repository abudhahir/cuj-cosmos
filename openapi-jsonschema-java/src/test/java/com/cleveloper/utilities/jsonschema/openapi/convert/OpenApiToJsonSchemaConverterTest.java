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
  void shouldMapRefSchemasToDefs() throws Exception {
    String yaml = Files.readString(Path.of("src/test/resources/specs/petstore-min.yml"));
    OpenAPI api = new OpenApiParser().parse(yaml);
    Schema<?> petRef = api.getComponents().getSchemas().get("PetRef");

    OpenApiToJsonSchemaConverter conv = new OpenApiToJsonSchemaConverter();
    JsonNode node = conv.convert(api, petRef);
    assertThat(node.get("$ref").asText()).isEqualTo("#/$defs/Pet");
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
    assertThat(node.get("allOf").get(0).get("$ref").asText()).isEqualTo("#/$defs/PartA");
    assertThat(node.get("allOf").get(1).get("$ref").asText()).isEqualTo("#/$defs/PartB");
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

    // NumbersExclusive: exclusiveMinimum/exclusiveMaximum mapping to numeric values
    JsonNode exclusive =
        conv.convert(api, api.getComponents().getSchemas().get("NumbersExclusive"));
    assertThat(exclusive.has("exclusiveMinimum")).isTrue();
    assertThat(exclusive.has("exclusiveMaximum")).isTrue();
    assertThat(exclusive.has("minimum")).isFalse();
    assertThat(exclusive.has("maximum")).isFalse();

    // AdditionalProperties false
    JsonNode addlFalse = conv.convert(api, api.getComponents().getSchemas().get("AdditionalProps"));
    assertThat(addlFalse.get("additionalProperties").asBoolean()).isFalse();

    // AdditionalProperties schema
    JsonNode addlSchema =
        conv.convert(api, api.getComponents().getSchemas().get("AdditionalSchema"));
    assertThat(addlSchema.get("additionalProperties").get("type").asText()).isEqualTo("string");

    // Array constraints
    JsonNode arrayC = conv.convert(api, api.getComponents().getSchemas().get("ArrayConstraints"));
    assertThat(arrayC.get("minItems").asInt()).isEqualTo(1);
    assertThat(arrayC.get("maxItems").asInt()).isEqualTo(3);
    assertThat(arrayC.get("uniqueItems").asBoolean()).isTrue();

    // Object property count constraints
    JsonNode objC = conv.convert(api, api.getComponents().getSchemas().get("ObjectPropsCount"));
    assertThat(objC.get("minProperties").asInt()).isEqualTo(1);
    assertThat(objC.get("maxProperties").asInt()).isEqualTo(2);

    // multipleOf
    JsonNode mult = conv.convert(api, api.getComponents().getSchemas().get("MultipleOf"));
    assertThat(mult.get("multipleOf").asInt()).isEqualTo(5);
  }

  @Test
  void shouldMapNotAndAnnotations() throws Exception {
    String yaml = Files.readString(Path.of("src/test/resources/specs/advanced.yml"));
    OpenAPI api = new OpenApiParser().parse(yaml);
    OpenApiToJsonSchemaConverter conv = new OpenApiToJsonSchemaConverter();

    JsonNode notNode = conv.convert(api, api.getComponents().getSchemas().get("NotString"));
    assertThat(notNode.has("not")).isTrue();
    assertThat(notNode.get("not").get("type").asText()).isEqualTo("string");

    JsonNode ann = conv.convert(api, api.getComponents().getSchemas().get("Annotated"));
    assertThat(ann.get("readOnly").asBoolean()).isTrue();
    assertThat(ann.get("writeOnly").asBoolean()).isFalse();
    assertThat(ann.get("deprecated").asBoolean()).isTrue();
  }
}
