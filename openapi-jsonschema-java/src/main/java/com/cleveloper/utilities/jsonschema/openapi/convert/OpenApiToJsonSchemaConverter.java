package com.cleveloper.utilities.jsonschema.openapi.convert;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Converts OpenAPI component schemas to JSON Schema Draft 2020-12.
 *
 * <p>Supported (initial): type, properties, required, items, enum, format, nullable,
 * allOf/oneOf/anyOf, and simple $ref dereference within the same document.
 */
public class OpenApiToJsonSchemaConverter {

  private final ObjectMapper mapper = new ObjectMapper();

  /** Convert the provided OpenAPI Schema object to a JSON Schema node. */
  public JsonNode convert(OpenAPI openApi, Schema<?> component) {
    if (component == null) return mapper.createObjectNode();
    Schema<?> schema = deref(openApi, component);
    return convertSchema(openApi, schema);
  }

  private ObjectNode convertSchema(OpenAPI openApi, Schema<?> schema) {
    ObjectNode node = mapper.createObjectNode();

    // Combinators
    if (schema.getAllOf() != null && !schema.getAllOf().isEmpty()) {
      ArrayNode arr = node.putArray("allOf");
      for (Schema<?> s : schema.getAllOf()) {
        arr.add(convert(openApi, s));
      }
    }
    if (schema.getOneOf() != null && !schema.getOneOf().isEmpty()) {
      ArrayNode arr = node.putArray("oneOf");
      for (Schema<?> s : schema.getOneOf()) {
        arr.add(convert(openApi, s));
      }
    }
    if (schema.getAnyOf() != null && !schema.getAnyOf().isEmpty()) {
      ArrayNode arr = node.putArray("anyOf");
      for (Schema<?> s : schema.getAnyOf()) {
        arr.add(convert(openApi, s));
      }
    }

    // Type/format, nullable handling
    String type = schema.getType();
    boolean nullable = Boolean.TRUE.equals(schema.getNullable());
    if (type != null) {
      if (nullable) {
        ArrayNode typeArray = node.putArray("type");
        typeArray.add(type);
        typeArray.add("null");
      } else {
        node.put("type", type);
      }
    } else if (nullable) {
      ArrayNode typeArray = node.putArray("type");
      typeArray.add("null");
    }
    if (schema.getFormat() != null) {
      node.put("format", schema.getFormat());
    }

    // Enum
    List<?> enumValues = schema.getEnum();
    if (enumValues != null && !enumValues.isEmpty()) {
      ArrayNode arr = node.putArray("enum");
      enumValues.forEach(v -> arr.addPOJO(v));
    }

    // Object properties
    Map<String, Schema> props = schema.getProperties();
    if (props != null && !props.isEmpty()) {
      ObjectNode propsNode = node.putObject("properties");
      for (Map.Entry<String, Schema> e : props.entrySet()) {
        propsNode.set(e.getKey(), convert(openApi, e.getValue()));
      }
    }
    if (schema.getRequired() != null && !schema.getRequired().isEmpty()) {
      ArrayNode req = node.putArray("required");
      schema.getRequired().forEach(req::add);
    }

    // Array items
    Schema<?> items = schema.getItems();
    if (items != null) {
      node.set("items", convert(openApi, items));
    }

    // Constraints (basic)
    applyNumericStringConstraints(node, schema);

    return node;
  }

  private void applyNumericStringConstraints(ObjectNode node, Schema<?> schema) {
    Integer minLength = schema.getMinLength();
    Integer maxLength = schema.getMaxLength();
    String pattern = schema.getPattern();
    if (minLength != null) node.put("minLength", minLength);
    if (maxLength != null) node.put("maxLength", maxLength);
    if (pattern != null) node.put("pattern", pattern);

    BigDecimal minimum = schema.getMinimum();
    BigDecimal maximum = schema.getMaximum();
    if (minimum != null) node.put("minimum", minimum);
    if (maximum != null) node.put("maximum", maximum);
  }

  private Schema<?> deref(OpenAPI openApi, Schema<?> schema) {
    String ref = schema.get$ref();
    if (ref == null || ref.isBlank()) {
      return schema;
    }
    String name = ref.substring(ref.lastIndexOf('/') + 1);
    if (openApi != null
        && openApi.getComponents() != null
        && openApi.getComponents().getSchemas() != null) {
      Schema<?> target = openApi.getComponents().getSchemas().get(name);
      return Objects.requireNonNullElse(target, schema);
    }
    return schema;
  }
}
