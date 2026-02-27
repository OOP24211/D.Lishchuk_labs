package validation.exceptions;

public class MissingArgumentException extends ValidationException {
    public MissingArgumentException(String message) {
        super(message);
    }
}
