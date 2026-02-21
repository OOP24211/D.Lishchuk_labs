import validation.validators.NotEmptyValidator;
import validation.validators.IsCsvFileValidator;
import validation.validators.IsTxtFileValidator;

import validation.arguments.ArgumentsValidationConfig;
import validation.arguments.ArgumentsValidator;

public class Main {
    public static void main(String[] args) {
        var config = ArgumentsValidationConfig.builder()
                .arg(0, new NotEmptyValidator())
                .arg(1, new NotEmptyValidator())
                .arg(0, new IsTxtFileValidator())
                .arg(1, new IsCsvFileValidator())
                .build();
        new ArgumentsValidator(config).validate((Object[]) args);

        CsvParserCli csvParserCli = new CsvParserCli();
        csvParserCli.processing(args[0], args[1]);
    }
}
