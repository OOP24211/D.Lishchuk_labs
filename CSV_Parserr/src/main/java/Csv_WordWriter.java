import java.nio.file.Path;
import java.util.Map;
import java.util.List;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.io.IOException;

public class Csv_WordWriter implements IWordWriter {
    @Override
    public void write(String FileName, List<Map.Entry<String, Integer>> word_list){
        Path path = Path.of(FileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (Map.Entry<String, Integer> entry : word_list) {
                writer.write(entry.getKey() + "," + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }
}
