package io.github.okjfarias.validator_engine.builder;

import io.github.okjfarias.validator_engine.enums.ValidationMode;
import io.github.okjfarias.validator_engine.exception.ValidationException;
import io.github.okjfarias.validator_engine.interfaces.DoneStep;
import io.github.okjfarias.validator_engine.interfaces.FieldStep;
import io.github.okjfarias.validator_engine.interfaces.ValidationFinalizer;
import io.github.okjfarias.validator_engine.model.ValidationError;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ValidatorBuilder<T> implements FieldStep<T>, ValidationFinalizer<T>, DoneStep {

    private final T value;
    private final String fieldName;
    private final ValidationMode mode;
    private final List<ValidationError> errors = new ArrayList<>();

    private ValidatorBuilder(T value, String fieldName, ValidationMode mode) {
        this.value = value;
        this.fieldName = fieldName;
        this.mode = mode;
    }

    public static <T> FieldStep<T> field(T value, String fieldName, ValidationMode mode) {
        return new ValidatorBuilder<>(value, fieldName, mode);
    }

    @Override
    public FieldStep<T> required() {
        if (value == null || (value instanceof String str && str.isBlank())) {
            handleError(fieldName + " é obrigatório.");
        }
        return this;
    }

    @Override
    public FieldStep<T> maxLength(int maxLength) {
        if (value instanceof String str && str.length() > maxLength) {
            handleError(fieldName + " deve ter no máximo " + maxLength + " caracteres.");
        }
        return this;
    }

    @Override
    public FieldStep<T> mustBeAfter(LocalDateTime date) {
        if (value instanceof LocalDateTime dateTime && dateTime.isBefore(date)) {
            handleError(fieldName + " deve ser maior ou igual a " + date.toLocalDate() + ".");
        }
        return this;
    }

    @Override
    public FieldStep<T> custom(Predicate<T> predicate, String errorMessage) {
        if (value != null && predicate.test(value)) {
            handleError(errorMessage);
        }
        return this;
    }

    @Override
    public ValidationFinalizer<T> throwIfInvalid() {
        if (!errors.isEmpty()) {
            List<String> mensagens = errors.stream()
                    .map(ValidationError::getMessage)
                    .toList();
            throw new ValidationException(mensagens);
        }
        return this;
    }

    @Override
    public DoneStep done() {
        return this;
    }

    private void handleError(String message) {
        if (mode == ValidationMode.IMMEDIATE) {
            throw new ValidationException(List.of(message));
        }
        errors.add(new ValidationError(message));
    }
}
