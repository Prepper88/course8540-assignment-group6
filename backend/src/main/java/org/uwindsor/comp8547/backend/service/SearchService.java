package org.uwindsor.comp8547.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.uwindsor.comp8547.backend.bean.SearchResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService {

    @Autowired
    private PlanSearchService planSearchService;

    @Autowired
    private RegexSearchService regexSearchService;

    @Autowired
    private TextSearchService textSearchService;

    @Autowired
    private SpellCheckService spellCheckService;

    @Autowired
    private WordCompletionService wordCompletionService;


    public SearchResponse search(String searchType, String keyword) {
        SearchResponse searchResponse = new SearchResponse();


        // Call the appropriate search service based on the search type
        switch (searchType) {
            case "Plan":
            case "plan":
                searchResponse.setPlans(planSearchService.search(searchType, keyword));
                break;
            case "Email":
            case "email":
            case "phone":
            case "Phone":
                searchResponse.setReItems(regexSearchService.search(searchType, keyword));
                break;
            case "Text":
            case "text":
                searchResponse.setTextItems(textSearchService.search(searchType, keyword));
                break;
        }

        try {
            List<String> hints;
            if (wordCompletionService.getWords(keyword).isEmpty()) {
                hints= spellCheckService.getCorrections(keyword);
            }else {
                hints=new ArrayList<String>();
            }

            searchResponse.setSpellHints(hints);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return searchResponse;
    }

    public List<String> autocomplete(String searchType, String keyword) {
        try {
            return wordCompletionService.getWords(keyword);
        } catch (IOException e) {
            e.printStackTrace();
            return List.of(); // fallback
        }
    }

}
