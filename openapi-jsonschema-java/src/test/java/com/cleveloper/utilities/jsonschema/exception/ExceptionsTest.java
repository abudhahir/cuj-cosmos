package com.cleveloper.utilities.jsonschema.exception;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class ExceptionsTest {

  @Test
  void validationExceptionShouldExposeErrorsAndContext() {
    ValidationException ex = new ValidationException("failed", List.of("e1", "e2"));
    assertThat(ex.getErrorCode()).isEqualTo("VAL_001");
    assertThat(ex.getErrors()).containsExactly("e1", "e2");
    assertThat(ex.getContext()).containsEntry("errorCount", 2);
  }

  @Test
  void schemaGenerationExceptionShouldIncludeTargetClass() {
    SchemaGenerationException ex = new SchemaGenerationException("oops", String.class);
    assertThat(ex.getErrorCode()).isEqualTo("GEN_001");
    assertThat(ex.getContext()).containsEntry("targetClass", String.class.getName());
  }

  @Test
  void schemaGenerationExceptionShouldSupportCauseAndNullTarget() {
    RuntimeException cause = new RuntimeException("root");
    SchemaGenerationException ex = new SchemaGenerationException("boom", null, cause);
    assertThat(ex.getErrorCode()).isEqualTo("GEN_001");
    assertThat(ex.getContext()).containsEntry("targetClass", "<unknown>");
    assertThat(ex.getCause()).isSameAs(cause);
  }

  @Test
  void jsonUtilityExceptionSecondaryConstructorAndAddContext() {
    class TestException extends JsonUtilityException {
      TestException(String message) {
        super(message, "TST_000");
      }

      TestException(String message, Throwable cause) {
        super(message, "TST_000", cause);
      }

      void put(String k, Object v) {
        addContext(k, v);
      }
    }

    TestException ex1 = new TestException("m1");
    ex1.put("k1", 1);
    assertThat(ex1.getErrorCode()).isEqualTo("TST_000");
    assertThat(ex1.getContext()).containsEntry("k1", 1);

    RuntimeException cause = new RuntimeException("root");
    TestException ex2 = new TestException("m2", cause);
    ex2.put("k2", 2);
    assertThat(ex2.getErrorCode()).isEqualTo("TST_000");
    assertThat(ex2.getContext()).containsEntry("k2", 2);
    assertThat(ex2.getCause()).isSameAs(cause);
  }
}
