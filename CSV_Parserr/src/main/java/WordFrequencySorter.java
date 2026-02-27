import java.util.List;
import java.util.Map;

public class WordFrequencySorter{
    protected void sort(List<Map.Entry<String, Integer>> result_of_sorting){
        result_of_sorting.sort((a, b) -> b.getValue().compareTo(a.getValue()));
    }
}
