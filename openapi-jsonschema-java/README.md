# COSMOS (Comprehensive OpenAPI Schema Management Operations Suite)

A comprehensive Java utility library for OpenAPI and JSON Schema processing, designed for seamless integration with the Spring Boot ecosystem.

## Project Status

### Phase 1 - Sprint 1 ✅ Complete (Weeks 1-2)
**Foundation and Core Infrastructure**

#### Completed Deliverables:
- ✅ **Multi-module Maven project structure** - Java 17+ with comprehensive build configuration
- ✅ **Core API interfaces** - `ValidationEngine`, `SchemaGenerator`, `JsonComparator` interfaces
- ✅ **Exception hierarchy** - Complete error handling structure with `JsonUtilityException` base
- ✅ **CI/CD pipeline** - GitHub Actions with quality gates (Checkstyle, SpotBugs, PMD, JaCoCo)
- ✅ **Development environment setup** - Full documentation in `docs/` directory
- ✅ **Spring Boot auto-configuration skeleton** - `JsonUtilityAutoConfiguration` with conditional beans
- ✅ **Quality gates configured**:
  - Code coverage: 80% minimum (enforced via JaCoCo)
  - Static analysis: SpotBugs, PMD, Checkstyle configured
  - Code formatting: Google Java Style with fmt-maven-plugin
  - Security scanning: OWASP dependency check integrated

### Phase 1 - Sprint 2 ✅ Complete (Weeks 3-4)
**OpenAPI Processing Foundation**

#### Completed Deliverables:
- ✅ OpenAPI 3.0+ parser integration (Swagger Parser 2.1.33)
- ✅ Schema extraction from OpenAPI components
- ✅ $ref dereferencing support for component schemas
- ✅ Caching abstraction with Caffeine stub implementation
- ✅ Config-driven multi-spec loading via `cosmos.openapi.specs[*]`
- ✅ Unit tests with sample OpenAPI specifications (including external refs)
- ✅ Configuration property: `cosmos.schema.inline-refs` to control inlining vs `$ref`

### Phase 2 - Sprint 3 ✅ Complete (Weeks 5-6)
**JSON Schema Generation**

#### Delivered:
- OpenAPI schema → JSON Schema Draft 2020-12 converter (nullable, enums, allOf/oneOf/anyOf, $defs)
- Java POJO → JSON Schema generation (VicTools)
- Validation pipeline wiring (NetworkNT) with clear error collection
- Golden-schema tests for POJOs and OpenAPI components
- Documentation for mapping rules and limits (see `docs/mapping-rules.md`)

### Phase 2 - Sprint 4 🚧 In Progress (Weeks 7-8)
**Hardening, Coverage, and Performance**

#### Goals:
- Raise code coverage to ≥80% (enforce JaCoCo gate)
- Add integration tests for complex OpenAPI/JSON Schema scenarios
- Establish performance benchmarks and profiling for large specs
- Introduce caching for repeated conversions (pluggable provider)

See `docs/sprint-4-plan.md` and `docs/todo.md` for tracking.

## Quick Start

### Prerequisites
- Java 17 or higher
- Maven 3.9+

### Build & Test
```bash
# Full build with all quality checks
mvn clean verify

# Quick build (skip tests)
mvn clean install -DskipTests

# Run tests only
mvn test
```

### Code Quality
```bash
# Format code
mvn fmt:format

# Run all quality checks
mvn checkstyle:check spotbugs:check pmd:check

# Check code coverage
mvn jacoco:check
```

### Vulnerability Coverage

COSMOS includes comprehensive security vulnerability scanning through multiple integrated tools:

#### OWASP Dependency Check (Always Available)
```bash
# Run vulnerability scan only (generates reports in docs/vulnerability-report/)
mvn org.owasp:dependency-check-maven:check

# Full build with vulnerability scanning and timestamped reports
mvn clean verify

# All quality checks including vulnerability scanning
mvn clean verify spotbugs:check pmd:check checkstyle:check

# Security scan with explicit timestamp flag
mvn clean verify -Dvulnerability.report.timestamp=true

# Skip vulnerability scanning locally (e.g., offline or no NVD API key)
mvn clean verify -Ddependency-check.skip=true
```

**Features:**
- **Automatic Timestamped Reports**: `dependency-check-report_YYYYMMDD_HHMMSS.{html,json,xml,csv}`
- **Multiple Formats**: HTML (human-readable), JSON (machine-readable), XML, CSV
- **Build Integration**: Fails build on CVSS ≥ 7.0 vulnerabilities
- **Suppression Support**: Configure false positives in `dependency-check-suppressions.xml`
- **Historical Tracking**: Timestamped copies preserved for trend analysis

#### SonarQube Integration (Optional - When Available)
```bash
# Basic SonarQube analysis (requires SonarQube server)
mvn sonar:sonar -Dsonar.host.url=http://localhost:9000

# SonarQube with authentication token
mvn sonar:sonar -Dsonar.host.url=http://localhost:9000 -Dsonar.login=${SONAR_TOKEN}

# Enhanced security analysis with custom profile
mvn clean verify sonar:sonar -Psonar

# Pull request analysis for CI/CD
mvn sonar:sonar -Dsonar.pullrequest.key=${PR_NUMBER} -Dsonar.pullrequest.branch=${PR_BRANCH} -Dsonar.pullrequest.base=main

# Combined OWASP + SonarQube security pipeline
mvn clean verify sonar:sonar -Dsonar.host.url=http://localhost:9000 -Dsonar.login=${SONAR_TOKEN}
```

**Features:**
- **Security Hotspots**: OWASP Top 10, CWE, SANS Top 25 detection
- **Quality Gates**: Automated security rating enforcement
- **Branch Analysis**: Support for feature branch and pull request analysis
- **CI/CD Ready**: GitHub Actions, Jenkins integration examples
- **Code-Level Analysis**: Static analysis beyond dependency vulnerabilities

#### Local SonarQube Development Setup
```bash
# Start local SonarQube with Docker Compose
docker-compose -f docker-compose.sonarqube.yml up -d

# Access SonarQube dashboard
open http://localhost:9000

# Stop SonarQube environment
docker-compose -f docker-compose.sonarqube.yml down
```

#### Security Reports Location

- **OWASP Reports**: `docs/vulnerability-report/`
  - Current reports: `dependency-check-report.{html,json,xml,csv}`
  - Timestamped copies: `dependency-check-report_YYYYMMDD_HHMMSS.*`
  - Scan summaries: `scan-summary_YYYYMMDD_HHMMSS.txt`

- **SonarQube Reports**: Available in SonarQube dashboard
  - Security hotspots and vulnerability analysis
  - Quality gate status and metrics
  - Historical trend analysis

#### Environment Variables for CI/CD

```bash
# SonarQube Configuration
export SONAR_HOST_URL="https://sonarqube.company.com"
export SONAR_TOKEN="your-sonarqube-token"

# OWASP Dependency-Check (NVD API access)
export NVD_API_KEY="your-nvd-api-key"

# Branch/PR Analysis
export BRANCH_NAME="feature/my-branch"
export PULL_REQUEST_KEY="123"
export PULL_REQUEST_BRANCH="feature/my-branch"
export PULL_REQUEST_BASE="main"
```

#### Documentation

- **OWASP Integration**: `docs/vulnerability-report/README.md`
- **SonarQube Integration**: `docs/sonarqube-integration.md`
- **Suppression Management**: `dependency-check-suppressions.xml`
- **SonarQube Configuration**: `sonar-project.properties`

## Features (Planned)

- **OpenAPI Processing**: Parse and extract schemas from OpenAPI 3.0+ specifications
- **JSON Schema Validation**: Full JSON Schema Draft 2020-12 compliance
- **Test Data Generation**: Generate valid and invalid test data systematically
- **JSON Comparison**: Initial comparator available; returns concise diffs (more options coming)

### Advanced Comparison
- Use `ConfigurableJsonComparator` with `JsonComparisonOptions` to:
  - Ignore paths via JSON Pointers (e.g., `/meta/ts`)
  - Apply numeric tolerance for floating-point comparisons
  - Default `JsonComparator` bean remains lightweight; create configurable instance as needed

## Test Data Generation

- Auto-configured `TestDataGenerator` bean:
  - Uses Instancio when present on the classpath for rich data.
  - Falls back to a simple internal generator otherwise.
- API: `TestDataGenerator.generate(Class<T>)` and `generateMany(Class<T>, int)`.
- Add Instancio for richer data: add dependency `org.instancio:instancio-core`.
- **Spring Boot Integration**: Zero-configuration setup with auto-configuration
- **High Performance**: Sub-100ms validation for typical documents

## Spring Boot Integration

When included in a Spring Boot application, COSMOS auto-configures necessary beans through `JsonUtilityAutoConfiguration`:

```java
@SpringBootApplication
public class YourApplication {
    // ValidationEngine bean auto-configured
    // SchemaGenerator bean auto-configured
    // JsonComparator bean auto-configured
}
```

### Auto-Configured Beans
- `ValidationEngine` (default: `DefaultValidationEngine`)
- `OpenApiParser`, `SpecCache`, `OpenApiSpecRegistry`
- `SchemaGenerator` (VicTools-based POJO → JSON Schema)
- `OpenApiSchemaService` (document-level generator with `$schema`, `$id`, `$defs`)
- `SchemaValidator` (NetworkNT Draft 2020-12)

### Configuration Properties
- `cosmos.openapi.specs[n].id` / `cosmos.openapi.specs[n].location`: register OpenAPI specs
- `cosmos.schema.uri`: override `$schema` URI (default: Draft 2020-12)
- `cosmos.schema.id-prefix`: customize `$id` prefix (default: `urn:cosmos:schema:`)
- `cosmos.schema.inline-refs`: `true` to inline components instead of `$ref: #/$defs/...` (default: `false`)

## Documentation

Comprehensive documentation available in the `docs/` directory:
- [Requirements](docs/requirements.md) - Functional and non-functional requirements
- [Implementation Plan](docs/implementation-plan.md) - 16-week sprint plan with milestones
- [Technology Stack](docs/tech-stack.md) - Technology choices and rationale
- [Coding Standards](docs/coding-standards-and-instructions.md) - Development guidelines
- [TODO](docs/todo.md) - Current task tracking

## Roadmap

- **Phase 1** (Weeks 1-4): Foundation and Core Infrastructure ✅ Sprint 1 | ✅ Sprint 2
- **Phase 2** (Weeks 5-8): JSON Schema Generation and Validation 🚧 Sprint 3
- **Phase 3** (Weeks 9-12): Test Data Generation and Comparison
- **Phase 4** (Weeks 13-16): Advanced Features and Integration

## License

[License information to be added]

## Contributing

[Contributing guidelines to be added]
