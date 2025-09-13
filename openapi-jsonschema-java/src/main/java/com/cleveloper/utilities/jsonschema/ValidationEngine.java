package com.cleveloper.utilities.jsonschema;

/**
 * Minimal validation interface for COSMOS utilities. Implementations should validate inputs and
 * fail fast.
 */
public interface ValidationEngine {

  /**
   * Returns true if the provided string appears to be JSON-like. This minimal stub checks non-null,
   * non-empty, and balanced braces.
   *
   * @param input raw string to validate
   * @return true if JSON-like; false otherwise
   */
  boolean isValid(String input);
}
