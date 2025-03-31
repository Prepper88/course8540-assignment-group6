package org.uwindsor.comp8547.backend.service;

import org.springframework.stereotype.Service;
import org.uwindsor.comp8547.backend.algorithms.EditDistance;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SpellCheckService {

    public ArrayList<String> getCorrections(String word) throws IOException {
        String path = "data/www.teksavvy.com.txt";
        String content = Files.readString(Path.of(path));
        String[] words = content.split("[\\s\\p{Punct}]+");
        EditDistance editDistance = new EditDistance();
        Map<String, Integer> map = new HashMap<>();
        for(String w: words){
            int dist = editDistance.getEditDistance(word, w);
            map.put(w, dist);
        }
        ArrayList<String> results = new ArrayList<>();

        //get three closest words
        String minKey = "";
        for(int i = 0; i < 3; i++){
            int minValue=5;
            for(String w: map.keySet()){
                if(map.get(w)<minValue){
                    minKey = w;
                    minValue = map.get(w);
                }
            }
            if(!minKey.isEmpty()){
                results.add(minKey);
                map.remove(minKey);
            }

        }
        return results;
    }
}
