package com.cleveloper.utilities.jsonschema.cache;

import static org.assertj.core.api.Assertions.assertThat;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;

class MapSpecCacheTest {

  @Test
  void putAndGetShouldWork() {
    MapSpecCache cache = new MapSpecCache();
    OpenAPI api = new OpenAPI();
    cache.put("k", api);
    assertThat(cache.get("k")).isSameAs(api);
  }
}
