package com.cleveloper.utilities.jsonschema.cache;

import static org.assertj.core.api.Assertions.assertThat;

import io.swagger.v3.oas.models.OpenAPI;
import java.time.Duration;
import org.junit.jupiter.api.Test;

class CaffeineSpecCacheTest {

  @Test
  void putAndGetShouldReturnSameInstance() {
    CaffeineSpecCache cache = new CaffeineSpecCache(Duration.ofMinutes(5));
    OpenAPI api = new OpenAPI();
    cache.put("k1", api);
    assertThat(cache.get("k1")).isSameAs(api);
  }
}
