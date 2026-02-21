import java.util.Map;
import java.util.ArrayList;

public class WordFrequencySorter extends CsvParserCli{
    protected void sort(Map<String, Integer> unsorted_map){
        result_of_sorting = new ArrayList<>(unsorted_map.entrySet());
        result_of_sorting.sort((a, b) -> b.getValue().compareTo(a.getValue()));
    }
}
