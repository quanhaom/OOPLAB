package services;

import java.util.ArrayList;
import java.util.List;

public class JsonParser {

    public static List<String> parseJsonArray(String json) {
        // Sanitize the input
        if (json == null || json.trim().isEmpty()) {
            return new ArrayList<>();
        }

        // Trim square brackets and clean extra spaces
        json = json.trim().replaceAll("^\\[|\\]$", ""); // Remove [ and ] from the ends

        // Handle empty array
        if (json.isEmpty()) {
            return new ArrayList<>();
        }

        // Split by commas and trim each element
        String[] items = json.split(",\\s*");
        List<String> result = new ArrayList<>();
        for (String item : items) {
            result.add(item.replaceAll("^\"|\"$", "")); // Remove surrounding double quotes
        }

        return result;
    }

    public static String toJsonArray(List<String> list) {
        if (list == null || list.isEmpty()) {
            return "[]"; // Return an empty JSON array
        }

        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            String item = list.get(i)
                              .replace("\\", "\\\\") // Escape backslashes
                              .replace("\"", "\\\""); // Escape double quotes
            json.append("\"").append(item).append("\""); // Add quotes around each string

            if (i < list.size() - 1) {
                json.append(", "); // Add a comma between elements
            }
        }
        json.append("]");
        return json.toString();
    }
}
