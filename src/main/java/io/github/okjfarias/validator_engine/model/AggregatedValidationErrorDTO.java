package io.github.okjfarias.validator_engine.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
public class AggregatedValidationErrorDTO {

    private int status;
    private String message;
    private Map<String, List<String>> validationErrors;

}
