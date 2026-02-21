import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvParserCli {
    private final WordReader reader;
    private final WordWriter writer;
    private final WordSorter sorter;
    private final LineParser lineParser;

    protected List<String> result_of_reading;
    protected Map<String, Integer> result_of_parsing;
    protected List<Map.Entry<String, Integer>> result_of_sorting;

    public CsvParserCli() {
        reader = new WordReader();
        lineParser = new LineParser();
        writer = new WordWriter();
        sorter = new WordSorter();

        result_of_parsing = new HashMap<>();
    }

    public void processing (String input_file, String output_file) {
        reader.read(input_file);
        for(String line : result_of_reading) {
            lineParser.parseLine(line);
        }
        sorter.sort(result_of_parsing);
        writer.write(output_file, result_of_sorting);
    }
}
