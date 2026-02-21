package validation.validators;

import validation.exceptions.NotCsvException;
import validation.exceptions.ValidationException;

public class IsCsvFileValidator implements IValidator<String> {
    @Override
    public void validate(String value) throws ValidationException {
        if (!value.toLowerCase().endsWith(".csv")) {
            throw new NotCsvException("Output file isn't .csv");
        }
    }
}
