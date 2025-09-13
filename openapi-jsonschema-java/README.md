# COSMOS Utilities (OpenAPI & JSON Schema)

Minimal Java 17 library scaffolding for COSMOS with tests, quality gates, and optional Spring Boot auto-configuration.

## Quick Start
- Build & test: `mvn clean verify`
- Formatting & checks: `mvn fmt:format checkstyle:check spotbugs:check pmd:check`
- Coverage gate: `mvn jacoco:check`

## Spring Boot Auto-Configuration
Include the JAR and Spring Boot will auto-configure a `ValidationEngine` bean via `JsonUtilityAutoConfiguration` when none is present.

Refer to `docs/` for requirements and implementation plan.
