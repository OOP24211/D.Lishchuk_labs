import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Txt_WordReader  implements IWordReader {
    @Override
    public void read(String File_Name, List<String> result){
        try (BufferedReader reader = new BufferedReader(new FileReader(File_Name))) {
            String line;
            while ((line = reader.readLine()) != null) {
                result.add(line);
            }
        } catch (IOException e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }
}
