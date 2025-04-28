# Validator Engine

**Fluent Validation Engine** with **Internationalization (i18n)**, **Validation Aggregation** and **Type-Safe Fluent API** for Spring Boot applications.

---

## Features

- Fluent API for field validation
- Validation Modes:
  - **IMMEDIATE** (fail fast)
  - **AGGREGATED** (collect multiple errors)
- Method Chaining (required → maxLength → mustBeAfter → throwIfInvalid → done)
- Internationalization ready (messages.properties)
- Compatible with Jakarta EE (Spring Boot 3.x)

---

## Project Structure

```bash
src/main/java/
├── builder/           # ValidatorBuilder
├── config/            # Spring MessageSourceConfig
├── enums/             # ValidationMode (IMMEDIATE, AGGREGATED)
├── exception/         # ValidationException
├── handler/           # GlobalExceptionHandler
├── interfaces/        # Fluent API interfaces
├── model/             # ValidationError DTOs
├── utils/             # ValidationErrorAggregator, ValidatorUtils
resources/
├── messages.properties
├── messages_en.properties
