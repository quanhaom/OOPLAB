package garbage;

import java.io.FileWriter;
import java.io.IOException;

public class GenerateExampleFile {
    public static void main(String[] args) {
        try (FileWriter writer = new FileWriter("example.txt")) {
            for (int i = 0; i < 10000000; i++) {
                writer.write("Line " + i + ": This is an example of a large text file.\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
