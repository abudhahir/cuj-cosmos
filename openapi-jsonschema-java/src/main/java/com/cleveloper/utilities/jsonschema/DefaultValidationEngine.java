package com.cleveloper.utilities.jsonschema;

/** Default implementation with a lightweight JSON-like check. */
public class DefaultValidationEngine implements ValidationEngine {

  @Override
  public boolean isValid(String input) {
    if (input == null) return false;
    String s = input.trim();
    if (s.isEmpty()) return false;
    // Very lightweight shape check to avoid external deps.
    boolean objectLike = s.startsWith("{") && s.endsWith("}");
    boolean arrayLike = s.startsWith("[") && s.endsWith("]");
    return objectLike || arrayLike;
  }
}
