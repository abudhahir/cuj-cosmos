# SonarQube Integration for COSMOS

This document explains how to integrate SonarQube static code analysis with the COSMOS project for enhanced security vulnerability scanning and code quality assessment.

## Overview

SonarQube provides comprehensive static code analysis including:
- **Security Vulnerability Detection**: OWASP Top 10, CWE, SANS Top 25
- **Code Quality Metrics**: Technical debt, complexity, maintainability
- **Security Hotspots**: Potential security-sensitive code review
- **Coverage Analysis**: Integration with JaCoCo test coverage
- **Quality Gates**: Automated pass/fail criteria for builds

## Prerequisites

### SonarQube Server Setup

1. **Local Development**:
   ```bash
   # Using Docker
   docker run -d --name sonarqube -p 9000:9000 sonarqube:community

   # Using Docker Compose
   docker-compose up -d sonarqube
   ```

2. **Enterprise/CI Environment**:
   - SonarQube server running on accessible URL
   - Authentication token configured
   - Project configured in SonarQube

### Maven Configuration

The project includes pre-configured SonarQube integration:
- **Plugin**: `sonar-maven-plugin` v3.10.0.2594
- **Profile**: Optional `sonar` profile for enhanced analysis
- **Properties**: Configured in `pom.xml` and `sonar-project.properties`

## Configuration

### Environment Variables

Set the following environment variables for seamless integration:

```bash
# SonarQube server configuration
export SONAR_HOST_URL="http://localhost:9000"
export SONAR_TOKEN="your-sonarqube-token"

# Optional: Project-specific settings
export SONAR_PROJECT_KEY="cleveloper-utilities-cosmos"
export SONAR_PROJECT_NAME="COSMOS - OpenAPI JSONSchema Utilities"

# CI/CD Integration
export BRANCH_NAME="feature/my-branch"
export PULL_REQUEST_KEY="123"
export PULL_REQUEST_BRANCH="feature/my-branch"
export PULL_REQUEST_BASE="main"
```

### Authentication

#### Token-based Authentication (Recommended)
1. Generate a token in SonarQube: User > My Account > Security > Generate Token
2. Use token in Maven commands:
   ```bash
   mvn sonar:sonar -Dsonar.login=${SONAR_TOKEN}
   ```

#### Username/Password Authentication
```bash
mvn sonar:sonar -Dsonar.login=username -Dsonar.password=password
```

## Usage

### Basic Analysis

```bash
# Standard analysis with default configuration
mvn clean compile test-compile sonar:sonar

# With custom SonarQube server
mvn sonar:sonar -Dsonar.host.url=https://sonarqube.company.com

# With authentication
mvn sonar:sonar -Dsonar.host.url=http://localhost:9000 -Dsonar.login=${SONAR_TOKEN}
```

### Enhanced Security Analysis

```bash
# Full build with SonarQube analysis
mvn clean verify sonar:sonar

# Using security-focused profile
mvn clean verify sonar:sonar -Psonar

# With quality gate enforcement
mvn clean verify sonar:sonar -Dsonar.qualitygate.wait=true
```

### Branch and Pull Request Analysis

```bash
# Branch analysis
mvn sonar:sonar -Dsonar.branch.name=feature/security-improvements

# Pull request analysis
mvn sonar:sonar \
  -Dsonar.pullrequest.key=123 \
  -Dsonar.pullrequest.branch=feature/security-improvements \
  -Dsonar.pullrequest.base=main
```

## Security-Focused Configuration

### Security Rules Enabled

The project is configured with enhanced security analysis:

- **Security Hotspots**: Automatic detection of security-sensitive code
- **Vulnerability Rules**: OWASP, CWE, SANS security standards
- **Security Rating**: A-grade target for security review
- **Security Review**: 100% hotspot review requirement

### Quality Gates

Default quality gate criteria:
- **Security Rating**: A (no vulnerabilities)
- **Security Hotspots Reviewed**: 100%
- **Coverage**: ≥80% (configurable)
- **Duplicated Lines**: <3%
- **Maintainability Rating**: A

### Integration with Other Tools

SonarQube automatically imports reports from:
- **JaCoCo**: Test coverage data
- **Checkstyle**: Code style violations
- **SpotBugs**: Bug detection results
- **PMD**: Code quality issues
- **OWASP Dependency Check**: Vulnerability reports (via custom configuration)

## CI/CD Integration

### GitHub Actions Example

```yaml
- name: SonarQube Analysis
  run: |
    mvn clean verify sonar:sonar \
      -Dsonar.host.url=${{ secrets.SONAR_HOST_URL }} \
      -Dsonar.login=${{ secrets.SONAR_TOKEN }} \
      -Dsonar.pullrequest.key=${{ github.event.number }} \
      -Dsonar.pullrequest.branch=${{ github.head_ref }} \
      -Dsonar.pullrequest.base=${{ github.base_ref }}
```

### Jenkins Pipeline Example

```groovy
stage('SonarQube Analysis') {
    environment {
        SONAR_TOKEN = credentials('sonar-token')
    }
    steps {
        sh '''
            mvn clean verify sonar:sonar \
              -Dsonar.host.url=${SONAR_HOST_URL} \
              -Dsonar.login=${SONAR_TOKEN} \
              -Dsonar.branch.name=${BRANCH_NAME}
        '''
    }
}
```

## Troubleshooting

### Common Issues

1. **Connection Refused**:
   - Verify SonarQube server is running
   - Check firewall and network connectivity
   - Validate `sonar.host.url` property

2. **Authentication Failed**:
   - Verify token is valid and not expired
   - Check user permissions in SonarQube
   - Ensure token has `Execute Analysis` permission

3. **Quality Gate Failed**:
   - Review SonarQube dashboard for specific failures
   - Check security hotspots and vulnerabilities
   - Verify coverage thresholds are met

4. **Missing Reports**:
   - Ensure all quality tools run before SonarQube analysis
   - Check file paths in sonar properties
   - Verify XML report generation is enabled

### Debug Mode

Enable debug logging for troubleshooting:

```bash
mvn sonar:sonar -Dsonar.verbose=true -X
```

## Best Practices

### Security Analysis
1. **Run Full Analysis**: Always include `mvn clean verify` before SonarQube
2. **Review Hotspots**: Address all security hotspots before merging
3. **Regular Scans**: Integrate with CI/CD for every commit/PR
4. **Quality Gates**: Enforce quality gates to prevent regression

### Performance
1. **Incremental Analysis**: Use branch/PR analysis for faster feedback
2. **Exclude Test Files**: Configure exclusions for test and generated code
3. **Parallel Execution**: Use Maven parallel builds where possible

### Maintenance
1. **Token Rotation**: Regularly rotate authentication tokens
2. **Rule Updates**: Keep SonarQube and rules updated
3. **Profile Customization**: Customize quality profiles for project needs
4. **Report Archival**: Archive historical analysis reports

## Resources

- [SonarQube Documentation](https://docs.sonarqube.org/)
- [SonarQube Security Rules](https://rules.sonarsource.com/java/type/Security%20Hotspot)
- [Maven SonarQube Plugin](https://docs.sonarqube.org/latest/analysis/scan/sonarscanner-for-maven/)
- [Quality Gates](https://docs.sonarqube.org/latest/user-guide/quality-gates/)
- [OWASP SonarQube Integration](https://owasp.org/www-project-devsecops-guideline/latest/02c-Static-Analysis-Security-Testing)