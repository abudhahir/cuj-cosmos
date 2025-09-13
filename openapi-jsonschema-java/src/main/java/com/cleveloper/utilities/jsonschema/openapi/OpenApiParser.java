package com.cleveloper.utilities.jsonschema.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.ParseOptions;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import java.util.List;

/** Parses OpenAPI documents from raw String content. */
public class OpenApiParser {

  /**
   * Parse an OpenAPI specification provided as a String (YAML or JSON).
   *
   * @param content raw OpenAPI content
   * @return parsed OpenAPI model
   * @throws IllegalArgumentException if parsing fails or content is empty
   */
  public OpenAPI parse(String content) {
    if (content == null || content.trim().isEmpty()) {
      throw new IllegalArgumentException("OpenAPI content must not be empty");
    }
    ParseOptions options = new ParseOptions();
    options.setResolve(true);
    options.setResolveFully(false);
    options.setFlatten(false);

    SwaggerParseResult result = new OpenAPIV3Parser().readContents(content, null, options);
    List<String> messages = result.getMessages();
    if (result.getOpenAPI() == null) {
      String msg =
          (messages == null || messages.isEmpty())
              ? "Unable to parse OpenAPI"
              : String.join("; ", messages);
      throw new IllegalArgumentException(msg);
    }
    return result.getOpenAPI();
  }
}
