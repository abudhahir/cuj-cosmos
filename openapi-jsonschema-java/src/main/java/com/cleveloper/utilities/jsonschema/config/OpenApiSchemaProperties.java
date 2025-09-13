package com.cleveloper.utilities.jsonschema.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cosmos.schema")
public class OpenApiSchemaProperties {
  /** JSON Schema dialect URI (default: Draft 2020-12). */
  private String uri;

  /** Prefix to use for $id values (default: urn:cosmos:schema:). */
  private String idPrefix;

  public String getUri() {
    return uri;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }

  public String getIdPrefix() {
    return idPrefix;
  }

  public void setIdPrefix(String idPrefix) {
    this.idPrefix = idPrefix;
  }
}
