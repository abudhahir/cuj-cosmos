package com.cleveloper.utilities.jsonschema.generation.data;

import java.util.List;
import java.util.Map;

/**
 * Enhanced test data generator that supports both valid and invalid data generation
 * with configurable constraints and edge cases.
 */
public interface EnhancedTestDataGenerator extends TestDataGenerator {

    /**
     * Generate valid test data conforming to the specified constraints.
     *
     * @param type the target class type
     * @param constraints optional constraints to apply
     * @param <T> the type parameter
     * @return valid test data instance
     */
    <T> T generateValid(Class<T> type, Map<String, Object> constraints);

    /**
     * Generate invalid test data that violates specific constraints.
     *
     * @param type the target class type
     * @param violationType the type of constraint violation to generate
     * @param <T> the type parameter
     * @return invalid test data instance
     */
    <T> T generateInvalid(Class<T> type, ViolationType violationType);

    /**
     * Generate edge case data (boundary values, nulls, empty collections).
     *
     * @param type the target class type
     * @param edgeCaseType the type of edge case to generate
     * @param <T> the type parameter
     * @return edge case test data instance
     */
    <T> T generateEdgeCase(Class<T> type, EdgeCaseType edgeCaseType);

    /**
     * Generate multiple invalid data instances for comprehensive testing.
     *
     * @param type the target class type
     * @param count number of instances to generate
     * @param violationType the type of constraint violation
     * @param <T> the type parameter
     * @return list of invalid test data instances
     */
    <T> List<T> generateManyInvalid(Class<T> type, int count, ViolationType violationType);

    /**
     * Generate multiple edge case data instances.
     *
     * @param type the target class type
     * @param count number of instances to generate
     * @param edgeCaseType the type of edge case
     * @param <T> the type parameter
     * @return list of edge case test data instances
     */
    <T> List<T> generateManyEdgeCases(Class<T> type, int count, EdgeCaseType edgeCaseType);

    /**
     * Generate realistic test data with proper formatting (emails, dates, etc.).
     *
     * @param type the target class type
     * @param <T> the type parameter
     * @return realistic test data instance
     */
    <T> T generateRealistic(Class<T> type);

    /**
     * Generate deterministic test data for reproducible tests.
     *
     * @param type the target class type
     * @param seed the seed for deterministic generation
     * @param <T> the type parameter
     * @return deterministic test data instance
     */
    <T> T generateDeterministic(Class<T> type, long seed);

    /**
     * Types of constraint violations that can be generated.
     */
    enum ViolationType {
        NULL_VALUE,           // Generate null where not allowed
        EMPTY_STRING,         // Generate empty string where minLength > 0
        STRING_TOO_LONG,      // Generate string exceeding maxLength
        STRING_TOO_SHORT,     // Generate string below minLength
        INVALID_PATTERN,      // Generate string that doesn't match pattern
        NUMBER_TOO_LARGE,     // Generate number exceeding maximum
        NUMBER_TOO_SMALL,     // Generate number below minimum
        INVALID_FORMAT,       // Generate invalid format (email, date, etc.)
        ARRAY_TOO_LARGE,      // Generate array exceeding maxItems
        ARRAY_TOO_SMALL,      // Generate array below minItems
        DUPLICATE_ITEMS,      // Generate array with duplicate items when uniqueItems=true
        MISSING_REQUIRED,     // Generate object missing required fields
        EXTRA_PROPERTIES,     // Generate object with extra properties when additionalProperties=false
        INVALID_TYPE          // Generate wrong data type
    }

    /**
     * Types of edge cases that can be generated.
     */
    enum EdgeCaseType {
        MINIMUM_VALUES,       // Generate minimum allowed values
        MAXIMUM_VALUES,       // Generate maximum allowed values
        BOUNDARY_VALUES,      // Generate values at boundaries
        NULL_VALUES,          // Generate null values where allowed
        EMPTY_COLLECTIONS,    // Generate empty arrays/collections
        SINGLE_ITEM_ARRAYS,   // Generate arrays with single item
        ZERO_VALUES,          // Generate zero values
        NEGATIVE_VALUES,      // Generate negative values where allowed
        SPECIAL_CHARACTERS,   // Generate strings with special characters
        UNICODE_CHARACTERS,   // Generate strings with Unicode characters
        VERY_LONG_STRINGS,    // Generate very long strings
        VERY_LARGE_NUMBERS,   // Generate very large numbers
        VERY_SMALL_NUMBERS    // Generate very small numbers
    }
}