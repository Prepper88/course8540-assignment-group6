package org.uwindsor.comp8547.backend.utils;

import java.util.*;

/**
 * Inverted Index implementation to replace AVLTree.
 * Maps keywords to the pages containing them along with frequency.
 */
public class InvertedIndex {

    private static final InvertedIndex instance = new InvertedIndex();
    private final Map<String, List<PageFrequency>> index = new HashMap<>();


    private InvertedIndex() {}


    public static InvertedIndex getInstance() {
        return instance;
    }

    public void add(String keyword, String url) {
        List<PageFrequency> frequencies = index.computeIfAbsent(keyword, k -> new ArrayList<>());

        for (PageFrequency pf : frequencies) {
            if (pf.getUrl().equals(url)) {
                pf.incrementFrequency();
                return;
            }
        }

        frequencies.add(new PageFrequency(1, url));
    }

    public List<PageFrequency> search(String keyword) {
        return index.getOrDefault(keyword, new ArrayList<>());
    }

    public void printIndex() {
        index.forEach((k, v) -> System.out.println("Keyword: " + k + " -> " + v));
    }

    public int getIndexSize() {
        return index.size();
    }
}