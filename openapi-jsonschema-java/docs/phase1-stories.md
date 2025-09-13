# Phase 1 – Developer Stories and Tasks

Source: See implementation detail in `docs/implementation-plan.md` (Phase 1) and related standards in `docs/coding-standards-and-instructions.md`.

## Sprint 1: Project Setup & Core Architecture
- [x] Create Maven project structure and toolchain (Java 17, Maven).
- [x] Add quality gates: Checkstyle, SpotBugs, PMD, JaCoCo (80%).
- [x] Configure CI pipeline (GitHub Actions: build, test, checks, coverage).
- [x] Define core interfaces: `ValidationEngine`, `SchemaGenerator`, `JsonComparator`.
- [x] Implement base exception hierarchy: `JsonUtilityException`, `ValidationException`, `SchemaGenerationException`.
- [x] Author minimal dev docs (`AGENTS.md`, module `README.md`).
- [x] Provide optional Spring Boot auto-configuration skeleton (`JsonUtilityAutoConfiguration`).
- [ ] Establish branch protection rules (admin action outside repo code).

## Sprint 2: OpenAPI Processing Foundation
- [x] Integrate OpenAPI parser (swagger-parser) and basic configuration.
- [x] Implement schema extraction from components with $ref resolution.
- [x] Add caching layer (Caffeine) abstraction and stub.
- [x] Unit tests (≥80%) for parser and extraction, with sample specs.
- [x] Configurable multi-spec loading via `cosmos.openapi.specs[*]`.

## Acceptance for Phase 1
- [x] Project builds and tests pass locally via `mvn clean verify`.
- [x] CI pipeline validates quality gates on PRs.
- [x] Core APIs and base exceptions available for consumers.
- [x] Spring Boot apps can auto-wire a default `ValidationEngine`.

Notes
- Branch protection cannot be automated here; set via GitHub settings.
- Future tasks in Phase 2+ are detailed in `docs/implementation-plan.md`.
