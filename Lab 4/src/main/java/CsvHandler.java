import java.io.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class CsvHandler {

    String CSV_FILE_NAME = "results.csv";

    CsvHandler() {
    }

    public String escapeSpecialCharacters(String data) {
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }

    private String convertToCSV(String[] data) {
        return Stream.of(data)
                .map(this::escapeSpecialCharacters)
                .collect(Collectors.joining(","));
    }

    public void saveToCsvFile(List<String[]> dataLines) throws IOException {
        FileWriter csvOutputFile = new FileWriter(CSV_FILE_NAME, true);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            dataLines.stream()
                    .map(this::convertToCSV)
                    .forEach(pw::println);
        }
    }
}
