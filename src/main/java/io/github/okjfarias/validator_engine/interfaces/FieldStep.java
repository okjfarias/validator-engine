package io.github.okjfarias.validator_engine.interfaces;

import java.time.LocalDateTime;
import java.util.function.Predicate;

public interface FieldStep<T> {
    FieldStep<T> required();
    FieldStep<T> maxLength(int maxLength);
    FieldStep<T> mustBeAfter(LocalDateTime date);
    FieldStep<T> custom(Predicate<T> predicate, String errorMessage);
    ValidationFinalizer<T> throwIfInvalid();
}
