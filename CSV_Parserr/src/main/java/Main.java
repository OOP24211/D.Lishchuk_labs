public class Main {
    public static void main(String[] args) {
        if  (args.length > 2) {
            System.out.printf("Too many arguments: %s");
            System.exit(1);
        }

        CsvParserCli csvParserCli = new CsvParserCli((Object[]) args);
        csvParserCli.processing(args[0], args[1]);
    }
}
