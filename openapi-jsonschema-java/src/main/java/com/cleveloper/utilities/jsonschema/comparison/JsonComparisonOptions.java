package com.cleveloper.utilities.jsonschema.comparison;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

/** Options to customize JSON comparison behavior. */
public final class JsonComparisonOptions {
  private final Set<String> ignoredPointers;
  private final double numericTolerance;
  private final boolean ignoreArrayOrder;
  private final boolean ignoreExtraFields;
  private final boolean caseSensitive;
  private final Set<String> excludedFields;
  private final Set<Pattern> excludedFieldPatterns;
  private final boolean ignoreWhitespace;
  private final boolean strictTypeChecking;
  private final int maxDepth;

  private JsonComparisonOptions(Builder b) {
    this.ignoredPointers = Collections.unmodifiableSet(new LinkedHashSet<>(b.ignoredPointers));
    this.numericTolerance = b.numericTolerance;
    this.ignoreArrayOrder = b.ignoreArrayOrder;
    this.ignoreExtraFields = b.ignoreExtraFields;
    this.caseSensitive = b.caseSensitive;
    this.excludedFields = Collections.unmodifiableSet(new LinkedHashSet<>(b.excludedFields));
    this.excludedFieldPatterns = Collections.unmodifiableSet(new LinkedHashSet<>(b.excludedFieldPatterns));
    this.ignoreWhitespace = b.ignoreWhitespace;
    this.strictTypeChecking = b.strictTypeChecking;
    this.maxDepth = b.maxDepth;
  }

  public Set<String> getIgnoredPointers() {
    return ignoredPointers;
  }

  public double getNumericTolerance() {
    return numericTolerance;
  }

  public boolean isIgnoreArrayOrder() {
    return ignoreArrayOrder;
  }

  public boolean isIgnoreExtraFields() {
    return ignoreExtraFields;
  }

  public boolean isCaseSensitive() {
    return caseSensitive;
  }

  public Set<String> getExcludedFields() {
    return excludedFields;
  }

  public Set<Pattern> getExcludedFieldPatterns() {
    return excludedFieldPatterns;
  }

  public boolean isIgnoreWhitespace() {
    return ignoreWhitespace;
  }

  public boolean isStrictTypeChecking() {
    return strictTypeChecking;
  }

  public int getMaxDepth() {
    return maxDepth;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private final Set<String> ignoredPointers = new LinkedHashSet<>();
    private final Set<String> excludedFields = new LinkedHashSet<>();
    private final Set<Pattern> excludedFieldPatterns = new LinkedHashSet<>();
    private double numericTolerance = 0.0;
    private boolean ignoreArrayOrder = false;
    private boolean ignoreExtraFields = false;
    private boolean caseSensitive = true;
    private boolean ignoreWhitespace = false;
    private boolean strictTypeChecking = true;
    private int maxDepth = 100;

    public Builder ignorePointer(String jsonPointer) {
      if (jsonPointer != null && !jsonPointer.isBlank()) {
        ignoredPointers.add(jsonPointer);
      }
      return this;
    }

    public Builder ignorePointers(Set<String> jsonPointers) {
      if (jsonPointers != null) {
        jsonPointers.stream().filter(Objects::nonNull).forEach(ignoredPointers::add);
      }
      return this;
    }

    /** Consider numeric values equal when abs(diff) <= tolerance. */
    public Builder numericTolerance(double tolerance) {
      this.numericTolerance = Math.max(0.0, tolerance);
      return this;
    }

    /** Ignore the order of elements in arrays. */
    public Builder ignoreArrayOrder(boolean ignore) {
      this.ignoreArrayOrder = ignore;
      return this;
    }

    /** Ignore extra fields in objects that are not present in the expected object. */
    public Builder ignoreExtraFields(boolean ignore) {
      this.ignoreExtraFields = ignore;
      return this;
    }

    /** Enable case-sensitive string comparison. */
    public Builder caseSensitive(boolean sensitive) {
      this.caseSensitive = sensitive;
      return this;
    }

    /** Exclude specific field names from comparison. */
    public Builder excludeField(String fieldName) {
      if (fieldName != null && !fieldName.isBlank()) {
        excludedFields.add(fieldName);
      }
      return this;
    }

    /** Exclude fields matching the given pattern. */
    public Builder excludeFieldPattern(String pattern) {
      if (pattern != null && !pattern.isBlank()) {
        excludedFieldPatterns.add(Pattern.compile(pattern));
      }
      return this;
    }

    /** Ignore whitespace differences in strings. */
    public Builder ignoreWhitespace(boolean ignore) {
      this.ignoreWhitespace = ignore;
      return this;
    }

    /** Enable strict type checking (no type coercion). */
    public Builder strictTypeChecking(boolean strict) {
      this.strictTypeChecking = strict;
      return this;
    }

    /** Set maximum depth for recursive comparison. */
    public Builder maxDepth(int depth) {
      this.maxDepth = Math.max(1, depth);
      return this;
    }

    public JsonComparisonOptions build() {
      return new JsonComparisonOptions(this);
    }
  }
}

