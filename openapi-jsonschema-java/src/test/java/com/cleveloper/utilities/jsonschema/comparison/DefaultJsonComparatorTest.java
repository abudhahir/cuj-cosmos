package com.cleveloper.utilities.jsonschema.comparison;

import static org.assertj.core.api.Assertions.assertThat;

import com.cleveloper.utilities.jsonschema.JsonComparator;
import org.junit.jupiter.api.Test;

class DefaultJsonComparatorTest {

  @Test
  void shouldReturnEmptyWhenEqualObjectsIgnoringWhitespace() {
    String a = "{\n  \"name\": \"Alice\", \"age\": 30 }";
    String b = "{\"age\":30,\n\"name\":\"Alice\"}";
    JsonComparator cmp = new DefaultJsonComparator();
    String diff = cmp.compare(a, b);
    assertThat(diff).isEmpty();
  }

  @Test
  void shouldReportMissingAndUnexpectedFields() {
    String exp = "{\"a\":1,\"b\":2}";
    String act = "{\"a\":1,\"c\":3}";
    JsonComparator cmp = new DefaultJsonComparator();
    String diff = cmp.compare(exp, act);
    assertThat(diff).contains("/b: missing in actual");
    assertThat(diff).contains("/c: unexpected field in actual");
  }

  @Test
  void shouldReportValueDifferences() {
    String exp = "{\"n\": [1,2,3]}";
    String act = "{\"n\": [1,4,3,5]}";
    JsonComparator cmp = new DefaultJsonComparator();
    String diff = cmp.compare(exp, act);
    assertThat(diff).contains("/n/1");
    assertThat(diff).contains("actual has more items");
  }

  @Test
  void shouldHandleNullValues() {
    String exp = "{\"a\": null, \"b\": 1}";
    String act = "{\"a\": 1, \"b\": null}";
    JsonComparator cmp = new DefaultJsonComparator();
    String diff = cmp.compare(exp, act);
    assertThat(diff).contains("/a: expected=null, actual=1");
    assertThat(diff).contains("/b: expected=1, actual=null");
  }

  @Test
  void shouldHandleEmptyObjects() {
    String exp = "{}";
    String act = "{\"key\": \"value\"}";
    JsonComparator cmp = new DefaultJsonComparator();
    String diff = cmp.compare(exp, act);
    assertThat(diff).contains("/key: unexpected field in actual");
  }

  @Test
  void shouldHandleEmptyArrays() {
    String exp = "[]";
    String act = "[1, 2, 3]";
    JsonComparator cmp = new DefaultJsonComparator();
    String diff = cmp.compare(exp, act);
    assertThat(diff).contains("actual has more items");
  }

  @Test
  void shouldHandleNestedObjects() {
    String exp = "{\"user\": {\"name\": \"Alice\", \"age\": 30}}";
    String act = "{\"user\": {\"name\": \"Bob\", \"age\": 30}}";
    JsonComparator cmp = new DefaultJsonComparator();
    String diff = cmp.compare(exp, act);
    assertThat(diff).contains("/user/name: expected=\"Alice\", actual=\"Bob\"");
  }

  @Test
  void shouldHandleNestedArrays() {
    String exp = "{\"matrix\": [[1, 2], [3, 4]]}";
    String act = "{\"matrix\": [[1, 2], [3, 5]]}";
    JsonComparator cmp = new DefaultJsonComparator();
    String diff = cmp.compare(exp, act);
    assertThat(diff).contains("/matrix/1/1: expected=4, actual=5");
  }

  @Test
  void shouldHandleDifferentDataTypes() {
    String exp = "{\"value\": 123}";
    String act = "{\"value\": \"123\"}";
    JsonComparator cmp = new DefaultJsonComparator();
    String diff = cmp.compare(exp, act);
    assertThat(diff).contains("/value: expected=123, actual=\"123\"");
  }

  @Test
  void shouldHandleBooleanValues() {
    String exp = "{\"flag\": true}";
    String act = "{\"flag\": false}";
    JsonComparator cmp = new DefaultJsonComparator();
    String diff = cmp.compare(exp, act);
    assertThat(diff).contains("/flag: expected=true, actual=false");
  }

  @Test
  void shouldHandleFloatingPointValues() {
    String exp = "{\"pi\": 3.14159}";
    String act = "{\"pi\": 3.1416}";
    JsonComparator cmp = new DefaultJsonComparator();
    String diff = cmp.compare(exp, act);
    assertThat(diff).contains("/pi: expected=3.14159, actual=3.1416");
  }

  @Test
  void shouldHandleSpecialCharactersInKeys() {
    String exp = "{\"key/with~special\": \"value\"}";
    String act = "{\"key/with~special\": \"different\"}";
    JsonComparator cmp = new DefaultJsonComparator();
    String diff = cmp.compare(exp, act);
    assertThat(diff).contains("/key~1with~0special: expected=\"value\", actual=\"different\"");
  }

  @Test
  void shouldHandleInvalidJsonInExpected() {
    String exp = "{\"invalid\": json}";
    String act = "{\"valid\": \"json\"}";
    JsonComparator cmp = new DefaultJsonComparator();
    String diff = cmp.compare(exp, act);
    assertThat(diff).contains("Failed to parse JSON");
  }

  @Test
  void shouldHandleInvalidJsonInActual() {
    String exp = "{\"valid\": \"json\"}";
    String act = "{\"invalid\": json}";
    JsonComparator cmp = new DefaultJsonComparator();
    String diff = cmp.compare(exp, act);
    assertThat(diff).contains("Failed to parse JSON");
  }

  @Test
  void shouldHandleComplexNestedStructure() {
    String exp = "{\"users\": [{\"id\": 1, \"profile\": {\"name\": \"Alice\", \"active\": true}}]}";
    String act = "{\"users\": [{\"id\": 1, \"profile\": {\"name\": \"Bob\", \"active\": true}}]}";
    JsonComparator cmp = new DefaultJsonComparator();
    String diff = cmp.compare(exp, act);
    assertThat(diff).contains("/users/0/profile/name: expected=\"Alice\", actual=\"Bob\"");
  }

  @Test
  void shouldHandleArrayWithDifferentLengths() {
    String exp = "{\"items\": [1, 2]}";
    String act = "{\"items\": [1, 2, 3, 4]}";
    JsonComparator cmp = new DefaultJsonComparator();
    String diff = cmp.compare(exp, act);
    assertThat(diff).contains("actual has more items");
  }

  @Test
  void shouldHandleArrayWithMissingItems() {
    String exp = "{\"items\": [1, 2, 3, 4]}";
    String act = "{\"items\": [1, 2]}";
    JsonComparator cmp = new DefaultJsonComparator();
    String diff = cmp.compare(exp, act);
    assertThat(diff).contains("expected has more items");
  }
}

