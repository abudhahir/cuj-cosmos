# Sprint 3 Plan – JSON Schema Generation (Phase 2)

Status: In Progress (Weeks 5–6)

## Objectives
- Implement OpenAPI schema → JSON Schema Draft 2020-12 conversion
- Generate JSON Schema from Java POJOs using VicTools
- Wire validation pipeline (NetworkNT) and error reporting
- Establish golden-schema tests and baseline performance checks

## Deliverables
- [ ] OpenAPI → JSON Schema converter covering: nullable, enums, allOf/oneOf/anyOf, $defs
- [ ] VicTools-based `SchemaGenerator.generateFor(Class<?>)` with custom modules
- [ ] Validation: NetworkNT Draft 2020-12 setup with detailed error paths
- [ ] Test suite: golden schemas for POJOs and OpenAPI components
- [ ] Docs: mapping rules, examples, and limits

## Work Breakdown
- [ ] Mapping rules draft and test fixtures for OpenAPI → JSON Schema
- [ ] Implement conversion passes (type, combinators, formats, nullable)
- [ ] `$defs` extraction and `$id` handling strategy
- [ ] VicTools configuration modules (Jackson, Bean Validation)
- [ ] Validator wiring + error message formatter
- [ ] Performance smoke tests on large specs
- [ ] Documentation updates and examples

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

