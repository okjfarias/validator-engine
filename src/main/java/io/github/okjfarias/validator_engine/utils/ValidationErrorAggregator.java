package io.github.okjfarias.validator_engine.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ValidationErrorAggregator {

    private final MessageSource messageSource;

    public Map<String, List<String>> aggregate(List<String> errorCodes) {
        Locale locale = resolveLocale();
        return errorCodes.stream()
                .collect(Collectors.groupingBy(
                        code -> classifyError(code),
                        Collectors.mapping(code -> translateMessage(code, locale), Collectors.toList())
                ));
    }

    private String translateMessage(String code, Locale locale) {
        return messageSource.getMessage(code, null, code, locale);
    }

    private Locale resolveLocale() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new IllegalStateException("Request attributes not found. Are you running outside a web context?");
        }
        HttpServletRequest request = attributes.getRequest();

        return request.getLocale();
    }

    private String classifyError(String errorCode) {
        if (errorCode.contains("obrigatorio") || errorCode.contains("required")) {
            return "requiredFields";
        }
        if (errorCode.contains("tamanho") || errorCode.contains("length")) {
            return "lengthErrors";
        }
        if (errorCode.contains("formato") || errorCode.contains("format")) {
            return "formatErrors";
        }
        if (errorCode.contains("data") || errorCode.contains("date")) {
            return "dateErrors";
        }
        return "others";
    }
}
