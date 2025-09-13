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

### Phase 1 - Sprint 2 🚧 In Progress (Weeks 3-4)
**OpenAPI Processing Foundation**

#### Current Progress:
- ✅ OpenAPI 3.0+ parser integration (Swagger Parser 2.1.33)
- ✅ Schema extraction from OpenAPI components
- ✅ Basic $ref dereferencing support
- ✅ Cache interface definition (Caffeine integration started)
- ✅ Unit tests with sample OpenAPI specifications
- 🚧 External $ref resolution (in progress)
- 🚧 Comprehensive caching implementation
- 🚧 Performance optimization for large specifications

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

## Features (Planned)

- **OpenAPI Processing**: Parse and extract schemas from OpenAPI 3.0+ specifications
- **JSON Schema Validation**: Full JSON Schema Draft 2020-12 compliance
- **Test Data Generation**: Generate valid and invalid test data systematically
- **JSON Comparison**: Detailed comparison with configurable rules
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

## Documentation

Comprehensive documentation available in the `docs/` directory:
- [Requirements](docs/requirements.md) - Functional and non-functional requirements
- [Implementation Plan](docs/implementation-plan.md) - 16-week sprint plan with milestones
- [Technology Stack](docs/tech-stack.md) - Technology choices and rationale
- [Coding Standards](docs/coding-standards-and-instructions.md) - Development guidelines
- [TODO](docs/todo.md) - Current task tracking

## Roadmap

- **Phase 1** (Weeks 1-4): Foundation and Core Infrastructure ✅ Sprint 1 | 🚧 Sprint 2
- **Phase 2** (Weeks 5-8): JSON Schema Generation and Validation
- **Phase 3** (Weeks 9-12): Test Data Generation and Comparison
- **Phase 4** (Weeks 13-16): Advanced Features and Integration

## License

[License information to be added]

## Contributing

[Contributing guidelines to be added]
