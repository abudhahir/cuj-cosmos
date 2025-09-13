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
  private final boolean useDefsRef;

  /** Creates a converter that references components via $ref: #/$defs/Name. */
  public OpenApiToJsonSchemaConverter() {
    this(true);
  }

  /**
   * @param useDefsRef when true, component references are emitted as $ref: #/$defs/Name; when
   *     false, component schemas are dereferenced and inlined.
   */
  public OpenApiToJsonSchemaConverter(boolean useDefsRef) {
    this.useDefsRef = useDefsRef;
  }

  /** Convert the provided OpenAPI Schema object to a JSON Schema node. */
  public JsonNode convert(OpenAPI openApi, Schema<?> component) {
    if (component == null) return mapper.createObjectNode();
    String ref = component.get$ref();
    if (ref != null && !ref.isBlank()) {
      if (useDefsRef) {
        ObjectNode refNode = mapper.createObjectNode();
        String name = ref.substring(ref.lastIndexOf('/') + 1);
        refNode.put("$ref", "#/$defs/" + name);
        return refNode;
      } else {
        return convertSchema(openApi, deref(openApi, component));
      }
    }
    return convertSchema(openApi, component);
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
    applyArrayObjectConstraints(node, schema);

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
    Boolean exclusiveMinFlag = schema.getExclusiveMinimum();
    Boolean exclusiveMaxFlag = schema.getExclusiveMaximum();
    BigDecimal multipleOf = schema.getMultipleOf();

    if (exclusiveMinFlag != null && exclusiveMinFlag && minimum != null) {
      node.put("exclusiveMinimum", minimum);
    } else if (minimum != null) {
      node.put("minimum", minimum);
    }
    if (exclusiveMaxFlag != null && exclusiveMaxFlag && maximum != null) {
      node.put("exclusiveMaximum", maximum);
    } else if (maximum != null) {
      node.put("maximum", maximum);
    }
    if (multipleOf != null) node.put("multipleOf", multipleOf);
  }

  private void applyArrayObjectConstraints(ObjectNode node, Schema<?> schema) {
    // Array
    Integer minItems = schema.getMinItems();
    Integer maxItems = schema.getMaxItems();
    Boolean uniqueItems = schema.getUniqueItems();
    if (minItems != null) node.put("minItems", minItems);
    if (maxItems != null) node.put("maxItems", maxItems);
    if (uniqueItems != null) node.put("uniqueItems", uniqueItems);

    // Object
    Integer minProps = schema.getMinProperties();
    Integer maxProps = schema.getMaxProperties();
    if (minProps != null) node.put("minProperties", minProps);
    if (maxProps != null) node.put("maxProperties", maxProps);

    Object addl = schema.getAdditionalProperties();
    if (addl != null) {
      if (addl instanceof Boolean) {
        node.put("additionalProperties", (Boolean) addl);
      } else if (addl instanceof Schema) {
        node.set("additionalProperties", convert(null, (Schema<?>) addl));
      }
    }

    // Metadata
    if (schema.getDescription() != null) node.put("description", schema.getDescription());
    if (schema.getTitle() != null) node.put("title", schema.getTitle());
    if (schema.getDefault() != null) node.set("default", mapper.valueToTree(schema.getDefault()));
    if (schema.getExample() != null) {
      ArrayNode examples = node.putArray("examples");
      examples.addPOJO(schema.getExample());
    }
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
