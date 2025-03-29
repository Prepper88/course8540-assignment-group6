package org.uwindsor.comp8547.backend.utils;

/**
 * A simple data class to store the frequency of a keyword and the corresponding page URL.
 */
public class PageFrequency {
    private int frequency;
    private String url;

    public PageFrequency(int frequency, String url) {
        this.frequency = frequency;
        this.url = url;
    }

    public int getFrequency() {
        return frequency;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return frequency + " " + url;
    }
}