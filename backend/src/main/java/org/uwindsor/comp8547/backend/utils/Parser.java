package org.uwindsor.comp8547.backend.utils;

import java.io.*;

public class Parser {
    private static final InvertedIndex index = InvertedIndex.getInstance();

    public static void parseFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String url = reader.readLine().replace("URL: ", "");
            StringBuilder content = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                content.append(line).append(" ");
            }

            parseKeywords(content.toString(), url);
        } catch (IOException e) {
            System.out.println("Error reading file: " + file.getName());
        }
    }

    private static void parseKeywords(String text, String url) {
        for (String word : text.toLowerCase().split("\\W+")) {
            if (!word.isEmpty()) {

                index.add(word, url);

            }
        }
    }
}
