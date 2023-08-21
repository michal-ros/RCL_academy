import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileWriterHelper {

    /**
     * Write a list of items to a file.
     *
     * @param fileName name of the file.
     * @param items list of items.
     * @param formatter function to format each item.
     * @param <T> type of the items.
     */
    public static <T> void writeToFile(String fileName, List<T> items, Function<T, String> formatter) {
        List<String> formattedItems = items.stream()
                .map(formatter)
                .collect(Collectors.toList());

        try {
            Files.write(Paths.get(fileName), formattedItems);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
