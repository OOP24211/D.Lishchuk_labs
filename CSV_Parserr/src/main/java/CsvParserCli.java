import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvParserCli {
    private final IWordReader reader;
    private final LineParser lineParser;
    private final WordFrequencySorter freq_sorter;
    private final IWordWriter writer;

    protected List<String> result_of_reading;
    protected Map<String, Integer> result_of_parsing;
    protected List<Map.Entry<String, Integer>> result_of_sorting;

    public CsvParserCli() {
        reader = new Txt_WordReader();
        lineParser = new LineParser();
        freq_sorter = new WordFrequencySorter();
        writer = new Csv_WordWriter();
        result_of_parsing = new HashMap<>();
    }

    public void processing (String input_file, String output_file) {
        reader.read(input_file);
        for(String line : result_of_reading) {
            lineParser.parseLine(line);
        }
        freq_sorter.sort(result_of_parsing);
        writer.write(output_file, result_of_sorting);
    }
}
