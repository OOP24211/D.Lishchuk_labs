import java.util.List;
import java.util.Map;

interface IWordWriter {
    void write(String FileName, List<Map.Entry<String, Integer>> word_list);
}
