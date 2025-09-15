package com.cleveloper.utilities.jsonschema.generation.data;

import java.util.ArrayList;
import java.util.List;
import org.instancio.Instancio;

/** Instancio-based generator producing realistic random data. */
public class InstancioTestDataGenerator implements TestDataGenerator {

  @Override
  public <T> T generate(Class<T> type) {
    return Instancio.create(type);
  }

  @Override
  public <T> List<T> generateMany(Class<T> type, int count) {
    int n = Math.max(0, count);
    List<T> list = new ArrayList<>(n);
    for (int i = 0; i < n; i++) {
      list.add(Instancio.create(type));
    }
    return list;
  }
}

