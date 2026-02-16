import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class WordSorter {
    public List<Map.Entry<String, Integer>> Sort(Map<String, Integer> unsorted_map){
        List<Map.Entry<String, Integer>> sorted_map = new ArrayList<>(unsorted_map.entrySet());
        sorted_map.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        return sorted_map;
    }
}
