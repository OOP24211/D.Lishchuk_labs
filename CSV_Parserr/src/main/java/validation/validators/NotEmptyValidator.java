package validation.validators;

import validation.exceptions.NotTxtException;
import validation.exceptions.ValidationException;

public final class NotEmptyValidator implements IValidator<Object> {
    @Override
    public void validate(Object value) throws ValidationException {
        if (value == null) {
            throw new NotTxtException("Значение не должно быть null. Формат ввода: CSV_Parser.exe input.txt output.csv");
        }
    }
}
