package org.uwindsor.comp8547.backend.crawler;

import jakarta.annotation.PostConstruct;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import java.io.*;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.*;


public class SeleniumCrawler {
    private static Set<String> visited = new HashSet<>();


    public static void crawlAndSave(String url, int depth) {
        if (depth == 0 || visited.contains(url)) return;
        visited.add(url);

        try {
            url = validateAndFixUrl(url, url);
            if (url == null) return;

            Document doc = Jsoup.connect(url).timeout(30000).get();
            String pageText = doc.text();

            // Save page content to a txt file
            saveToFile(url, pageText);

            // Extract links and recursively crawl
            Elements links = doc.select("a[href]");
            int count = 0;
            for (Element link : links) {
                if (count++ >= 15) break;
                String absUrl = validateAndFixUrl(url, link.attr("href"));
                if (absUrl != null && !visited.contains(absUrl)) {
                    crawlAndSave(absUrl, depth - 1);
                }
            }
        } catch (IOException e) {
            System.out.println("Error while processing URL: " + url + " - " + e.getMessage());
        }
    }

    private static void saveToFile(String url, String content) throws IOException {
        File dir = new File("data");
        if (!dir.exists()) dir.mkdir();

        String filename = "data/" + url.replaceAll("[^a-zA-Z0-9]", "_") + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("URL: " + url + "\n\n");
            writer.write(content);
            System.out.println("Saved: " + filename);
        }
    }

    private static String validateAndFixUrl(String baseUrl, String url) {
        try {
            if (url == null || url.isEmpty()) return null;
            url = url.trim().replaceAll("[\n\t\r]+", "");

            if (!url.matches("^(http|https)://.*")) {
                URL base = new URL(baseUrl);
                url = new URL(base, url).toString();
            }

            new URL(url); // Validate URL
            return url;
        } catch (MalformedURLException e) {
            System.out.println("Invalid URL format: " + url);
            return null;
        }
    }
}