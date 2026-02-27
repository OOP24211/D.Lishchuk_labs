package validation.validators;

import validation.exceptions.NotCsvException;
import validation.exceptions.NotTxtException;
import validation.exceptions.ValidationException;

public class IsTxtFileValidator implements IValidator<String> {
    @Override
    public void validate(String value) throws ValidationException {
        if (!value.toLowerCase().endsWith(".txt")) {
            throw new NotCsvException("Input file isn't .txt");
        }
    }
}
