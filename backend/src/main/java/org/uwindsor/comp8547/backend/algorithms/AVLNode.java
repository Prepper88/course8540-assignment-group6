package org.uwindsor.comp8547.backend.algorithms;

public class AVLNode {
    public String key;
    public int freq;
    public int height;
    public AVLNode left, right;

    public AVLNode(String key) {
        this.key = key;
        this.freq = 1;
        this.height = 1;
    }
}