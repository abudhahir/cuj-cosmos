# JSON Schema Mapping Rules (OpenAPI → Draft 2020-12)

This document describes how COSMOS maps OpenAPI component schemas to JSON Schema Draft 2020-12.

Scope focuses on commonly used keywords and the features covered by tests.

Implementation: `com.cleveloper.utilities.jsonschema.openapi.convert.OpenApiToJsonSchemaConverter`

- Types: `type` copied as-is. When `nullable: true`, `type` becomes an array including `"null"`.
- Format: `format` copied when present.
- Enum: `enum` values preserved using Jackson POJO serialization.
- Objects:
  - `properties`: each property converted recursively.
  - `required`: copied to `required` array.
  - `additionalProperties`: `false` preserved; schema value converted recursively.
  - `minProperties` / `maxProperties`: copied.
- Arrays:
  - `items`: converted recursively.
  - `minItems` / `maxItems` / `uniqueItems`: copied.
- Numbers/Strings:
  - `minimum` / `maximum`: copied unless `exclusive*` flags are true.
  - `exclusiveMinimum` / `exclusiveMaximum`: when `true`, numeric bound emitted under `exclusive*`.
  - `multipleOf`, `minLength`, `maxLength`, `pattern`: copied.
- Combinators: `allOf`, `anyOf`, `oneOf` mapped by recursively converting each element.
- Negation: `not` converted recursively.
- Annotations: `description`, `title`, `default`, `example(s)`, `readOnly`, `writeOnly`, `deprecated` preserved.
- References:
  - By default, component `$ref` maps to `$ref: #/$defs/{Name}`.
  - When configured to inline, the reference target is dereferenced and converted in place.

Document-level assembly: `DefaultOpenApiSchemaService`

- `$schema`: defaults to `https://json-schema.org/draft/2020-12/schema` (configurable via `cosmos.schema.uri`).
- `$id`: emitted as `{idPrefix}{componentName}` (default prefix `urn:cosmos:schema:`; configurable via `cosmos.schema.id-prefix`).
- `$defs`: populated with the closure of component references reachable from the root schema. Only referenced components are included; the root component itself is not duplicated under `$defs`.

Limits & Notes

- Conditional keywords (`if`/`then`/`else`) are not mapped in this phase.
- Advanced OpenAPI-specific semantics (discriminator, content encodings, nullable semantics beyond type-null union) are not covered.
- VicTools-based POJO → JSON Schema generation targets Draft 2020-12 and can be customized by adding optional VicTools modules on the classpath.

