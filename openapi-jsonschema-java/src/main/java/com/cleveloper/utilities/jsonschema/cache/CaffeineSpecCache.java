package com.cleveloper.utilities.jsonschema.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.swagger.v3.oas.models.OpenAPI;
import java.time.Duration;

/** Spec cache backed by Caffeine with TTL. */
public class CaffeineSpecCache implements SpecCache {
  private final Cache<String, OpenAPI> cache;

  public CaffeineSpecCache(Duration ttl) {
    Duration effective = ttl == null ? Duration.ofMinutes(30) : ttl;
    this.cache = Caffeine.newBuilder().expireAfterWrite(effective).maximumSize(512).build();
  }

  public CaffeineSpecCache() {
    this(Duration.ofMinutes(30));
  }

  @Override
  public OpenAPI get(String key) {
    return cache.getIfPresent(key);
  }

  @Override
  public void put(String key, OpenAPI value) {
    if (key != null && value != null) {
      cache.put(key, value);
    }
  }
}
