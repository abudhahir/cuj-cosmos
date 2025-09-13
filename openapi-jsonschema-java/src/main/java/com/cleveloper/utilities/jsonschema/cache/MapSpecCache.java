package com.cleveloper.utilities.jsonschema.cache;

import io.swagger.v3.oas.models.OpenAPI;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/** Simple in-memory cache using ConcurrentHashMap. */
public class MapSpecCache implements SpecCache {
  private final ConcurrentMap<String, OpenAPI> delegate = new ConcurrentHashMap<>();

  @Override
  public OpenAPI get(String key) {
    return delegate.get(key);
  }

  @Override
  public void put(String key, OpenAPI value) {
    if (key != null && value != null) {
      delegate.put(key, value);
    }
  }
}
