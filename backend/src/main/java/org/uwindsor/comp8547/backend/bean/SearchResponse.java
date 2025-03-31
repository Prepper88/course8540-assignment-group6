package org.uwindsor.comp8547.backend.bean;

import lombok.Data;

import java.util.List;

@Data
public class SearchResponse {
    private List<String> spellHints;
    private List<Plan> plans;
    private List<ReItem> reItems;
    private List<TextItem> textItems;
    private int searchCount;
}
