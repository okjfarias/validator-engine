package io.github.okjfarias.validator_engine.model;

public class ValidationError {
    private final String message;

    public ValidationError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}