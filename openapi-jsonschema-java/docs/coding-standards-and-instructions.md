# Coding Standards and Development Instructions

## 1. Overview

This document establishes comprehensive coding standards, development practices, and instructions for COSMOS (Comprehensive OpenAPI Schema Management Operations Suite). These standards ensure code quality, maintainability, performance, and team collaboration effectiveness.

## 2. General Development Principles

### 2.1 Core Principles
- **Clean Code**: Write self-documenting, readable code that expresses intent clearly
- **SOLID Principles**: Apply Single Responsibility, Open/Closed, Liskov Substitution, Interface Segregation, and Dependency Inversion
- **DRY (Don't Repeat Yourself)**: Eliminate code duplication through proper abstraction
- **YAGNI (You Aren't Gonna Need It)**: Implement only what's required, avoid over-engineering
- **Fail Fast**: Detect and report errors as early as possible in the execution flow

### 2.2 Design Patterns
**Preferred Patterns**:
- **Builder Pattern**: For complex object construction with optional parameters
- **Factory Pattern**: For object creation with varying implementations
- **Strategy Pattern**: For algorithm selection and validation rule implementation
- **Observer Pattern**: For event-driven architecture and monitoring
- **Command Pattern**: For validation rule execution and test data generation

**Anti-Patterns to Avoid**:
- God Objects: Classes with too many responsibilities
- Anemic Domain Models: Objects without behavior
- Singleton Pattern: Use dependency injection instead
- Deep Inheritance: Prefer composition over inheritance

## 3. Code Formatting and Style

### 3.1 Google Java Style Guide Compliance
**Base Standard**: Follow Google Java Style Guide with specific modifications

**Key Rules**:
- **Indentation**: 4 spaces (not tabs)
- **Line Length**: 120 characters maximum
- **Braces**: K&R style (opening brace on same line)
- **Imports**: Group and order according to Google standards
- **Naming**: CamelCase for classes, camelCase for methods and variables

### 3.2 Checkstyle Configuration
```xml
<!-- checkstyle.xml configuration -->
<module name="Checker">
    <property name="charset" value="UTF-8"/>
    <property name="severity" value="warning"/>
    <property name="fileExtensions" value="java, properties, xml"/>
    
    <!-- Size Violations -->
    <module name="LineLength">
        <property name="max" value="120"/>
        <property name="ignorePattern" value="^package.*|^import.*|a href|href|http://|https://|ftp://"/>
    </module>
    
    <!-- TreeWalker -->
    <module name="TreeWalker">
        <!-- Naming Conventions -->
        <module name="TypeName"/>
        <module name="MethodName"/>
        <module name="VariableName"/>
        <module name="ConstantName"/>
        <module name="PackageName"/>
        
        <!-- Code Structure -->
        <module name="LeftCurly"/>
        <module name="RightCurly"/>
        <module name="WhitespaceAround"/>
        <module name="Indentation">
            <property name="basicOffset" value="4"/>
            <property name="braceAdjustment" value="0"/>
        </module>
    </module>
</module>
```

### 3.3 IDE Configuration

#### IntelliJ IDEA Settings
```xml
<!-- Code Style Settings -->
<code_scheme name="JsonUtilityLibrary" version="173">
    <JavaCodeStyleSettings>
        <option name="ANNOTATION_PARAMETER_WRAP" value="1"/>
        <option name="ALIGN_MULTILINE_ANNOTATION_PARAMETERS" value="true"/>
        <option name="INSERT_INNER_CLASS_IMPORTS" value="true"/>
        <option name="CLASS_COUNT_TO_USE_IMPORT_ON_DEMAND" value="99"/>
        <option name="NAMES_COUNT_TO_USE_IMPORT_ON_DEMAND" value="99"/>
        <option name="PACKAGES_TO_USE_IMPORT_ON_DEMAND">
            <value/>
        </option>
        <option name="IMPORT_LAYOUT_TABLE">
            <value>
                <package name="java" withSubpackages="true" static="false"/>
                <emptyLine/>
                <package name="javax" withSubpackages="true" static="false"/>
                <emptyLine/>
                <package name="org" withSubpackages="true" static="false"/>
                <emptyLine/>
                <package name="com" withSubpackages="true" static="false"/>
                <emptyLine/>
                <package name="" withSubpackages="true" static="false"/>
                <emptyLine/>
                <package name="" withSubpackages="true" static="true"/>
            </value>
        </option>
    </JavaCodeStyleSettings>
</code_scheme>
```

## 4. Naming Conventions

### 4.1 Package Naming
```java
// Base package structure
com.cleveloper.utilities.jsonschema
├── api/                    // Public interfaces and DTOs
├── core/                   // Core implementations and utilities
├── openapi/               // OpenAPI specific functionality
├── validation/            // Validation engine and rules
├── generation/            // Test data generation
├── comparison/            // JSON comparison utilities
├── config/                // Configuration classes
├── exception/             // Exception hierarchy
└── internal/              // Internal implementation details
```

### 4.2 Class Naming Patterns
```java
// Interfaces
public interface ValidationEngine { }
public interface SchemaGenerator { }
public interface JsonComparator { }

// Implementations
public class DefaultValidationEngine implements ValidationEngine { }
public class OpenApiSchemaGenerator implements SchemaGenerator { }
public class JsonUnitComparator implements JsonComparator { }

// Abstract classes
public abstract class AbstractValidator { }
public abstract class BaseSchemaProcessor { }

// Exception classes
public class ValidationException extends JsonUtilityException { }
public class SchemaGenerationException extends JsonUtilityException { }

// Configuration classes
@ConfigurationProperties("json-utility")
public class JsonUtilityProperties { }

// Spring components
@Service
public class ValidationService { }

@Component
public class SchemaCache { }

@Configuration
public class JsonUtilityAutoConfiguration { }
```

### 4.3 Method Naming
```java
// Action verbs for methods
public JsonSchema generateSchema(Class<?> targetClass) { }
public ValidationResult validate(JsonNode data, JsonSchema schema) { }
public ComparisonResult compare(JsonNode expected, JsonNode actual) { }

// Boolean methods
public boolean isValid(JsonNode data) { }
public boolean hasErrors() { }
public boolean canProcess(String mediaType) { }

// Getter/Setter patterns
public String getSchemaId() { }
public void setSchemaId(String schemaId) { }

// Builder pattern methods
public ValidationBuilder field(String path) { }
public ValidationBuilder required() { }
public ValidationBuilder pattern(String regex) { }
public Validator build() { }
```

### 4.4 Variable Naming
```java
// Constants
public static final String DEFAULT_SCHEMA_VERSION = "draft-2020-12";
public static final int MAX_VALIDATION_ERRORS = 100;
public static final Duration CACHE_EXPIRY = Duration.ofHours(24);

// Local variables
JsonNode jsonData = parseJson(input);
ValidationResult validationResult = engine.validate(data, schema);
List<String> errorMessages = result.getErrors();

// Parameters
public ValidationResult validate(JsonNode inputData, JsonSchema targetSchema) { }
```

## 5. Code Documentation Standards

### 5.1 JavaDoc Requirements
**All public APIs must include comprehensive JavaDoc**:

```java
/**
 * Validates JSON data against a JSON Schema and returns detailed validation results.
 * 
 * <p>This method performs comprehensive validation including:
 * <ul>
 *   <li>Type validation according to JSON Schema specification</li>
 *   <li>Constraint validation (min/max, pattern, etc.)</li>
 *   <li>Custom validation rules if configured</li>
 * </ul>
 * 
 * <p>Example usage:
 * <pre>{@code
 * JsonSchema schema = schemaGenerator.generateSchema(User.class);
 * JsonNode userData = mapper.readTree(jsonString);
 * ValidationResult result = validator.validate(userData, schema);
 * 
 * if (!result.isValid()) {
 *     result.getErrors().forEach(System.out::println);
 * }
 * }</pre>
 * 
 * @param data the JSON data to validate, must not be {@code null}
 * @param schema the JSON schema to validate against, must not be {@code null}
 * @return validation result containing success status and any errors found
 * @throws IllegalArgumentException if data or schema is null
 * @throws ValidationException if validation cannot be performed due to schema errors
 * @since 1.0.0
 * @see ValidationResult
 * @see JsonSchema
 */
public ValidationResult validate(JsonNode data, JsonSchema schema) {
    // Implementation
}
```

### 5.2 Inline Comments
```java
// Use inline comments for complex business logic
public List<JsonNode> generateInvalidData(JsonSchema schema, ViolationConfig config) {
    List<JsonNode> invalidData = new ArrayList<>();
    
    // Generate systematic violations for each constraint type
    // This ensures comprehensive negative test coverage
    if (schema.hasNumericConstraints()) {
        // Violate minimum/maximum constraints
        invalidData.addAll(generateNumericViolations(schema));
    }
    
    if (schema.hasStringConstraints()) {
        // Violate pattern, length, and format constraints
        invalidData.addAll(generateStringViolations(schema));
    }
    
    return invalidData;
}
```

### 5.3 TODO and FIXME Standards
```java
// TODO: Add support for additional JSON Schema draft versions (Issue #123)
// FIXME: Memory leak in schema caching mechanism (Priority: High)
// HACK: Temporary workaround for Jackson deserialization issue
```

## 6. Error Handling and Exception Design

### 6.1 Exception Hierarchy
```java
/**
 * Base exception for all JSON utility operations.
 */
public abstract class JsonUtilityException extends RuntimeException {
    private final String errorCode;
    private final Map<String, Object> context;
    
    protected JsonUtilityException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.context = new HashMap<>();
    }
    
    protected JsonUtilityException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.context = new HashMap<>();
    }
    
    public String getErrorCode() { return errorCode; }
    public Map<String, Object> getContext() { return Collections.unmodifiableMap(context); }
    
    protected void addContext(String key, Object value) {
        this.context.put(key, value);
    }
}

/**
 * Exception thrown when JSON validation fails.
 */
public class ValidationException extends JsonUtilityException {
    private final List<ValidationError> validationErrors;
    
    public ValidationException(String message, List<ValidationError> errors) {
        super(message, "VAL_001");
        this.validationErrors = List.copyOf(errors);
        addContext("errorCount", errors.size());
    }
    
    public List<ValidationError> getValidationErrors() {
        return validationErrors;
    }
}

/**
 * Exception thrown when schema generation fails.
 */
public class SchemaGenerationException extends JsonUtilityException {
    public SchemaGenerationException(String message, Class<?> targetClass) {
        super(message, "GEN_001");
        addContext("targetClass", targetClass.getName());
    }
    
    public SchemaGenerationException(String message, Class<?> targetClass, Throwable cause) {
        super(message, "GEN_001", cause);
        addContext("targetClass", targetClass.getName());
    }
}
```

### 6.2 Error Handling Patterns
```java
// Use specific exceptions for different error conditions
public JsonSchema loadSchema(String schemaId) {
    Objects.requireNonNull(schemaId, "Schema ID must not be null");
    
    try {
        return doLoadSchema(schemaId);
    } catch (IOException e) {
        throw new SchemaLoadException("Failed to load schema: " + schemaId, e);
    } catch (JsonProcessingException e) {
        throw new SchemaParseException("Invalid schema format: " + schemaId, e);
    }
}

// Validate input parameters early
public ValidationResult validate(JsonNode data, JsonSchema schema) {
    Objects.requireNonNull(data, "JSON data must not be null");
    Objects.requireNonNull(schema, "JSON schema must not be null");
    
    if (data.isMissingNode()) {
        throw new IllegalArgumentException("JSON data cannot be missing node");
    }
    
    return performValidation(data, schema);
}
```

## 7. Testing Standards

### 7.1 Test Structure and Organization
```java
// Test class naming: [ClassUnderTest]Test
public class ValidationEngineTest {
    
    // Test method naming: should[ExpectedBehavior]When[StateUnderTest]
    @Test
    void shouldReturnValidResultWhenJsonMatchesSchema() { }
    
    @Test
    void shouldThrowExceptionWhenSchemaIsNull() { }
    
    @Test
    void shouldReturnErrorsWhenJsonViolatesConstraints() { }
}

// Integration test naming: [Feature]IntegrationTest
public class SchemaValidationIntegrationTest {
    
    @SpringBootTest
    @Testcontainers
    class RedisIntegrationTest {
        @Container
        static RedisContainer redis = new RedisContainer(DockerImageName.parse("redis:7-alpine"));
        
        @Test
        void shouldCacheSchemaInRedis() { }
    }
}
```

### 7.2 Test Data Management
```java
// Use test data builders for complex objects
public class JsonSchemaTestDataBuilder {
    private String schemaId = "test-schema";
    private String version = "1.0.0";
    private JsonNode content = JsonNodeFactory.instance.objectNode();
    
    public static JsonSchemaTestDataBuilder aJsonSchema() {
        return new JsonSchemaTestDataBuilder();
    }
    
    public JsonSchemaTestDataBuilder withId(String schemaId) {
        this.schemaId = schemaId;
        return this;
    }
    
    public JsonSchemaTestDataBuilder withVersion(String version) {
        this.version = version;
        return this;
    }
    
    public JsonSchema build() {
        return new JsonSchema(schemaId, version, content);
    }
}

// Usage in tests
@Test
void shouldValidateSuccessfullyWithValidData() {
    // Given
    JsonSchema schema = aJsonSchema()
        .withId("user-schema")
        .withVersion("1.0.0")
        .build();
    
    JsonNode validData = createValidUserData();
    
    // When
    ValidationResult result = validator.validate(validData, schema);
    
    // Then
    assertThat(result.isValid()).isTrue();
    assertThat(result.getErrors()).isEmpty();
}
```

### 7.3 Assertion Standards
```java
// Use AssertJ for fluent assertions
@Test
void shouldReturnDetailedErrorsWhenValidationFails() {
    // Given
    JsonNode invalidData = createInvalidData();
    JsonSchema schema = loadTestSchema();
    
    // When
    ValidationResult result = validator.validate(invalidData, schema);
    
    // Then
    assertThat(result.isValid()).isFalse();
    assertThat(result.getErrors())
        .hasSize(2)
        .extracting(ValidationError::getField)
        .containsExactlyInAnyOrder("email", "age");
    
    assertThat(result.getErrors())
        .filteredOn(error -> "email".equals(error.getField()))
        .extracting(ValidationError::getMessage)
        .containsExactly("Invalid email format");
}

// Test exception scenarios
@Test
void shouldThrowValidationExceptionWhenSchemaIsInvalid() {
    // Given
    JsonNode invalidSchema = createMalformedSchema();
    JsonNode data = createTestData();
    
    // When & Then
    assertThatThrownBy(() -> validator.validate(data, invalidSchema))
        .isInstanceOf(ValidationException.class)
        .hasMessage("Schema validation failed")
        .satisfies(ex -> {
            ValidationException validationEx = (ValidationException) ex;
            assertThat(validationEx.getErrorCode()).isEqualTo("VAL_001");
            assertThat(validationEx.getContext()).containsKey("schemaId");
        });
}
```

### 7.4 Performance Testing
```java
// Use JMH for micro-benchmarks
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class ValidationPerformanceBenchmark {
    
    private ValidationEngine validator;
    private JsonNode testData;
    private JsonSchema testSchema;
    
    @Setup
    public void setup() {
        validator = new DefaultValidationEngine();
        testData = loadLargeTestData();
        testSchema = loadComplexSchema();
    }
    
    @Benchmark
    public ValidationResult validateLargeDocument() {
        return validator.validate(testData, testSchema);
    }
}

// Integration performance tests
@Test
@Timeout(value = 5, unit = TimeUnit.SECONDS)
void shouldValidateLargeDocumentWithinTimeLimit() {
    // Given
    JsonNode largeDocument = generateLargeTestDocument(10_000); // 10K elements
    JsonSchema schema = loadComplexSchema();
    
    // When
    ValidationResult result = validator.validate(largeDocument, schema);
    
    // Then
    assertThat(result).isNotNull();
    // Test completes within timeout
}
```

## 8. Logging Standards

### 8.1 Logging Levels and Usage
```java
// Use SLF4J with Logback
private static final Logger log = LoggerFactory.getLogger(ValidationService.class);

public ValidationResult validate(JsonNode data, JsonSchema schema) {
    log.debug("Starting validation for schema: {}", schema.getId());
    
    try {
        ValidationResult result = performValidation(data, schema);
        
        if (result.isValid()) {
            log.debug("Validation successful for schema: {}", schema.getId());
        } else {
            log.info("Validation failed for schema: {} with {} errors", 
                    schema.getId(), result.getErrorCount());
        }
        
        return result;
    } catch (Exception e) {
        log.error("Validation failed unexpectedly for schema: {}", schema.getId(), e);
        throw e;
    }
}

// Structured logging with MDC
public void processOpenApiSpec(String specLocation) {
    MDC.put("specLocation", specLocation);
    MDC.put("operation", "openapi-processing");
    
    try {
        log.info("Processing OpenAPI specification");
        // ... processing logic
        log.info("OpenAPI specification processed successfully");
    } finally {
        MDC.clear();
    }
}
```

### 8.2 Log Message Patterns
```java
// Consistent log message patterns
log.info("Operation [{}] completed successfully in {}ms", operationName, duration);
log.warn("Performance degradation detected: operation [{}] took {}ms (threshold: {}ms)", 
         operationName, actualDuration, threshold);
log.error("Operation [{}] failed: {}", operationName, errorMessage, exception);

// Include relevant context
log.debug("Schema loaded from cache: id={}, version={}, cacheHit={}", 
          schema.getId(), schema.getVersion(), cacheHit);
```

## 9. Configuration Management

### 9.1 Configuration Properties
```java
@ConfigurationProperties(prefix = "json-utility")
@Data
@Validated
public class JsonUtilityProperties {
    
    /**
     * Enable schema caching for improved performance.
     */
    private boolean cacheEnabled = true;
    
    /**
     * Schema storage configuration.
     */
    private Storage storage = new Storage();
    
    /**
     * Validation configuration settings.
     */
    private Validation validation = new Validation();
    
    @Data
    public static class Storage {
        /**
         * Base location for schema storage.
         */
        @NotBlank
        private String location = "classpath:schemas/";
        
        /**
         * Enable schema versioning.
         */
        private boolean versioningEnabled = true;
    }
    
    @Data
    public static class Validation {
        /**
         * Maximum number of validation errors to collect.
         */
        @Min(1)
        @Max(1000)
        private int maxErrors = 100;
        
        /**
         * Enable fail-fast validation mode.
         */
        private boolean failFast = false;
    }
}
```

### 9.2 Configuration Validation
```java
@Component
@Validated
public class JsonUtilityConfigurationValidator {
    
    @EventListener
    public void validateConfiguration(ApplicationReadyEvent event) {
        JsonUtilityProperties properties = event.getApplicationContext()
            .getBean(JsonUtilityProperties.class);
            
        validateStorageConfiguration(properties.getStorage());
        validateValidationConfiguration(properties.getValidation());
    }
    
    private void validateStorageConfiguration(Storage storage) {
        if (storage.getLocation().startsWith("file:")) {
            Path path = Paths.get(storage.getLocation().substring(5));
            if (!Files.exists(path)) {
                throw new ConfigurationException("Storage location does not exist: " + path);
            }
        }
    }
}
```

## 10. Security Guidelines

### 10.1 Input Validation
```java
// Validate all external inputs
public JsonSchema parseSchema(String schemaContent) {
    // Validate input size to prevent DoS attacks
    if (schemaContent.length() > MAX_SCHEMA_SIZE) {
        throw new IllegalArgumentException("Schema size exceeds maximum allowed: " + MAX_SCHEMA_SIZE);
    }
    
    // Sanitize input to prevent injection attacks
    String sanitizedContent = HtmlUtils.htmlEscape(schemaContent);
    
    try {
        return doParseSchema(sanitizedContent);
    } catch (JsonProcessingException e) {
        // Don't expose internal details in error messages
        throw new SchemaParseException("Invalid schema format provided");
    }
}

// Use parameterized queries for database operations
public void saveSchema(JsonSchema schema) {
    String sql = "INSERT INTO schemas (id, version, content) VALUES (?, ?, ?)";
    jdbcTemplate.update(sql, schema.getId(), schema.getVersion(), schema.getContent());
}
```

### 10.2 Sensitive Data Handling
```java
// Avoid logging sensitive information
public ValidationResult validateUserData(JsonNode userData) {
    log.debug("Validating user data with {} fields", userData.size());
    // Don't log the actual data content
    
    ValidationResult result = performValidation(userData);
    
    if (!result.isValid()) {
        // Log error count but not error details that might contain sensitive data
        log.warn("User data validation failed with {} errors", result.getErrorCount());
    }
    
    return result;
}
```

## 11. Performance Guidelines

### 11.1 Object Creation and Memory Management
```java
// Reuse expensive objects
public class ValidationService {
    private final ObjectMapper objectMapper; // Reuse Jackson ObjectMapper
    private final JsonSchemaFactory schemaFactory; // Reuse schema factory
    
    // Use object pools for frequently created objects
    private final ObjectPool<ValidationContext> contextPool;
    
    public ValidationResult validate(JsonNode data, JsonSchema schema) {
        ValidationContext context = contextPool.borrowObject();
        try {
            return performValidation(data, schema, context);
        } finally {
            contextPool.returnObject(context);
        }
    }
}

// Prefer StringBuilder for string concatenation in loops
public String buildErrorMessage(List<ValidationError> errors) {
    StringBuilder message = new StringBuilder("Validation failed with errors: ");
    for (ValidationError error : errors) {
        message.append(error.getField()).append(" - ").append(error.getMessage()).append("; ");
    }
    return message.toString();
}
```

### 11.2 Caching Strategies
```java
// Use appropriate cache eviction policies
@Service
public class SchemaService {
    
    @Cacheable(value = "schemas", key = "#schemaId + ':' + #version")
    public JsonSchema loadSchema(String schemaId, String version) {
        return doLoadSchema(schemaId, version);
    }
    
    @CacheEvict(value = "schemas", key = "#schema.id + ':' + #schema.version")
    public void updateSchema(JsonSchema schema) {
        doUpdateSchema(schema);
    }
    
    // Implement cache warming for frequently used schemas
    @EventListener
    public void warmCache(ApplicationReadyEvent event) {
        List<String> frequentlyUsedSchemas = getFrequentlyUsedSchemas();
        frequentlyUsedSchemas.forEach(this::preloadSchema);
    }
}
```

## 12. Concurrency and Thread Safety

### 12.1 Thread-Safe Design
```java
// Make services thread-safe
@Service
public class ValidationService {
    // Use thread-safe collections
    private final ConcurrentMap<String, JsonSchema> schemaCache = new ConcurrentHashMap<>();
    
    // Use immutable objects where possible
    public ValidationResult validate(JsonNode data, JsonSchema schema) {
        // ValidationResult should be immutable
        return ValidationResult.builder()
            .valid(isValid)
            .errors(List.copyOf(errors)) // Immutable copy
            .timestamp(Instant.now())
            .build();
    }
}

// Use concurrent utilities appropriately
@Component
public class AsyncValidationService {
    private final ExecutorService executorService = 
        ForkJoinPool.commonPool(); // For CPU-intensive tasks
    
    public CompletableFuture<ValidationResult> validateAsync(JsonNode data, JsonSchema schema) {
        return CompletableFuture.supplyAsync(() -> {
            return validator.validate(data, schema);
        }, executorService);
    }
}
```

### 12.2 Avoiding Common Concurrency Issues
```java
// Avoid shared mutable state
public class ValidationStatistics {
    private final AtomicLong totalValidations = new AtomicLong(0);
    private final AtomicLong successfulValidations = new AtomicLong(0);
    
    public void recordValidation(boolean successful) {
        totalValidations.incrementAndGet();
        if (successful) {
            successfulValidations.incrementAndGet();
        }
    }
    
    public double getSuccessRate() {
        long total = totalValidations.get();
        return total == 0 ? 0.0 : (double) successfulValidations.get() / total;
    }
}
```

## 13. Build and Deployment Standards

### 13.1 Maven Configuration
```xml
<!-- pom.xml quality and security plugins -->
<build>
    <plugins>
        <!-- Code formatting -->
        <plugin>
            <groupId>com.spotify.fmt</groupId>
            <artifactId>fmt-maven-plugin</artifactId>
            <version>2.21.1</version>
            <executions>
                <execution>
                    <goals>
                        <goal>format</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
        
        <!-- Code quality -->
        <plugin>
            <groupId>com.github.spotbugs</groupId>
            <artifactId>spotbugs-maven-plugin</artifactId>
            <version>4.8.2.0</version>
            <configuration>
                <effort>Max</effort>
                <threshold>Low</threshold>
                <failOnError>true</failOnError>
            </configuration>
        </plugin>
        
        <!-- Security scanning -->
        <plugin>
            <groupId>org.owasp</groupId>
            <artifactId>dependency-check-maven</artifactId>
            <version>9.0.7</version>
            <configuration>
                <failBuildOnCVSS>7</failBuildOnCVSS>
            </configuration>
        </plugin>
        
        <!-- Test coverage -->
        <plugin>
            <groupId>org.jacoco</groupId>
            <artifactId>jacoco-maven-plugin</artifactId>
            <version>0.8.11</version>
            <configuration>
                <rules>
                    <rule>
                        <element>BUNDLE</element>
                        <limits>
                            <limit>
                                <counter>INSTRUCTION</counter>
                                <value>COVEREDRATIO</value>
                                <minimum>0.80</minimum>
                            </limit>
                        </limits>
                    </rule>
                </rules>
            </configuration>
        </plugin>
    </plugins>
</build>
```

### 13.2 CI/CD Quality Gates
```yaml
# GitHub Actions example
name: Quality Gate
on: [push, pull_request]

jobs:
  quality-check:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
      
      - name: Cache dependencies
        uses: actions/cache@v3
        with:
          path: ~/.m2
          key: ${{ runner.os }}-m2-${{ hashFiles('**/pom.xml') }}
      
      - name: Run tests
        run: mvn clean test
      
      - name: Check code coverage
        run: mvn jacoco:check
      
      - name: Run security scan
        run: mvn org.owasp:dependency-check-maven:check
      
      - name: Static code analysis
        run: mvn spotbugs:check pmd:check checkstyle:check
```

## 14. Documentation Standards

### 14.1 README Structure
```markdown
# Project Name

## Overview
Brief description and key features

## Quick Start
Minimal example to get started

## Installation
Dependencies and setup instructions

## Configuration
Configuration options and examples

## API Documentation
Link to generated JavaDoc

## Examples
Common usage patterns

## Performance
Benchmarks and optimization tips

## Contributing
Development setup and guidelines

## License
License information
```

### 14.2 Architecture Decision Records (ADRs)
```markdown
# ADR-001: JSON Schema Validation Library Selection

## Status
Accepted

## Context
Need to select a JSON Schema validation library that supports Draft 2020-12

## Decision
Use NetworkNT JSON Schema Validator

## Consequences
- Pros: Best performance, full Draft 2020-12 support
- Cons: Less mature than alternatives
- Migration path: Well-defined API for future changes
```

## 15. Code Review Guidelines

### 15.1 Review Checklist
- [ ] Code follows established naming conventions
- [ ] All public methods have comprehensive JavaDoc
- [ ] Error handling is appropriate and consistent
- [ ] Tests cover both positive and negative scenarios
- [ ] No security vulnerabilities introduced
- [ ] Performance implications considered
- [ ] No code duplication or violations of DRY principle
- [ ] Configuration changes are documented
- [ ] Breaking changes are clearly identified

### 15.2 Review Process
1. **Self-Review**: Author reviews own code before submission
2. **Automated Checks**: CI pipeline validates code quality
3. **Peer Review**: At least one team member reviews changes
4. **Documentation Review**: Ensure documentation is updated
5. **Final Approval**: Senior developer approval for significant changes

This comprehensive coding standard ensures consistency, quality, and maintainability across the entire COSMOS (Comprehensive OpenAPI Schema Management Operations Suite) codebase.
