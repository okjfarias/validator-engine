package io.github.okjfarias.validator_engine.handler;

import io.github.okjfarias.validator_engine.exception.ValidationException;
import io.github.okjfarias.validator_engine.model.AggregatedValidationErrorDTO;
import io.github.okjfarias.validator_engine.utils.ValidationErrorAggregator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ValidationErrorAggregator errorAggregator;

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<AggregatedValidationErrorDTO> handleValidationException(ValidationException ex) {
        var aggregatedErrors = errorAggregator.aggregate(ex.getErrors());

        AggregatedValidationErrorDTO response = AggregatedValidationErrorDTO.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Validation failed")
                .validationErrors(aggregatedErrors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
