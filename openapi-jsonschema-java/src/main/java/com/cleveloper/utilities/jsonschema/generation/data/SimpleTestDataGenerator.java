package com.cleveloper.utilities.jsonschema.generation.data;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Very simple fallback generator when Instancio is not on the classpath.
 * Initializes public fields with simple defaults where possible.
 */
public class SimpleTestDataGenerator implements TestDataGenerator {
  private static final Random RND = new Random(12345L);

  @Override
  public <T> T generate(Class<T> type) {
    try {
      Constructor<T> ctor = type.getDeclaredConstructor();
      ctor.setAccessible(true);
      T instance = ctor.newInstance();
      for (Field f : type.getFields()) {
        Class<?> ft = f.getType();
        if (ft == String.class) f.set(instance, "str-" + RND.nextInt(1000));
        else if (ft == int.class || ft == Integer.class) f.set(instance, RND.nextInt(100));
        else if (ft == long.class || ft == Long.class) f.set(instance, Math.abs(RND.nextLong() % 1000));
        else if (ft == boolean.class || ft == Boolean.class) f.set(instance, RND.nextBoolean());
        else if (ft == double.class || ft == Double.class) f.set(instance, RND.nextDouble());
      }
      return instance;
    } catch (Exception e) {
      throw new IllegalStateException("Failed to instantiate " + type.getName(), e);
    }
  }

  @Override
  public <T> List<T> generateMany(Class<T> type, int count) {
    int n = Math.max(0, count);
    List<T> list = new ArrayList<>(n);
    for (int i = 0; i < n; i++) list.add(generate(type));
    return list;
  }
}

