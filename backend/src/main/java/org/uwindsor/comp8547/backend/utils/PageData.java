package org.uwindsor.comp8547.backend.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.uwindsor.comp8547.backend.algorithms.AVLTree;
import org.uwindsor.comp8547.backend.bean.SearchResponse;

import java.util.List;
/**
 * A class that stores the URL of a page and its corresponding AVL tree.
 * This allows repeated keyword searches without re-crawling.
 */
public class PageData {
    private String url;
    private AVLTree tree;

    public PageData(String url, AVLTree tree) {
        this.url = url;
        this.tree = tree;
    }

    public String getUrl() {
        return url;
    }

    public AVLTree getTree() {
        return tree;
    }
}