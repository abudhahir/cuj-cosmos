package com.cleveloper.utilities.jsonschema;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DefaultValidationEngineTest {

  private ValidationEngine engine;

  @BeforeEach
  void setUp() {
    engine = new DefaultValidationEngine();
  }

  @Test
  @DisplayName("should return true for object-like JSON")
  void shouldReturnTrueForObjectLike() {
    assertThat(engine.isValid("{\"k\":1}")).isTrue();
  }

  @Test
  @DisplayName("should return true for array-like JSON")
  void shouldReturnTrueForArrayLike() {
    assertThat(engine.isValid("[1,2,3]")).isTrue();
  }

  @Test
  @DisplayName("should return false for null or empty")
  void shouldReturnFalseForNullOrEmpty() {
    assertThat(engine.isValid(null)).isFalse();
    assertThat(engine.isValid("   ")).isFalse();
  }

  @Test
  @DisplayName("should return false for non-JSON strings")
  void shouldReturnFalseForNonJson() {
    assertThat(engine.isValid("hello")).isFalse();
    assertThat(engine.isValid("<xml></xml>")).isFalse();
  }
}
