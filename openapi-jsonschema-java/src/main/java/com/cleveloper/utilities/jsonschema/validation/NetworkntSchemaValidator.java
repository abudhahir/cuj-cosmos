package com.cleveloper.utilities.jsonschema.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/** Implementation using NetworkNT json-schema-validator with Draft 2020-12. */
public class NetworkntSchemaValidator implements SchemaValidator {
  private final ObjectMapper mapper = new ObjectMapper();
  private final JsonSchemaFactory factory =
      JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V202012);

  @Override
  public List<String> validate(String schemaJson, String instanceJson) {
    try {
      JsonNode schemaNode = mapper.readTree(schemaJson);
      JsonNode instanceNode = mapper.readTree(instanceJson);

      JsonSchema schema = factory.getSchema(schemaNode);
      Set<ValidationMessage> messages = schema.validate(instanceNode);
      List<String> errors = new ArrayList<>();
      for (ValidationMessage vm : messages) {
        String msg = vm.getMessage();
        errors.add(msg);
      }
      return errors;
    } catch (Exception e) {
      List<String> errors = new ArrayList<>();
      errors.add("Validation failed to execute: " + e.getMessage());
      return errors;
    }
  }
}
