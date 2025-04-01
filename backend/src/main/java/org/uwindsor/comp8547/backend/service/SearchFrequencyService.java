package org.uwindsor.comp8547.backend.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Service
public class SearchFrequencyService {
    private static final String FILE_PATH = "src/main/resources/Dictionary/search-freq.json";
    private final Gson gson = new Gson();

    //load a file into the hash map, put the new searched word into the hash map, save the hash map back to the file, and finally return the previous word frequency.
    public synchronized int incrementKeyword(String keyword) throws IOException {
        Map<String, Integer> stats = loadStats();
        String key = keyword.toLowerCase();
        int previousCount = stats.getOrDefault(key, 0);
        stats.put(key, previousCount + 1);
        saveStats(stats);
        return previousCount;//return the previous word frequency.
    }

    public synchronized int getKeywordCount(String keyword) throws IOException {
        Map<String, Integer> stats = loadStats();
        return stats.getOrDefault(keyword.toLowerCase(), 0);
    }

    //load a file into the hash map
    private Map<String, Integer> loadStats() throws IOException {
        try (Reader reader = new FileReader(FILE_PATH)) {
            Type type = new TypeToken<Map<String, Integer>>(){}.getType();
            return gson.fromJson(reader, type);
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    // save the hash map back to the file
    private void saveStats(Map<String, Integer> stats) throws IOException {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(stats, writer);
        }
    }
}

