# Sprint 3 Plan – JSON Schema Generation (Phase 2)

Status: ✅ COMPLETE (Weeks 5–6) - Closed on 2025-09-14

## Objectives
- Implement OpenAPI schema → JSON Schema Draft 2020-12 conversion
- Generate JSON Schema from Java POJOs using VicTools
- Wire validation pipeline (NetworkNT) and error reporting
- Establish golden-schema tests and baseline performance checks

## Deliverables
- [x] OpenAPI → JSON Schema converter covering: nullable, enums, allOf/oneOf/anyOf, $defs
- [x] VicTools-based `SchemaGenerator.generateFor(Class<?>)` (modules loaded reflectively if present)
- [x] Validation: NetworkNT Draft 2020-12 setup with clear error collection
- [x] Test suite: golden schemas for POJOs and OpenAPI components
- [x] Docs: mapping rules, examples, and limits (see `docs/mapping-rules.md`)

## Work Breakdown
- [x] Mapping rules draft and test fixtures for OpenAPI → JSON Schema
- [x] Implement conversion passes (type, combinators, formats, nullable)
- [x] `$defs` extraction and `$id` handling strategy
- [x] VicTools generator with optional modules (loaded reflectively)
- [x] Validator wiring + error collection
- [x] Performance smoke tests on typical specs
- [x] Documentation updates and examples

## Tracking
- See `docs/todo.md` (Sprint 3 Backlog) and `README.md` (status)
- Related packages:
  - `com.cleveloper.utilities.jsonschema.openapi.convert`
  - `com.cleveloper.utilities.jsonschema.generation`
  - `com.cleveloper.utilities.jsonschema.validation`

## Risks & Mitigations
- JSON Schema 2020-12 complexity → incremental converter with parity tests
- Divergence from OpenAPI semantics → document mapping rules + tests
- Performance on large specs → caching and staged conversion
