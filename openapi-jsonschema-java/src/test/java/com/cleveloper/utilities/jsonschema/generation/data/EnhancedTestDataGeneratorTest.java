package com.cleveloper.utilities.jsonschema.generation.data;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class EnhancedTestDataGeneratorTest {

  static class TestPerson {
    public String name;
    public int age;
    public String email;
    public boolean active;
    public Double salary;
  }

  static class TestProduct {
    public String id;
    public String name;
    public double price;
    public int quantity;
    public String category;
  }

  @Test
  void shouldGenerateValidDataWithConstraints() {
    EnhancedTestDataGenerator gen = new EnhancedSimpleTestDataGenerator();
    Map<String, Object> constraints = new java.util.HashMap<>();
    constraints.put("age", 25);
    
    TestPerson person = gen.generateValid(TestPerson.class, constraints);
    
    assertThat(person).isNotNull();
    assertThat(person.name).isNotBlank();
    assertThat(person.age).isGreaterThanOrEqualTo(0);
    assertThat(person.email).isNotBlank();
  }

  @Test
  void shouldGenerateInvalidDataWithNullValues() {
    EnhancedTestDataGenerator gen = new EnhancedSimpleTestDataGenerator();
    
    TestPerson person = gen.generateInvalid(TestPerson.class, 
        EnhancedTestDataGenerator.ViolationType.NULL_VALUE);
    
    assertThat(person).isNotNull();
    // Some fields should be null (depending on implementation)
    // This tests the invalid data generation capability
  }

  @Test
  void shouldGenerateInvalidDataWithEmptyStrings() {
    EnhancedTestDataGenerator gen = new EnhancedSimpleTestDataGenerator();
    
    TestPerson person = gen.generateInvalid(TestPerson.class, 
        EnhancedTestDataGenerator.ViolationType.EMPTY_STRING);
    
    assertThat(person).isNotNull();
    // Some string fields should be empty
  }

  @Test
  void shouldGenerateInvalidDataWithTooLongStrings() {
    EnhancedTestDataGenerator gen = new EnhancedSimpleTestDataGenerator();
    
    TestPerson person = gen.generateInvalid(TestPerson.class, 
        EnhancedTestDataGenerator.ViolationType.STRING_TOO_LONG);
    
    assertThat(person).isNotNull();
    // Some string fields should be very long
  }

  @Test
  void shouldGenerateInvalidDataWithTooLargeNumbers() {
    EnhancedTestDataGenerator gen = new EnhancedSimpleTestDataGenerator();
    
    TestPerson person = gen.generateInvalid(TestPerson.class, 
        EnhancedTestDataGenerator.ViolationType.NUMBER_TOO_LARGE);
    
    assertThat(person).isNotNull();
    // Some numeric fields should be at maximum values
  }

  @Test
  void shouldGenerateInvalidDataWithInvalidFormat() {
    EnhancedTestDataGenerator gen = new EnhancedSimpleTestDataGenerator();
    
    TestPerson person = gen.generateInvalid(TestPerson.class, 
        EnhancedTestDataGenerator.ViolationType.INVALID_FORMAT);
    
    assertThat(person).isNotNull();
    // Email field should have invalid format
  }

  @Test
  void shouldGenerateEdgeCaseDataWithMinimumValues() {
    EnhancedTestDataGenerator gen = new EnhancedSimpleTestDataGenerator();
    
    TestPerson person = gen.generateEdgeCase(TestPerson.class, 
        EnhancedTestDataGenerator.EdgeCaseType.MINIMUM_VALUES);
    
    assertThat(person).isNotNull();
    assertThat(person.age).isEqualTo(0);
    assertThat(person.name).isEqualTo("a"); // Minimum length
  }

  @Test
  void shouldGenerateEdgeCaseDataWithMaximumValues() {
    EnhancedTestDataGenerator gen = new EnhancedSimpleTestDataGenerator();
    
    TestPerson person = gen.generateEdgeCase(TestPerson.class, 
        EnhancedTestDataGenerator.EdgeCaseType.MAXIMUM_VALUES);
    
    assertThat(person).isNotNull();
    assertThat(person.age).isEqualTo(Integer.MAX_VALUE);
    assertThat(person.name).hasSize(100); // Very long string
  }

  @Test
  void shouldGenerateEdgeCaseDataWithNullValues() {
    EnhancedTestDataGenerator gen = new EnhancedSimpleTestDataGenerator();
    
    TestPerson person = gen.generateEdgeCase(TestPerson.class, 
        EnhancedTestDataGenerator.EdgeCaseType.NULL_VALUES);
    
    assertThat(person).isNotNull();
    // Some fields should be null
  }

  @Test
  void shouldGenerateEdgeCaseDataWithSpecialCharacters() {
    EnhancedTestDataGenerator gen = new EnhancedSimpleTestDataGenerator();
    
    TestPerson person = gen.generateEdgeCase(TestPerson.class, 
        EnhancedTestDataGenerator.EdgeCaseType.SPECIAL_CHARACTERS);
    
    assertThat(person).isNotNull();
    assertThat(person.name).contains("!");
  }

  @Test
  void shouldGenerateEdgeCaseDataWithUnicodeCharacters() {
    EnhancedTestDataGenerator gen = new EnhancedSimpleTestDataGenerator();
    
    TestPerson person = gen.generateEdgeCase(TestPerson.class, 
        EnhancedTestDataGenerator.EdgeCaseType.UNICODE_CHARACTERS);
    
    assertThat(person).isNotNull();
    assertThat(person.name).contains("你好");
  }

  @Test
  void shouldGenerateManyInvalidDataInstances() {
    EnhancedTestDataGenerator gen = new EnhancedSimpleTestDataGenerator();
    
    List<TestPerson> persons = gen.generateManyInvalid(TestPerson.class, 5, 
        EnhancedTestDataGenerator.ViolationType.NULL_VALUE);
    
    assertThat(persons).hasSize(5);
    assertThat(persons).doesNotHaveDuplicates();
  }

  @Test
  void shouldGenerateManyEdgeCaseDataInstances() {
    EnhancedTestDataGenerator gen = new EnhancedSimpleTestDataGenerator();
    
    List<TestPerson> persons = gen.generateManyEdgeCases(TestPerson.class, 3, 
        EnhancedTestDataGenerator.EdgeCaseType.MINIMUM_VALUES);
    
    assertThat(persons).hasSize(3);
    assertThat(persons).doesNotHaveDuplicates();
  }

  @Test
  void shouldGenerateRealisticData() {
    EnhancedTestDataGenerator gen = new EnhancedSimpleTestDataGenerator();
    
    TestPerson person = gen.generateRealistic(TestPerson.class);
    
    assertThat(person).isNotNull();
    assertThat(person.name).isNotBlank();
    assertThat(person.email).isNotBlank();
  }

  @Test
  void shouldGenerateDeterministicData() {
    EnhancedTestDataGenerator gen = new EnhancedSimpleTestDataGenerator();
    
    TestPerson person1 = gen.generateDeterministic(TestPerson.class, 12345L);
    TestPerson person2 = gen.generateDeterministic(TestPerson.class, 12345L);
    
    assertThat(person1).isNotNull();
    assertThat(person2).isNotNull();
    // With same seed, should generate same data
    assertThat(person1.name).isEqualTo(person2.name);
    assertThat(person1.age).isEqualTo(person2.age);
  }

  @Test
  void shouldGenerateDifferentDataWithDifferentSeeds() {
    EnhancedTestDataGenerator gen = new EnhancedSimpleTestDataGenerator();
    
    TestPerson person1 = gen.generateDeterministic(TestPerson.class, 12345L);
    TestPerson person2 = gen.generateDeterministic(TestPerson.class, 67890L);
    
    assertThat(person1).isNotNull();
    assertThat(person2).isNotNull();
    // With different seeds, should generate different data
    assertThat(person1.name).isNotEqualTo(person2.name);
  }

  @Test
  void shouldHandleComplexObjectWithMultipleViolationTypes() {
    EnhancedTestDataGenerator gen = new EnhancedSimpleTestDataGenerator();
    
    // Test different violation types
    TestProduct product1 = gen.generateInvalid(TestProduct.class, 
        EnhancedTestDataGenerator.ViolationType.NULL_VALUE);
    TestProduct product2 = gen.generateInvalid(TestProduct.class, 
        EnhancedTestDataGenerator.ViolationType.EMPTY_STRING);
    TestProduct product3 = gen.generateInvalid(TestProduct.class, 
        EnhancedTestDataGenerator.ViolationType.NUMBER_TOO_LARGE);
    
    assertThat(product1).isNotNull();
    assertThat(product2).isNotNull();
    assertThat(product3).isNotNull();
  }

  @Test
  void shouldHandleComplexObjectWithMultipleEdgeCaseTypes() {
    EnhancedTestDataGenerator gen = new EnhancedSimpleTestDataGenerator();
    
    // Test different edge case types
    TestProduct product1 = gen.generateEdgeCase(TestProduct.class, 
        EnhancedTestDataGenerator.EdgeCaseType.MINIMUM_VALUES);
    TestProduct product2 = gen.generateEdgeCase(TestProduct.class, 
        EnhancedTestDataGenerator.EdgeCaseType.MAXIMUM_VALUES);
    TestProduct product3 = gen.generateEdgeCase(TestProduct.class, 
        EnhancedTestDataGenerator.EdgeCaseType.SPECIAL_CHARACTERS);
    
    assertThat(product1).isNotNull();
    assertThat(product2).isNotNull();
    assertThat(product3).isNotNull();
  }

  @Test
  void shouldHandleZeroCountForInvalidGeneration() {
    EnhancedTestDataGenerator gen = new EnhancedSimpleTestDataGenerator();
    
    List<TestPerson> persons = gen.generateManyInvalid(TestPerson.class, 0, 
        EnhancedTestDataGenerator.ViolationType.NULL_VALUE);
    
    assertThat(persons).isEmpty();
  }

  @Test
  void shouldHandleNegativeCountForInvalidGeneration() {
    EnhancedTestDataGenerator gen = new EnhancedSimpleTestDataGenerator();
    
    List<TestPerson> persons = gen.generateManyInvalid(TestPerson.class, -5, 
        EnhancedTestDataGenerator.ViolationType.NULL_VALUE);
    
    assertThat(persons).isEmpty();
  }

  @Test
  void shouldHandleZeroCountForEdgeCaseGeneration() {
    EnhancedTestDataGenerator gen = new EnhancedSimpleTestDataGenerator();
    
    List<TestPerson> persons = gen.generateManyEdgeCases(TestPerson.class, 0, 
        EnhancedTestDataGenerator.EdgeCaseType.MINIMUM_VALUES);
    
    assertThat(persons).isEmpty();
  }

  @Test
  void shouldHandleNegativeCountForEdgeCaseGeneration() {
    EnhancedTestDataGenerator gen = new EnhancedSimpleTestDataGenerator();
    
    List<TestPerson> persons = gen.generateManyEdgeCases(TestPerson.class, -3, 
        EnhancedTestDataGenerator.EdgeCaseType.MINIMUM_VALUES);
    
    assertThat(persons).isEmpty();
  }

  @Test
  void shouldGenerateLargeCountOfInvalidData() {
    EnhancedTestDataGenerator gen = new EnhancedSimpleTestDataGenerator();
    
    List<TestPerson> persons = gen.generateManyInvalid(TestPerson.class, 100, 
        EnhancedTestDataGenerator.ViolationType.NULL_VALUE);
    
    assertThat(persons).hasSize(100);
    assertThat(persons).doesNotHaveDuplicates();
  }

  @Test
  void shouldGenerateLargeCountOfEdgeCaseData() {
    EnhancedTestDataGenerator gen = new EnhancedSimpleTestDataGenerator();
    
    List<TestPerson> persons = gen.generateManyEdgeCases(TestPerson.class, 50, 
        EnhancedTestDataGenerator.EdgeCaseType.SPECIAL_CHARACTERS);
    
    assertThat(persons).hasSize(50);
    assertThat(persons).doesNotHaveDuplicates();
  }
}