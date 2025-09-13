package com.cleveloper.utilities.jsonschema;

/**
 * Generates JSON Schemas from sources (e.g., POJOs, OpenAPI components).
 */
public interface SchemaGenerator {
    /**
     * Generate a JSON Schema for the given class.
     *
     * @param type the Java type to analyze
     * @return a textual JSON Schema representation
     */
    String generateFor(Class<?> type);
}

