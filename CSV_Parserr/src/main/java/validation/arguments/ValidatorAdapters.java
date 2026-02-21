package validation.arguments;

import validation.validators.IValidator;
import validation.exceptions.UnsupportedDataTypeException;
import validation.exceptions.ValidationException;

import java.util.Objects;

public final class ValidatorAdapters {
    private ValidatorAdapters() {
    }


    @SuppressWarnings({"rawtypes", "unchecked"})
    public static IValidator<Object> raw(IValidator<?> validator) {
        Objects.requireNonNull(validator, "validation");

        return new IValidator<>() {
            @Override
            public void validate(Object value) throws ValidationException {
                try {
                    ((IValidator) validator).validate(value);
                } catch (ClassCastException ex) {
                    String got = (value == null) ? "null" : value.getClass().getName();
                    throw new UnsupportedDataTypeException("Validator doesn't support value type: " + got);
                }
            }
        };
    }

    /**
     * Адаптирует типизированный валидатор (например IValidator<String>)
     * к "универсальному" IValidator<Object> для хранения в единой коллекции.
     */
    public static <T> IValidator<Object> typed(Class<T> type, IValidator<? super T> validator) {
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(validator, "validation");

        return new IValidator<>() {
            @Override
            public void validate(Object value) throws ValidationException {
                if (value == null) {
                    // Даем делегату решить, валиден ли null
                    validator.validate(null);
                    return;
                }
                if (!type.isInstance(value)) {
                    throw new UnsupportedDataTypeException(
                            "Expected type '" + type.getName() + "', got '" + value.getClass().getName() + "'"
                    );
                }
                validator.validate(type.cast(value));
            }
        };
    }
}
