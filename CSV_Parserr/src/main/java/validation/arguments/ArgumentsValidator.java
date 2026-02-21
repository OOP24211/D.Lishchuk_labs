package validation.arguments;

import validation.validators.IValidator;
import validation.exceptions.MissingArgumentException;
import validation.exceptions.ValidationException;

import java.util.List;
import java.util.Objects;

public final class ArgumentsValidator {
    private final ArgumentsValidationConfig config;

    public ArgumentsValidator(ArgumentsValidationConfig config) {
        this.config = Objects.requireNonNull(config, "config");
    }

    public void validate(Object... args) {
        Objects.requireNonNull(args, "args");

        for (int idx : config.indices().stream().sorted().toList()) {
            if (idx >= args.length) {
                throw new MissingArgumentException("Аргумент с индексом " + idx + " отсутствует");
            }

            validateOne(idx, args[idx]);
        }
    }

    private void validateOne(int index, Object value) {
        List<IValidator<Object>> validators = config.validatorsFor(index);
        for (IValidator<Object> validator : validators) {
            try {
                validator.validate(value);
            } catch (ValidationException ex) {
                throw new ValidationException("arg[" + index + "] validation failed: " + ex.getMessage(), ex);
            }
        }
    }
}

