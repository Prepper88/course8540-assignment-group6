package org.uwindsor.comp8547.backend.utils;

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

    public void incrementFrequency() {
        this.frequency++;
    }

    @Override
    public String toString() {
        return frequency + " " + url;
    }
}
