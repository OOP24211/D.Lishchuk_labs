public class LineParser extends CsvParserCli {
    private static final String REGULAR_EXPRESSION_OF_SEPARATING_CHARACTERS = "[\\s.,!?;:\"()\\[\\]{}]+";
    protected void parseLine(String line) {
        String[] lineWords = line.split(REGULAR_EXPRESSION_OF_SEPARATING_CHARACTERS);
        for (String word : lineWords) {
            if (!word.isEmpty()) {
                result_of_parsing.put(word.toLowerCase(), result_of_parsing.getOrDefault(word.toLowerCase(),0) + 1);
            }
        }
    }
}
