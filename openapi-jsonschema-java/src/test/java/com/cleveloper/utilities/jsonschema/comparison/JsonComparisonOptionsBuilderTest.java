package com.cleveloper.utilities.jsonschema.comparison;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonComparisonOptionsBuilderTest {

  @Test
  @DisplayName("builder should clamp numeric tolerance and max depth and collect pointers")
  void builderClampsAndCollects() {
    JsonComparisonOptions opts =
        JsonComparisonOptions.builder()
            .numericTolerance(-5.0) // clamp to 0
            .maxDepth(0) // clamp to >=1
            .ignorePointer("/a/b")
            .ignorePointers(Set.of("/x", null, "/y"))
            .excludeField("id")
            .excludeFieldPattern("^ts_.*$")
            .ignoreArrayOrder(true)
            .ignoreExtraFields(true)
            .caseSensitive(false)
            .ignoreWhitespace(true)
            .strictTypeChecking(false)
            .build();

    assertThat(opts.getNumericTolerance()).isEqualTo(0.0);
    assertThat(opts.getMaxDepth()).isGreaterThanOrEqualTo(1);
    assertThat(opts.getIgnoredPointers()).contains("/a/b", "/x", "/y");
    assertThat(opts.getExcludedFields()).contains("id");
    assertThat(opts.getExcludedFieldPatterns()).hasSize(1);
    assertThat(opts.isIgnoreArrayOrder()).isTrue();
    assertThat(opts.isIgnoreExtraFields()).isTrue();
    assertThat(opts.isCaseSensitive()).isFalse();
    assertThat(opts.isIgnoreWhitespace()).isTrue();
    assertThat(opts.isStrictTypeChecking()).isFalse();
  }
}

