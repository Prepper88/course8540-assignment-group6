package org.uwindsor.comp8547.backend.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.uwindsor.comp8547.backend.bean.Plan;
import org.uwindsor.comp8547.backend.bean.ReItem;
import org.uwindsor.comp8547.backend.bean.TextItem;
import org.uwindsor.comp8547.backend.crawler.SeleniumCrawler;
import org.uwindsor.comp8547.backend.utils.PageData;
import org.uwindsor.comp8547.backend.utils.Parser;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.uwindsor.comp8547.backend.algorithms.ContactMethodExtractionAndValidation.*;

@Service
public class RegexSearchService {

    public List<ReItem> search(String searchType, String keyword) {
        List<ReItem> ReItemList = new ArrayList<>();
        String txtDir = "data/";
        if (!searchType.equalsIgnoreCase("email") && !searchType.equalsIgnoreCase("phone")) {
            return List.of();
        }
        else if(searchType.equalsIgnoreCase("phone")&&keyword.length()==0){
            File folder = new File(txtDir);
            File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));

            for (File file : files) {

                String filePath = file.getPath();
                System.out.println(filePath);
                if (extractPhoneNumber(filePath).size()!=0) {
                    ReItem reItem = new ReItem();
                    reItem.setName(readFirstLine(filePath));
                    reItem.setUrl(readFirstLine(filePath));
                    reItem.setResults(extractPhoneNumber(filePath));
                    ReItemList.add(reItem);
                }
            }
            return ReItemList;
        }
        else if(searchType.equalsIgnoreCase("email")&&keyword.length()==0){


            File folder = new File(txtDir);
            File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));;

            for (File file : files) {

                String filePath = file.getPath();

                if (extractEmails(filePath).size()!=0) {
                    ReItem reItem = new ReItem();
                    reItem.setName(readFirstLine(filePath));
                    reItem.setUrl(readFirstLine(filePath));
                    reItem.setResults(extractEmails(filePath));
                    ReItemList.add(reItem);
                }
            }
            return ReItemList;
        }
        else if (searchType.equalsIgnoreCase("phone")) {
            boolean flag = isValidPhoneNumber(keyword);

            if (flag){
                File folder = new File(txtDir);
                File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));;
                for (File file : files) {
                    String filePath = file.getPath();

                    if (searchKeywordInFile(file, keyword).size()!=0) {
                        ReItem reItem = new ReItem();
                        reItem.setName(readFirstLine(filePath));
                        reItem.setResults(searchKeywordInFile(file, keyword));
                        reItem.setUrl(readFirstLine(filePath));
                        ReItemList.add(reItem);
                    }
                }
                if (ReItemList.size() == 0){
                    ReItem reItem = new ReItem();
                    reItem.setResults(List.of("No phone number found"));
                    return List.of(reItem);
                }
                return ReItemList;
            }
            else {
                ReItem reItem = new ReItem();
                reItem.setResults(List.of("Invalid phone number"));
                return List.of(reItem);
            }
        }
        else if (searchType.equalsIgnoreCase("email")) {
            boolean flag = isValidEmail(keyword);
            if (flag){
                File folder = new File(txtDir);
                File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));;
                for (File file : files) {

                    String filePath = file.getPath();

                    if (searchKeywordInFile(file, keyword).size()!=0) {
                        ReItem reItem = new ReItem();
                        reItem.setName(readFirstLine(filePath));
                        reItem.setResults(searchKeywordInFile(file, keyword));
                        reItem.setUrl(readFirstLine(filePath));
                        ReItemList.add(reItem);
                    }
                }
                if (ReItemList.size() == 0){
                    ReItem reItem = new ReItem();
                    reItem.setResults(List.of("No email found"));
                    return List.of(reItem);
                }
                return ReItemList;
            }
            else {
                ReItem reItem = new ReItem();
                reItem.setResults(List.of("Invalid email address"));
                return List.of(reItem);
            }
        }
        return ReItemList;
    }
}
