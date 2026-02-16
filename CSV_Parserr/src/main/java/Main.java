public class Main {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Формат ввода: input.txt output.csv");
            System.exit(1);
        }
        WordReader Reader = new WordReader();
        Reader.Read(args[0]);
        WordWriter Writer = new WordWriter();
        Writer.write(args[1], Reader.result_of_reading);
    }
}
