package org.uwindsor.comp8547.backend.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.uwindsor.comp8547.backend.algorithms.AVLTree;
import org.uwindsor.comp8547.backend.bean.SearchResponse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Parser {
    public static PageData parseFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String url = reader.readLine().replace("URL: ", "");
            StringBuilder content = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                content.append(line).append(" ");
            }

            // Parse content and build AVL tree
            AVLTree tree = parseKeywords(content.toString());
            return new PageData(url, tree);
        } catch (IOException e) {
            System.out.println("Error reading file: " + file.getName());
            return null;
        }
    }

    public static AVLTree parseKeywords(String text) {
        AVLTree tree = new AVLTree();
        for (String word : text.toLowerCase().split("\\W+")) {
            if (!word.isEmpty()) tree.insert(word);
        }
        return tree;
    }
}
