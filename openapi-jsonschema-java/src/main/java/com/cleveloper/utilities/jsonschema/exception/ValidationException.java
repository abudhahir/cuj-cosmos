package com.cleveloper.utilities.jsonschema.exception;

import java.util.List;

/** Exception thrown when validation fails. */
public class ValidationException extends JsonUtilityException {
    private final List<String> errors;

    public ValidationException(String message, List<String> errors) {
        super(message, "VAL_001");
        this.errors = List.copyOf(errors);
        addContext("errorCount", errors.size());
    }

    public List<String> getErrors() {
        return errors;
    }
}

