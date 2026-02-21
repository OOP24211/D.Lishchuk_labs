public class CsvParserCli {
    void processing (String input_file, String output_file) {
        WordReader Reader = new WordReader();
        Reader.Read(input_file);
        WordWriter Writer = new WordWriter();
        Writer.write(output_file, Reader.result_of_reading);
    }
}
