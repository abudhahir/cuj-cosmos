# Repository Guidelines

## Project Structure & Module Organization
- `openapi-jsonschema-java/docs/`: Specs, standards, and plans for the COSMOS utilities suite.
- Planned code layout (when sources are added):
  - `openapi-jsonschema-java/src/main/java/com/cleveloper/utilities/jsonschema/...`
  - `openapi-jsonschema-java/src/test/java/com/cleveloper/utilities/jsonschema/...`
- Suggested packages: `api/`, `core/`, `openapi/`, `validation/`, `generation/`, `comparison/`, `config/`, `exception/`, `internal/`.

## Build, Test, and Development Commands
- Java 17 + Maven (recommended). Run from `openapi-jsonschema-java/`.
- Verify toolchain: `mvn -q -v` and `java -version`.
- Build & test (once `pom.xml` exists): `mvn clean verify`.
- Unit tests only: `mvn test`.
- Formatting & quality (when configured): `mvn fmt:format checkstyle:check spotbugs:check pmd:check`.
- Coverage gate (when configured): `mvn jacoco:check`.

## Coding Style & Naming Conventions
- Indentation: 4 spaces; max line length: 120; K&R braces; ordered imports.
- Naming: `PascalCase` classes/interfaces, `camelCase` methods/fields, `UPPER_SNAKE_CASE` constants.
- Packages: all lowercase, base `com.cleveloper.utilities.jsonschema`.
- JavaDoc required for all public APIs; document exceptions and examples.

## Testing Guidelines
- Frameworks: JUnit 5 + AssertJ; Testcontainers for integration as needed.
- Class names: `[ClassUnderTest]Test` (e.g., `ValidationEngineTest`).
- Method names: `should[Expected]When[State]`.
- Coverage target: ≥80% (JaCoCo). Run: `mvn test` and `mvn jacoco:check` (when enabled).

## Commit & Pull Request Guidelines
- Use Conventional Commits: `feat:`, `fix:`, `docs:`, `test:`, `refactor:`, `chore:`.
- PRs include: clear description, linked issues, rationale, and updates to `openapi-jsonschema-java/docs/` when behavior changes.
- Ensure local checks pass before opening PR: `mvn clean verify` (once build is set up).

## Security & Configuration Tips
- Do not commit secrets; use environment variables and `.gitignore`d local configs.
- Pin dependency versions; run OWASP Dependency-Check when available: `mvn org.owasp:dependency-check-maven:check`.
- Prefer dependency injection over singletons; validate inputs early and fail fast.

