# Sprint 4 Plan – Hardening, Coverage, and Performance (Phase 2)

Status: 🚧 IN PROGRESS (Weeks 7–8)

## Objectives
- Raise test coverage to ≥80% (JaCoCo gate)
- Add integration tests for complex OpenAPI/JSON Schema scenarios
- Establish performance benchmarks and profiling on large specs
- Introduce caching for repeated conversions to improve throughput

## Deliverables
- [ ] Coverage ≥80% with focused tests on error/edge paths
- [ ] Integration test suite covering nested refs, combinators, formats
- [ ] Benchmark suite and docs with baseline numbers and targets
- [ ] Optional caching layer (memoization) for converter operations
- [ ] Documentation updates (README status, TODO, usage examples)

## Work Breakdown
- [ ] Identify low-coverage packages and write targeted tests
- [ ] Create integration fixtures (large OpenAPI, deeply nested POJOs)
- [ ] Add JMH or simple micro-bench harness for critical paths
- [ ] Implement cache abstraction and pluggable provider (e.g., Caffeine)
- [ ] Update CI: ensure coverage gate enforced and reports archived

## Tracking
- Backlog captured in `docs/todo.md`
- Related packages:
  - `com.cleveloper.utilities.jsonschema.openapi.convert`
  - `com.cleveloper.utilities.jsonschema.generation`
  - `com.cleveloper.utilities.jsonschema.validation`
  - `com.cleveloper.utilities.jsonschema.comparison`

## Risks & Mitigations
- Coverage targeting brittle tests → Focus on behavior, use golden fixtures
- Performance variability on CI → Use local baselines; document environment
- Caching correctness → Validate cache keys/invalidations with tests

