package garbage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GarbageCreator {
    public static void main(String[] args) {
        try {
            byte[] inputBytes = Files.readAllBytes(Paths.get("example.txt"));
            long startTime = System.currentTimeMillis();

            StringBuffer outputString = new StringBuffer();
            for (byte b : inputBytes) {
                outputString.append((char) b); 
            }

            long endTime = System.currentTimeMillis();
            System.out.println("Time using StringBuffer: " + (endTime - startTime) + "ms");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


//Time using StringBuffer: 10617ms