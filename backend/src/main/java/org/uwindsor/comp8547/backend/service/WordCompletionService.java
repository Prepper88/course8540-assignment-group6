package org.uwindsor.comp8547.backend.service;
import org.springframework.stereotype.Service;
import org.uwindsor.comp8547.backend.algorithms.Trie;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

@Service
public class WordCompletionService {
    public ArrayList<String> getWords(String prefix) throws IOException {
        String path = "backend/src/main/resources/Dictionary/www.teksavvy.com.txt";
        String content = Files.readString(Path.of(path));
        String[] words = content.split("[\\s\\p{Punct}]+");
        Trie trie = new Trie();
        for (String word : words) {
            if (!word.isEmpty() && word.chars().allMatch(c -> c < 256)) {
                trie.insert(word);
            }
        }
        ArrayList<String> result = new ArrayList<>();
        trie.getMatchedWords(prefix, result);
        return result;
    }
}
