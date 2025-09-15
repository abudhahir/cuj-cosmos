package com.cleveloper.utilities.jsonschema.generation.data;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.Test;

class TestDataGeneratorTest {

  static class Person {
    public String name;
    public int age;
  }

  static class ComplexType {
    public String stringField;
    public int intField;
    public Integer integerField;
    public long longField;
    public Long longObjectField;
    public boolean booleanField;
    public Boolean booleanObjectField;
    public double doubleField;
    public Double doubleObjectField;
  }

  static class NoDefaultConstructor {
    public String name;
    
    public NoDefaultConstructor(String name) {
      this.name = name;
    }
  }

  @Test
  void instancioGeneratorShouldProduceNonNull() {
    TestDataGenerator gen = new InstancioTestDataGenerator();
    Person p = gen.generate(Person.class);
    assertThat(p).isNotNull();
    assertThat(p.name).isNotBlank();
    assertThat(p.age).isGreaterThanOrEqualTo(0);
  }

  @Test
  void simpleGeneratorShouldProduceNonNull() {
    TestDataGenerator gen = new SimpleTestDataGenerator();
    Person p = gen.generate(Person.class);
    assertThat(p).isNotNull();
    assertThat(p.name).isNotBlank();
  }

  @Test
  void generateManyShouldReturnRequestedCount() {
    TestDataGenerator gen = new SimpleTestDataGenerator();
    List<Person> list = gen.generateMany(Person.class, 5);
    assertThat(list).hasSize(5);
  }

  @Test
  void simpleGeneratorShouldHandleComplexTypes() {
    TestDataGenerator gen = new SimpleTestDataGenerator();
    ComplexType obj = gen.generate(ComplexType.class);
    
    assertThat(obj).isNotNull();
    assertThat(obj.stringField).isNotBlank();
    assertThat(obj.intField).isBetween(0, 99);
    assertThat(obj.integerField).isBetween(0, 99);
    assertThat(obj.longField).isBetween(0L, 999L);
    assertThat(obj.longObjectField).isBetween(0L, 999L);
    assertThat(obj.doubleField).isBetween(0.0, 1.0);
    assertThat(obj.doubleObjectField).isBetween(0.0, 1.0);
    // Boolean can be true or false, so just check it's not null
    assertThat(obj.booleanObjectField).isNotNull();
  }

  @Test
  void simpleGeneratorShouldHandleNegativeCount() {
    TestDataGenerator gen = new SimpleTestDataGenerator();
    List<Person> list = gen.generateMany(Person.class, -5);
    assertThat(list).isEmpty();
  }

  @Test
  void simpleGeneratorShouldHandleZeroCount() {
    TestDataGenerator gen = new SimpleTestDataGenerator();
    List<Person> list = gen.generateMany(Person.class, 0);
    assertThat(list).isEmpty();
  }

  @Test
  void simpleGeneratorShouldHandleLargeCount() {
    TestDataGenerator gen = new SimpleTestDataGenerator();
    List<Person> list = gen.generateMany(Person.class, 1000);
    assertThat(list).hasSize(1000);
    // Verify all objects are different instances
    assertThat(list).doesNotHaveDuplicates();
  }

  @Test
  void simpleGeneratorShouldThrowExceptionForNoDefaultConstructor() {
    TestDataGenerator gen = new SimpleTestDataGenerator();
    assertThatThrownBy(() -> gen.generate(NoDefaultConstructor.class))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("Failed to instantiate");
  }

  @Test
  void instancioGeneratorShouldHandleComplexTypes() {
    TestDataGenerator gen = new InstancioTestDataGenerator();
    ComplexType obj = gen.generate(ComplexType.class);
    
    assertThat(obj).isNotNull();
    assertThat(obj.stringField).isNotBlank();
    assertThat(obj.intField).isNotNull();
    assertThat(obj.integerField).isNotNull();
    assertThat(obj.longField).isNotNull();
    assertThat(obj.longObjectField).isNotNull();
    assertThat(obj.doubleField).isNotNull();
    assertThat(obj.doubleObjectField).isNotNull();
    assertThat(obj.booleanObjectField).isNotNull();
  }

  @Test
  void instancioGeneratorShouldHandleNegativeCount() {
    TestDataGenerator gen = new InstancioTestDataGenerator();
    List<Person> list = gen.generateMany(Person.class, -5);
    assertThat(list).isEmpty();
  }

  @Test
  void instancioGeneratorShouldHandleZeroCount() {
    TestDataGenerator gen = new InstancioTestDataGenerator();
    List<Person> list = gen.generateMany(Person.class, 0);
    assertThat(list).isEmpty();
  }

  @Test
  void instancioGeneratorShouldHandleLargeCount() {
    TestDataGenerator gen = new InstancioTestDataGenerator();
    List<Person> list = gen.generateMany(Person.class, 100);
    assertThat(list).hasSize(100);
    // Verify all objects are different instances
    assertThat(list).doesNotHaveDuplicates();
  }
}

