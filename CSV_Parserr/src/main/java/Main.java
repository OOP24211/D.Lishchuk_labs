public class Main {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Формат ввода: input.txt output.csv");
            System.exit(1);
        }

        CsvParserCli csvParserCli = new CsvParserCli();
        csvParserCli.processing(args[0], args[1]);
    }
}
