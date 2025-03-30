package org.uwindsor.comp8547.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.uwindsor.comp8547.backend.bean.SearchResponse;
import org.uwindsor.comp8547.backend.service.SearchService;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/search")
    public SearchResponse search(@RequestParam String searchType, @RequestParam String keyword) {
        return searchService.search(searchType, keyword);
    }


    @GetMapping("/autocomplete")
    public List<String> autocomplete(@RequestParam String searchType, @RequestParam String keyword) {
        return searchService.autocomplete(searchType, keyword);
    }



}
