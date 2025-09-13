package com.cleveloper.utilities.jsonschema.exception;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/** Base exception for all COSMOS utility errors. */
public abstract class JsonUtilityException extends RuntimeException {
    private final String errorCode;
    private final Map<String, Object> context = new HashMap<>();

    protected JsonUtilityException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    protected JsonUtilityException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public Map<String, Object> getContext() {
        return Collections.unmodifiableMap(context);
    }

    protected void addContext(String key, Object value) {
        this.context.put(key, value);
    }
}

