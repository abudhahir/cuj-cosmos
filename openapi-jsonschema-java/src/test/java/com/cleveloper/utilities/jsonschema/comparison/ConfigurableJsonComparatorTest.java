package com.cleveloper.utilities.jsonschema.comparison;

import static org.assertj.core.api.Assertions.assertThat;

import com.cleveloper.utilities.jsonschema.JsonComparator;
import org.junit.jupiter.api.Test;

class ConfigurableJsonComparatorTest {

  @Test
  void shouldIgnoreSpecifiedPath() {
    String exp = "{\"meta\":{\"ts\":123},\"val\":1}";
    String act = "{\"meta\":{\"ts\":999},\"val\":1}";
    JsonComparisonOptions opts =
        JsonComparisonOptions.builder().ignorePointer("/meta/ts").build();
    JsonComparator cmp = new ConfigurableJsonComparator(opts);
    String diff = cmp.compare(exp, act);
    assertThat(diff).isEmpty();
  }

  @Test
  void shouldApplyNumericTolerance() {
    String exp = "{\"score\": 10.0}";
    String act = "{\"score\": 10.001}";
    JsonComparisonOptions opts = JsonComparisonOptions.builder().numericTolerance(0.01).build();
    JsonComparator cmp = new ConfigurableJsonComparator(opts);
    String diff = cmp.compare(exp, act);
    assertThat(diff).isEmpty();
  }

  @Test
  void shouldIgnoreMultiplePaths() {
    String exp = "{\"meta\":{\"ts\":123,\"id\":\"abc\"},\"val\":1,\"other\":2}";
    String act = "{\"meta\":{\"ts\":999,\"id\":\"xyz\"},\"val\":1,\"other\":3}";
    JsonComparisonOptions opts = JsonComparisonOptions.builder()
        .ignorePointer("/meta/ts")
        .ignorePointer("/meta/id")
        .build();
    JsonComparator cmp = new ConfigurableJsonComparator(opts);
    String diff = cmp.compare(exp, act);
    assertThat(diff).contains("/other: expected=2.0, actual=3.0");
  }

  @Test
  void shouldApplyNumericToleranceToMultipleValues() {
    String exp = "{\"score1\": 10.0, \"score2\": 20.0}";
    String act = "{\"score1\": 10.001, \"score2\": 20.001}";
    JsonComparisonOptions opts = JsonComparisonOptions.builder().numericTolerance(0.01).build();
    JsonComparator cmp = new ConfigurableJsonComparator(opts);
    String diff = cmp.compare(exp, act);
    assertThat(diff).isEmpty();
  }

  @Test
  void shouldNotApplyNumericToleranceWhenDifferenceExceedsTolerance() {
    String exp = "{\"score\": 10.0}";
    String act = "{\"score\": 10.1}";
    JsonComparisonOptions opts = JsonComparisonOptions.builder().numericTolerance(0.01).build();
    JsonComparator cmp = new ConfigurableJsonComparator(opts);
    String diff = cmp.compare(exp, act);
    assertThat(diff).contains("/score: expected=10.0, actual=10.1");
  }

  @Test
  void shouldHandleZeroTolerance() {
    String exp = "{\"score\": 10.0}";
    String act = "{\"score\": 10.0}";
    JsonComparisonOptions opts = JsonComparisonOptions.builder().numericTolerance(0.0).build();
    JsonComparator cmp = new ConfigurableJsonComparator(opts);
    String diff = cmp.compare(exp, act);
    assertThat(diff).isEmpty();
  }

  @Test
  void shouldHandleLargeTolerance() {
    String exp = "{\"score\": 10.0}";
    String act = "{\"score\": 100.0}";
    JsonComparisonOptions opts = JsonComparisonOptions.builder().numericTolerance(100.0).build();
    JsonComparator cmp = new ConfigurableJsonComparator(opts);
    String diff = cmp.compare(exp, act);
    assertThat(diff).isEmpty();
  }

  @Test
  void shouldIgnoreNestedPaths() {
    String exp = "{\"user\":{\"profile\":{\"timestamp\":123,\"name\":\"Alice\"}}}";
    String act = "{\"user\":{\"profile\":{\"timestamp\":999,\"name\":\"Bob\"}}}";
    JsonComparisonOptions opts = JsonComparisonOptions.builder()
        .ignorePointer("/user/profile/timestamp")
        .build();
    JsonComparator cmp = new ConfigurableJsonComparator(opts);
    String diff = cmp.compare(exp, act);
    assertThat(diff).contains("/user/profile/name: expected=\"Alice\", actual=\"Bob\"");
  }

  @Test
  void shouldHandleArrayIndicesInIgnorePaths() {
    String exp = "{\"items\":[{\"id\":1,\"value\":100},{\"id\":2,\"value\":200}]}";
    String act = "{\"items\":[{\"id\":1,\"value\":101},{\"id\":2,\"value\":201}]}";
    JsonComparisonOptions opts = JsonComparisonOptions.builder()
        .ignorePointer("/items/0/value")
        .ignorePointer("/items/1/value")
        .build();
    JsonComparator cmp = new ConfigurableJsonComparator(opts);
    String diff = cmp.compare(exp, act);
    assertThat(diff).isEmpty();
  }

  @Test
  void shouldCombineIgnorePathsAndNumericTolerance() {
    String exp = "{\"meta\":{\"ts\":123},\"score\":10.0}";
    String act = "{\"meta\":{\"ts\":999},\"score\":10.001}";
    JsonComparisonOptions opts = JsonComparisonOptions.builder()
        .ignorePointer("/meta/ts")
        .numericTolerance(0.01)
        .build();
    JsonComparator cmp = new ConfigurableJsonComparator(opts);
    String diff = cmp.compare(exp, act);
    assertThat(diff).isEmpty();
  }

  @Test
  void shouldHandleEmptyOptions() {
    String exp = "{\"value\":1}";
    String act = "{\"value\":2}";
    JsonComparisonOptions opts = JsonComparisonOptions.builder().build();
    JsonComparator cmp = new ConfigurableJsonComparator(opts);
    String diff = cmp.compare(exp, act);
    assertThat(diff).contains("/value: expected=1.0, actual=2.0");
  }

  @Test
  void shouldHandleNonNumericValuesWithTolerance() {
    String exp = "{\"text\":\"hello\",\"number\":10.0}";
    String act = "{\"text\":\"world\",\"number\":10.001}";
    JsonComparisonOptions opts = JsonComparisonOptions.builder().numericTolerance(0.01).build();
    JsonComparator cmp = new ConfigurableJsonComparator(opts);
    String diff = cmp.compare(exp, act);
    assertThat(diff).contains("/text: expected=\"hello\", actual=\"world\"");
    assertThat(diff).doesNotContain("/number");
  }
}

