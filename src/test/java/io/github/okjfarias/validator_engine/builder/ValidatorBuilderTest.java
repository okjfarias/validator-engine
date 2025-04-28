package io.github.okjfarias.validator_engine.builder;

import io.github.okjfarias.validator_engine.enums.ValidationMode;
import io.github.okjfarias.validator_engine.exception.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("ValidatorBuilder - Testes Unitários")
class ValidatorBuilderTest {

    @Nested
    @DisplayName("Validação de Campo Obrigatório (required)")
    class RequiredValidation {

        static Stream<String> valoresInvalidos() {
            return Stream.of(null, "", " ");
        }

        @ParameterizedTest(name = "Deve lançar exceção para valor inválido: \"{0}\"")
        @MethodSource("valoresInvalidos")
        void deveLancarExcecaoSeCampoObrigatorioInvalido(String valor) {
            ValidationException exception = assertThrows(ValidationException.class, () -> {
                ValidatorBuilder.field(valor, "Descrição", ValidationMode.IMMEDIATE)
                        .required()
                        .throwIfInvalid()
                        .done();
            });

            assertEquals(1, exception.getErrors().size());
        }
    }

    @Nested
    @DisplayName("Validação de Tamanho Máximo (maxLength)")
    class MaxLengthValidation {

        @Test
        @DisplayName("Deve lançar exceção se exceder o tamanho máximo")
        void deveLancarExcecaoSeExcederMaxLength() {
            ValidationException exception = assertThrows(ValidationException.class, () -> {
                ValidatorBuilder.field("Texto muito longo", "Descrição", ValidationMode.IMMEDIATE)
                        .maxLength(5)
                        .throwIfInvalid()
                        .done();
            });

            assertEquals(1, exception.getErrors().size());
        }

        @Test
        @DisplayName("Não deve lançar exceção se dentro do limite de tamanho")
        void naoDeveLancarExcecaoSeDentroDoLimite() {
            assertDoesNotThrow(() -> {
                ValidatorBuilder.field("Curto", "Descrição", ValidationMode.IMMEDIATE)
                        .maxLength(10)
                        .throwIfInvalid()
                        .done();
            });
        }
    }

    @Nested
    @DisplayName("Validação de Datas (mustBeAfter)")
    class MustBeAfterValidation {

        @Test
        @DisplayName("Deve lançar exceção se data for anterior à esperada")
        void deveLancarExcecaoSeDataForAnterior() {
            ValidationException exception = assertThrows(ValidationException.class, () -> {
                ValidatorBuilder.field(LocalDateTime.of(2000, 1, 1, 0, 0), "Data Vigência", ValidationMode.IMMEDIATE)
                        .mustBeAfter(LocalDateTime.now())
                        .throwIfInvalid()
                        .done();
            });

            assertEquals(1, exception.getErrors().size());
        }

        @Test
        @DisplayName("Não deve lançar exceção para data válida")
        void naoDeveLancarExcecaoParaDataValida() {
            assertDoesNotThrow(() -> {
                ValidatorBuilder.field(LocalDateTime.now().plusDays(1), "Data Vigência", ValidationMode.IMMEDIATE)
                        .mustBeAfter(LocalDateTime.now())
                        .throwIfInvalid()
                        .done();
            });
        }
    }

    @Nested
    @DisplayName("Validação Customizada (custom)")
    class CustomValidation {

        @Test
        @DisplayName("Deve lançar exceção quando predicate customizado falhar")
        void deveLancarExcecaoPredicateFalhou() {
            ValidationException exception = assertThrows(ValidationException.class, () -> {
                ValidatorBuilder.field("Texto Grande", "Descrição", ValidationMode.IMMEDIATE)
                        .custom(value -> value.length() > 5, "Tamanho inválido.")
                        .throwIfInvalid()
                        .done();
            });

            assertEquals(1, exception.getErrors().size());
        }

        @Test
        @DisplayName("Não deve lançar exceção quando predicate customizado passar")
        void naoDeveLancarExcecaoPredicatePassou() {
            assertDoesNotThrow(() -> {
                ValidatorBuilder.field("OK", "Descrição", ValidationMode.IMMEDIATE)
                        .custom(value -> value.length() > 10, "Tamanho inválido.")
                        .throwIfInvalid()
                        .done();
            });
        }
    }

    @Nested
    @DisplayName("Validação Modo AGGREGATED")
    class AggregatedModeValidation {

        @Test
        @DisplayName("Deve coletar múltiplos erros no modo AGGREGATED")
        void deveColetarMultiplosErrosAggregated() {
            ValidationException exception = assertThrows(ValidationException.class, () -> {
                ValidatorBuilder.field("    ", "Descrição", ValidationMode.AGGREGATED)
                        .required()
                        .maxLength(2)
                        .throwIfInvalid()
                        .done();
            });

            assertTrue(exception.getErrors().size() >= 2);
        }
    }

    @Nested
    @DisplayName("Casos de Sucesso")
    class SuccessCases {

        @Test
        @DisplayName("Deve passar todas as validações corretamente")
        void devePassarTodasValidacoes() {
            assertDoesNotThrow(() -> {
                ValidatorBuilder.field("Texto", "Descrição", ValidationMode.IMMEDIATE)
                        .required()
                        .maxLength(100)
                        .mustBeAfter(LocalDateTime.now().minusDays(1))
                        .custom(value -> false, "Nunca dispara.")
                        .throwIfInvalid()
                        .done();
            });
        }
    }
}
