package org.uwindsor.comp8547.backend.algorithms;

import org.uwindsor.comp8547.backend.utils.PageFrequency;
import java.util.Collections;
import java.util.List;

public class QuickSort {
    public static void sort(List<PageFrequency> list) {
        quickSort(list, 0, list.size() - 1);
    }

    private static void quickSort(List<PageFrequency> list, int low, int high) {
        if (low < high) {
            int pi = partition(list, low, high);
            quickSort(list, low, pi - 1);
            quickSort(list, pi + 1, high);
        }
    }

    private static int partition(List<PageFrequency> list, int low, int high) {
        int pivot = list.get(high).getFrequency();
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            if (list.get(j).getFrequency() >= pivot) {
                i++;
                Collections.swap(list, i, j);
            }
        }
        Collections.swap(list, i + 1, high);
        return i + 1;
    }
}