package org.uwindsor.comp8547.backend.algorithms;

import java.util.ArrayList;
import java.util.List;

/**
 * A MaxHeap (Max Priority Queue) implementation in Java.
 * Provides basic heap operations like insert, extractMax, and peek.
 */
public class MaxHeap<T extends Comparable<T>> {
    private List<T> heap;

    /**
     * Constructor to initialize an empty MaxHeap.
     */
    public MaxHeap() {
        heap = new ArrayList<>();
    }

    /**
     * Inserts a new element into the heap.
     * @param value The value to be inserted.
     */
    public void insert(T value) {
        heap.add(value);
        siftUp(heap.size() - 1);
    }

    /**
     * Returns the maximum value (root of the heap) without removing it.
     * @return The maximum value in the heap, or null if the heap is empty.
     */
    public T peek() {
        return heap.isEmpty() ? null : heap.get(0);
    }

    /**
     * Extracts and returns the maximum value (root of the heap).
     * @return The maximum value in the heap, or null if the heap is empty.
     */
    public T extractMax() {
        if (heap.isEmpty()) return null;
        if (heap.size() == 1) return heap.remove(0);

        T max = heap.get(0);
        heap.set(0, heap.remove(heap.size() - 1));
        siftDown(0);
        return max;
    }

    /**
     * Sifts up the element at the given index to maintain heap property.
     * @param index The index of the element to sift up.
     */
    private void siftUp(int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            if (heap.get(index).compareTo(heap.get(parentIndex)) <= 0) break;
            swap(index, parentIndex);
            index = parentIndex;
        }
    }

    /**
     * Sifts down the element at the given index to maintain heap property.
     * @param index The index of the element to sift down.
     */
    private void siftDown(int index) {
        int leftChild, rightChild, largest;
        while (index < heap.size() / 2) { // While index has at least one child
            leftChild = 2 * index + 1;
            rightChild = 2 * index + 2;
            largest = index;

            if (leftChild < heap.size() && heap.get(leftChild).compareTo(heap.get(largest)) > 0) {
                largest = leftChild;
            }

            if (rightChild < heap.size() && heap.get(rightChild).compareTo(heap.get(largest)) > 0) {
                largest = rightChild;
            }

            if (largest == index) break;

            swap(index, largest);
            index = largest;
        }
    }

    /**
     * Swaps two elements in the heap.
     * @param i Index of the first element.
     * @param j Index of the second element.
     */
    private void swap(int i, int j) {
        T temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    /**
     * Checks if the heap is empty.
     * @return true if the heap is empty, false otherwise.
     */
    public boolean isEmpty() {
        return heap.isEmpty();
    }

    /**
     * Returns the size of the heap.
     * @return The number of elements in the heap.
     */
    public int size() {
        return heap.size();
    }

    /**
     * Returns the heap elements as a list (for testing or debugging purposes).
     * @return The list representing the heap.
     */
    public List<T> getHeap() {
        return heap;
    }

    /**
     * Main method for testing the MaxHeap implementation.
     */
    public static void main(String[] args) {
        MaxHeap<Integer> maxHeap = new MaxHeap<>();
        maxHeap.insert(10);
        maxHeap.insert(40);
        maxHeap.insert(30);
        maxHeap.insert(5);
        maxHeap.insert(12);

        System.out.println("Heap elements: " + maxHeap.getHeap());
        System.out.println("Max element: " + maxHeap.extractMax());
        System.out.println("Heap after extracting max: " + maxHeap.getHeap());
    }
}