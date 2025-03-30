package org.uwindsor.comp8547.backend.service;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.uwindsor.comp8547.backend.algorithms.QuickSort;
import org.uwindsor.comp8547.backend.bean.SearchResponse;
import org.uwindsor.comp8547.backend.bean.TextItem;
import org.uwindsor.comp8547.backend.crawler.SeleniumCrawler;
import org.uwindsor.comp8547.backend.utils.PageData;
import org.uwindsor.comp8547.backend.utils.PageFrequency;
import org.uwindsor.comp8547.backend.utils.Parser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class TextSearchService {

    private final List<PageData> pageDataList = new ArrayList<>();

    @PostConstruct
    public void runAfterStartup() {
        String[] urls = {"https://oxio.ca/en","https://www.rogers.com/"};  //input websites to be crawled
        for (String url : urls) {
            SeleniumCrawler.crawlAndSave(url.trim(), 3);
        }

        File folder = new File("data/");
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));

        if (files != null) {
            for (File file : files) {
                PageData pageData = Parser.parseFile(file);
                pageDataList.add(pageData);
                System.out.println("Loaded file: " + file.getName());
            }
        }
    }



    public List<TextItem> search(String searchType, String keyword) {
        if (!searchType.equalsIgnoreCase("text")) {
            return List.of();
        }

        String targetKeyword = keyword.trim().toLowerCase();

        List<PageFrequency> results = new ArrayList<>();

        File folder = new File("data/");
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));

        if (pageDataList != null) {
            for (PageData pageData : pageDataList) {
                int freq = pageData.getTree().search(targetKeyword);
                if (freq > 0) {
                    results.add(new PageFrequency(freq, pageData.getUrl()));
                }
            }
        }
        QuickSort.sort(results);

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
