package services;

import java.util.ArrayList;
import java.util.List;

public class JsonParser {

    public static List<String> parseJsonArray(String json) {
        if (json == null || json.trim().isEmpty()) {
            return new ArrayList<>();
        }
        json = json.trim().replaceAll("^\\[|\\]$", ""); // Remove [ and ] from the ends
        if (json.isEmpty()) {
            return new ArrayList<>();
        }
        String[] items = json.split(",\\s*");
        List<String> result = new ArrayList<>();
        for (String item : items) {
            result.add(item.replaceAll("^\"|\"$", "")); // Remove surrounding double quotes
        }

        return result;
    }

    public static String toJsonArray(List<String> list) {
        if (list == null || list.isEmpty()) {
            return "[]"; 
        }

        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            String item = list.get(i)
                              .replace("\\", "\\\\")
                              .replace("\"", "\\\""); 
            json.append("\"").append(item).append("\""); 

            if (i < list.size() - 1) {
                json.append(", ");
            }
        }
        json.append("]");
        return json.toString();
    }
}
