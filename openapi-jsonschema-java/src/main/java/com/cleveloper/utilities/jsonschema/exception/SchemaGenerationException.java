package com.cleveloper.utilities.jsonschema.exception;

/** Exception thrown when schema generation fails. */
public class SchemaGenerationException extends JsonUtilityException {
  public SchemaGenerationException(String message, Class<?> targetClass) {
    super(message, "GEN_001");
    addContext("targetClass", targetClass != null ? targetClass.getName() : "<unknown>");
  }

  public SchemaGenerationException(String message, Class<?> targetClass, Throwable cause) {
    super(message, "GEN_001", cause);
    addContext("targetClass", targetClass != null ? targetClass.getName() : "<unknown>");
  }
}
