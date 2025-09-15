package com.cleveloper.utilities.jsonschema.generation.data;

import java.util.List;

/** Generates sample test data objects for a given type. */
public interface TestDataGenerator {
  <T> T generate(Class<T> type);

  <T> List<T> generateMany(Class<T> type, int count);
}

