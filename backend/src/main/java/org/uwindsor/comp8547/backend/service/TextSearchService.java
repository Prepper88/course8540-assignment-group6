package org.uwindsor.comp8547.backend.service;

import org.springframework.stereotype.Service;
import org.uwindsor.comp8547.backend.utils.InvertedIndex;
import org.uwindsor.comp8547.backend.utils.PageFrequency;
import org.uwindsor.comp8547.backend.utils.Parser;
import org.uwindsor.comp8547.backend.crawler.SeleniumCrawler;
import org.uwindsor.comp8547.backend.bean.TextItem;
import jakarta.annotation.PostConstruct;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class TextSearchService {

    private static final InvertedIndex index = InvertedIndex.getInstance();

    @PostConstruct
    public void runAfterStartup() {

        String[] urls = {"https://oxio.ca/en","https://www.rogers.com/","https://www.teksavvy.com/"};  //input websites to be crawled

        for (String url : urls) {
            SeleniumCrawler.crawlAndSave(url.trim(), 3);
        }
       
        File folder = new File("data/");
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));

        if (files != null) {
            for (File file : files) {
                Parser.parseFile(file);
                System.out.println("Loaded and indexed file: " + file.getName());
            }
        }

    }

    public List<TextItem> search(String searchType, String keyword) {
        if (!searchType.equalsIgnoreCase("text")) {
            return List.of();
        }

        String targetKeyword = keyword.trim().toLowerCase();
        List<PageFrequency> results = index.search(targetKeyword);

        results.sort((a, b) -> Integer.compare(b.getFrequency(), a.getFrequency()));

        List<TextItem> textItemList = new ArrayList<>();

        for (PageFrequency result : results) {

            TextItem textItem = new TextItem();

            textItem.setKeyword(keyword);
            textItem.setUrl(result.getUrl());
            textItem.setOccurrences(result.getFrequency());

            textItemList.add(textItem);

        }

        return textItemList;
    }
}
