# Validator Engine

![Java CI](https://github.com/okjfarias/validator-engine/actions/workflows/ci.yml/badge.svg)

Fluent Validation Engine with **i18n support**, **Validation Aggregation** and **Type-Safe Fluent API** for Spring Boot 3.x applications.

---

## Features

- Fluent API for field validation
- Validation Modes: Immediate or Aggregated
- Method Chaining with safety (required → maxLength → mustBeAfter → custom → throwIfInvalid → done)
- Internationalization (i18n) ready
- Spring Boot 3.x compatible
- 100% JUnit 5 coverage with clear test organization

---

## Project Structure

```bash
src/main/java/io/github/okjfarias/validator_engine/
├── builder/           # ValidatorBuilder (core)
├── config/            # MessageSource Spring Boot configuration
├── enums/             # ValidationMode (IMMEDIATE, AGGREGATED)
├── exception/         # ValidationException
├── handler/           # GlobalExceptionHandler
├── interfaces/        # Fluent API stage interfaces
├── model/             # ValidationError DTO
└── utils/             # Aggregator and Validator Utils
resources/
├── messages.properties
└── messages_en.properties
```

---

## How ValidatorBuilder Works

The ValidatorBuilder is based on **three core parameters**:

| Parameter | Purpose |
|:----------|:--------|
| `value` | The actual data you want to validate (e.g., a String, Date, Number) |
| `fieldName` | Human-friendly description used in error messages |
| `validationMode` | How the engine will behave (fail fast or aggregate errors) |

---

## Parameters Explained

| Argument in field() | Example | Meaning |
|:--------------------|:--------|:--------|
| `value` | `"João Farias"` | Real content to validate |
| `fieldName` | `"Nome Completo"` | Label used in error messages |
| `ValidationMode` | `ValidationMode.IMMEDIATE` | Fail on first error (or collect errors with AGGREGATED mode) |

Example Call:

```java
ValidatorBuilder.field("Texto", "Descrição", ValidationMode.IMMEDIATE)
    .required()
    .maxLength(300)
    .mustBeAfter(LocalDateTime.now().minusDays(1))
    .custom(value -> value.contains("x"), "Não pode conter 'x'.")
    .throwIfInvalid()
    .done();
```

This means:

- **"Texto"** is the actual value to validate.
- **"Descrição"** is used to generate user-friendly error messages.
- **Immediate Mode** means validation stops at the first error found.

---

## Example: Validating a UserDTO

Imagine you have a simple DTO:

```java
@Data
public class UserDTO {
    private String name;
    private String email;
    private LocalDateTime birthDate;
}
```

You can validate it easily with `ValidatorBuilder`:

```java
UserDTO user = new UserDTO();
user.setName("");
user.setEmail("email@invalid");
user.setBirthDate(LocalDateTime.of(1990, 1, 1, 0, 0));

ValidatorBuilder.field(user.getName(), "Nome", ValidationMode.AGGREGATED)
    .required()
    .maxLength(50)
    .throwIfInvalid()
    .done();

ValidatorBuilder.field(user.getEmail(), "Email", ValidationMode.AGGREGATED)
    .required()
    .custom(email -> !email.contains("@"), "Formato de email inválido.")
    .throwIfInvalid()
    .done();

ValidatorBuilder.field(user.getBirthDate(), "Data de Nascimento", ValidationMode.AGGREGATED)
    .mustBeAfter(LocalDateTime.of(1900, 1, 1, 0, 0))
    .throwIfInvalid()
    .done();
```

If errors are found:
- In Immediate Mode → stops at first error
- In Aggregated Mode → collects all errors to report together

---

## Internationalization (i18n)

**Supports dynamic translation** based on HTTP `Accept-Language` header:

- `messages.properties` → Default (Portuguese)
- `messages_en.properties` → English fallback

Example Message Key:
```properties
validacao.campo.obrigatorio=O campo {0} é obrigatório.
```

All error codes can be translated automatically.

---

## Running Tests

```bash
mvn clean test
```

- 100% coverage with `JUnit 5`
- Organized with `@Nested` classes and `@DisplayName`
- CI/CD ready with GitHub Actions

---

## License

This project is licensed under the [MIT License](LICENSE).

---

## Future Improvements

- Add built-in email, CPF, and CNPJ validators
- Add field group validation (ex: nested object validation)

---

## Developer

**João Farias**  
[GitHub Profile](https://github.com/okjfarias)

---