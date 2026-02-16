import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordReader {
    protected Map<String, Integer> word_list = new HashMap<>();
    public List<Map.Entry<String, Integer>> result_of_reading;
    public void Read(String File_Name){
        try (BufferedReader reader = new BufferedReader(new FileReader(File_Name))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] lineWords = line.split("[\\s.,!?;:\"()\\[\\]{}]+");
                for (String word : lineWords) {
                    if (!word.isEmpty()) {
                        word_list.put(word.toLowerCase(), word_list.getOrDefault(word.toLowerCase(),0) + 1);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
        WordSorter sorter = new WordSorter();
        result_of_reading = sorter.Sort(word_list);
    }
}
