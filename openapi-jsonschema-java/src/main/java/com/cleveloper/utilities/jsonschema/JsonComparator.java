package com.cleveloper.utilities.jsonschema;

/** Compares two JSON documents and returns a human-readable diff summary. */
public interface JsonComparator {
  /**
   * Compare two JSON strings and return a summary (empty if equal).
   *
   * @param expected expected JSON
   * @param actual actual JSON
   * @return diff summary; empty if logically equivalent
   */
  String compare(String expected, String actual);
}
