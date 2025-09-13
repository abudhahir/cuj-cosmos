1. i should be able to define the location of the openapi specs location (multiple) in application properties yaml.

## JSON Schema Generation – Actively Maintained OSS Options

This note tracks popular, actively maintained tools we can leverage or interoperate with for JSON Schema generation across different inputs (POJOs, OpenAPI, samples). These complement our primary choice (VicTools) and inform future enhancements.

- VicTools `jsonschema-generator` (Java): Mature POJO → JSON Schema (Draft 2020-12)
  - Pros: Java-native, highly configurable, module ecosystem (Jackson, Bean Validation), supports 2020-12, stable API.
  - Cons: Not an OpenAPI Schema converter; focused on Java type introspection.
  - Fit: Primary engine for `SchemaGenerator.generateFor(Class<?>)` (already integrated).

- openapi-contrib `openapi-schema-to-json-schema` (JavaScript): OpenAPI 3.x Schema → JSON Schema
  - Pros: Widely used for bridging OpenAPI↔JSON Schema divergence; handles nullable, allOf/oneOf/anyOf, formats.
  - Cons: Node.js dependency; not Java-native. Needs CLI/process bridge or embedding (e.g., GraalVM) for JVM usage.
  - Fit: Viable reference for our OpenAPI→JSON Schema mapping rules and parity tests.

- `openapi-json-schema-tools/openapi-json-schema-generator` (Open source): OpenAPI → JSON Schema utilities
  - Pros: Community-maintained successor to older generators; focuses on JSON Schema artifacts from OpenAPI documents.
  - Cons: Not Java; integration strategy needed (CLI/Node). Feature set varies by language target.
  - Fit: Cross-check conversion behavior and edge cases; potential CLI integration for non-Java users.

- quicktype (CLI): Generate JSON Schema and code from JSON samples
  - Pros: Excellent for bootstrapping schemas from example payloads; active community; many targets.
  - Cons: Sample-driven (may miss constraints); not Java library.
  - Fit: Useful for prototyping/golden tests and producing example schemas from fixtures.

Notes / Guidance
- Our core path remains Java-native: VicTools for POJOs; custom converter for OpenAPI component schemas.
- For OpenAPI conversion, align behavior with `openapi-schema-to-json-schema` where sensible (nullable semantics, combinators, $defs extraction) and add tests to ensure parity.
- If we need a temporary bridge before full Java conversion is complete, consider optional CLI integration for Node-based tools behind a feature flag (off by default).
