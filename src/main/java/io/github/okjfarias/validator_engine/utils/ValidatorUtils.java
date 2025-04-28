package io.github.okjfarias.validator_engine.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidatorUtils {

    public boolean isFilled(String value) {
        return value != null && !value.isBlank();
    }

    public boolean isBlankOrNull(String value) {
        return value == null || value.isBlank();
    }

    public boolean exceedsLength(String value, int maxLength) {
        return value != null && value.length() > maxLength;
    }
}
