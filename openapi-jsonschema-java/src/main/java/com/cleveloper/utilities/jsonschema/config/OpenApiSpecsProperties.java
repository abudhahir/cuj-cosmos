package com.cleveloper.utilities.jsonschema.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cosmos.openapi")
public class OpenApiSpecsProperties {

  /** Multiple specs can be configured via application.properties/yml. */
  private List<Spec> specs = new ArrayList<>();

  public List<Spec> getSpecs() {
    return specs;
  }

  public void setSpecs(List<Spec> specs) {
    this.specs = specs;
  }

  public static class Spec {
    /** Logical identifier for the spec (used as cache key). */
    private String id;

    /** Location can be classpath:, file path, or URL. */
    private String location;

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public String getLocation() {
      return location;
    }

    public void setLocation(String location) {
      this.location = location;
    }
  }
}
