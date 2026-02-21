import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Txt_WordReader extends CsvParserCli  implements IWordReader {
    @Override
    public void read(String File_Name){
        try (BufferedReader reader = new BufferedReader(new FileReader(File_Name))) {
            String line;
            while ((line = reader.readLine()) != null) {
                result_of_reading.add(line);
            }
        } catch (IOException e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }
}
