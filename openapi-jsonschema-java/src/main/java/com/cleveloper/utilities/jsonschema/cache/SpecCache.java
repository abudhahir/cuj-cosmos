package com.cleveloper.utilities.jsonschema.cache;

import io.swagger.v3.oas.models.OpenAPI;

public interface SpecCache {
  OpenAPI get(String key);

  void put(String key, OpenAPI value);
}
