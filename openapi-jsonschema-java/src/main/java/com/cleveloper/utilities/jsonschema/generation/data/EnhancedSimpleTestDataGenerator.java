package com.cleveloper.utilities.jsonschema.generation.data;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Enhanced implementation of SimpleTestDataGenerator that supports invalid data generation
 * and edge cases for comprehensive testing.
 */
public class EnhancedSimpleTestDataGenerator implements EnhancedTestDataGenerator {
    
    private static final Random RND = new Random(12345L);
    private final SimpleTestDataGenerator baseGenerator;

    public EnhancedSimpleTestDataGenerator() {
        this.baseGenerator = new SimpleTestDataGenerator();
    }

    @Override
    public <T> T generate(Class<T> type) {
        return baseGenerator.generate(type);
    }

    @Override
    public <T> List<T> generateMany(Class<T> type, int count) {
        return baseGenerator.generateMany(type, count);
    }

    @Override
    public <T> T generateValid(Class<T> type, Map<String, Object> constraints) {
        // For now, just use the base generator
        // In a full implementation, this would apply constraints
        return baseGenerator.generate(type);
    }

    @Override
    public <T> T generateInvalid(Class<T> type, ViolationType violationType) {
        try {
            T instance = createInstance(type);

            for (Field f : type.getFields()) {
                Class<?> ft = f.getType();
                Object invalidValue = generateInvalidValue(ft, violationType);
                // Skip null values for primitive types
                if (invalidValue == null && ft.isPrimitive()) {
                    continue;
                }
                f.set(instance, invalidValue);
            }

            return instance;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to instantiate " + type.getName(), e);
        }
    }

    @Override
    public <T> T generateEdgeCase(Class<T> type, EdgeCaseType edgeCaseType) {
        try {
            T instance = createInstance(type);

            for (Field f : type.getFields()) {
                Class<?> ft = f.getType();
                Object edgeValue = generateEdgeCaseValue(ft, edgeCaseType);
                // Skip null values for primitive types
                if (edgeValue == null && ft.isPrimitive()) {
                    continue;
                }
                f.set(instance, edgeValue);
            }

            return instance;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to instantiate " + type.getName(), e);
        }
    }

    @Override
    public <T> List<T> generateManyInvalid(Class<T> type, int count, ViolationType violationType) {
        int n = Math.max(0, count);
        List<T> list = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            list.add(generateInvalid(type, violationType));
        }
        return list;
    }

    @Override
    public <T> List<T> generateManyEdgeCases(Class<T> type, int count, EdgeCaseType edgeCaseType) {
        int n = Math.max(0, count);
        List<T> list = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            list.add(generateEdgeCase(type, edgeCaseType));
        }
        return list;
    }

    @Override
    public <T> T generateRealistic(Class<T> type) {
        // For now, use base generator
        // In a full implementation, this would use Java Faker or similar
        return baseGenerator.generate(type);
    }

    @Override
    public <T> T generateDeterministic(Class<T> type, long seed) {
        // Create a new generator with the specified seed
        Random seededRandom = new Random(seed);
        try {
            T instance = createInstance(type);

            for (Field f : type.getFields()) {
                Class<?> ft = f.getType();
                Object value = generateDeterministicValue(ft, seededRandom);
                f.set(instance, value);
            }

            return instance;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to instantiate " + type.getName(), e);
        }
    }

    private Object generateInvalidValue(Class<?> fieldType, ViolationType violationType) {
        switch (violationType) {
            case NULL_VALUE:
                return null;
            case EMPTY_STRING:
                if (fieldType == String.class) return "";
                break;
            case STRING_TOO_LONG:
                if (fieldType == String.class) return generateLongString(1000);
                break;
            case STRING_TOO_SHORT:
                if (fieldType == String.class) return "";
                break;
            case INVALID_PATTERN:
                if (fieldType == String.class) return "invalid-pattern-123";
                break;
            case NUMBER_TOO_LARGE:
                if (fieldType == int.class || fieldType == Integer.class) return Integer.MAX_VALUE;
                if (fieldType == long.class || fieldType == Long.class) return Long.MAX_VALUE;
                if (fieldType == double.class || fieldType == Double.class) return Double.MAX_VALUE;
                break;
            case NUMBER_TOO_SMALL:
                if (fieldType == int.class || fieldType == Integer.class) return Integer.MIN_VALUE;
                if (fieldType == long.class || fieldType == Long.class) return Long.MIN_VALUE;
                if (fieldType == double.class || fieldType == Double.class) return Double.MIN_VALUE;
                break;
            case INVALID_FORMAT:
                if (fieldType == String.class) return "invalid-email-format";
                break;
            case INVALID_TYPE:
                if (fieldType == String.class) return 123; // Wrong type
                if (fieldType == int.class || fieldType == Integer.class) return "string-instead-of-int";
                break;
            default:
                return generateDefaultValue(fieldType);
        }
        return generateDefaultValue(fieldType);
    }

    private Object generateEdgeCaseValue(Class<?> fieldType, EdgeCaseType edgeCaseType) {
        switch (edgeCaseType) {
            case MINIMUM_VALUES:
                if (fieldType == int.class || fieldType == Integer.class) return 0;
                if (fieldType == long.class || fieldType == Long.class) return 0L;
                if (fieldType == double.class || fieldType == Double.class) return 0.0;
                if (fieldType == String.class) return "a"; // Minimum length
                break;
            case MAXIMUM_VALUES:
                if (fieldType == int.class || fieldType == Integer.class) return Integer.MAX_VALUE;
                if (fieldType == long.class || fieldType == Long.class) return Long.MAX_VALUE;
                if (fieldType == double.class || fieldType == Double.class) return Double.MAX_VALUE;
                if (fieldType == String.class) return generateLongString(100);
                break;
            case BOUNDARY_VALUES:
                if (fieldType == int.class || fieldType == Integer.class) return 1;
                if (fieldType == long.class || fieldType == Long.class) return 1L;
                if (fieldType == double.class || fieldType == Double.class) return 1.0;
                if (fieldType == String.class) return "boundary";
                break;
            case NULL_VALUES:
                return null;
            case EMPTY_COLLECTIONS:
                if (fieldType == String.class) return "";
                break;
            case ZERO_VALUES:
                if (fieldType == int.class || fieldType == Integer.class) return 0;
                if (fieldType == long.class || fieldType == Long.class) return 0L;
                if (fieldType == double.class || fieldType == Double.class) return 0.0;
                break;
            case NEGATIVE_VALUES:
                if (fieldType == int.class || fieldType == Integer.class) return -1;
                if (fieldType == long.class || fieldType == Long.class) return -1L;
                if (fieldType == double.class || fieldType == Double.class) return -1.0;
                break;
            case SPECIAL_CHARACTERS:
                if (fieldType == String.class) return "special!@#$%^&*()_+-=[]{}|;':\",./<>?";
                break;
            case UNICODE_CHARACTERS:
                if (fieldType == String.class) return "Unicode: 你好世界 🌍 émojis";
                break;
            case VERY_LONG_STRINGS:
                if (fieldType == String.class) return generateLongString(10000);
                break;
            case VERY_LARGE_NUMBERS:
                if (fieldType == int.class || fieldType == Integer.class) return Integer.MAX_VALUE - 1;
                if (fieldType == long.class || fieldType == Long.class) return Long.MAX_VALUE - 1;
                if (fieldType == double.class || fieldType == Double.class) return Double.MAX_VALUE - 1;
                break;
            case VERY_SMALL_NUMBERS:
                if (fieldType == int.class || fieldType == Integer.class) return Integer.MIN_VALUE + 1;
                if (fieldType == long.class || fieldType == Long.class) return Long.MIN_VALUE + 1;
                if (fieldType == double.class || fieldType == Double.class) return Double.MIN_VALUE + 1;
                break;
            default:
                return generateDefaultValue(fieldType);
        }
        return generateDefaultValue(fieldType);
    }

    private Object generateDeterministicValue(Class<?> fieldType, Random random) {
        if (fieldType == String.class) return "str-" + random.nextInt(1000);
        else if (fieldType == int.class || fieldType == Integer.class) return random.nextInt(100);
        else if (fieldType == long.class || fieldType == Long.class) return Math.abs(random.nextLong() % 1000);
        else if (fieldType == boolean.class || fieldType == Boolean.class) return random.nextBoolean();
        else if (fieldType == double.class || fieldType == Double.class) return random.nextDouble();
        return null;
    }

    private Object generateDefaultValue(Class<?> fieldType) {
        if (fieldType == String.class) return "str-" + RND.nextInt(1000);
        else if (fieldType == int.class || fieldType == Integer.class) return RND.nextInt(100);
        else if (fieldType == long.class || fieldType == Long.class) return Math.abs(RND.nextLong() % 1000);
        else if (fieldType == boolean.class || fieldType == Boolean.class) return RND.nextBoolean();
        else if (fieldType == double.class || fieldType == Double.class) return RND.nextDouble();
        return null;
    }

    private String generateLongString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append((char) ('a' + (i % 26)));
        }
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    private <T> T createInstance(Class<T> type) throws Exception {
        // Try different approaches to create an instance
        try {
            // First, try no-arg constructor
            Constructor<T> ctor = type.getDeclaredConstructor();
            ctor.setAccessible(true);
            return ctor.newInstance();
        } catch (NoSuchMethodException e) {
            // If no default constructor, try to find any constructor
            Constructor<?>[] constructors = type.getDeclaredConstructors();
            if (constructors.length > 0) {
                Constructor<?> ctor = constructors[0];
                ctor.setAccessible(true);
                Class<?>[] paramTypes = ctor.getParameterTypes();
                Object[] params = new Object[paramTypes.length];
                // Fill with default values
                for (int i = 0; i < paramTypes.length; i++) {
                    params[i] = getDefaultValue(paramTypes[i]);
                }
                return (T) ctor.newInstance(params);
            }
            // Last resort: use Unsafe or throw exception
            throw new IllegalStateException("Cannot instantiate " + type.getName() + ": no suitable constructor found");
        }
    }

    private Object getDefaultValue(Class<?> type) {
        if (type.isPrimitive()) {
            if (type == boolean.class) return false;
            if (type == byte.class) return (byte) 0;
            if (type == short.class) return (short) 0;
            if (type == int.class) return 0;
            if (type == long.class) return 0L;
            if (type == float.class) return 0.0f;
            if (type == double.class) return 0.0;
            if (type == char.class) return '\0';
        }
        return null;
    }
}