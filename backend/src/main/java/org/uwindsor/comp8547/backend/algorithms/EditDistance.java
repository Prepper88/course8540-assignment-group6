package org.uwindsor.comp8547.backend.algorithms;

//algorithm to calculate edit distance between two words
public class EditDistance {
    public int getEditDistance(String source, String target) {
        int sLen = source.length();
        int tLen = target.length();

        // Create a 2D array to store the distances
        int[][] distance = new int[sLen + 1][tLen + 1];

        for (int i = 0; i <= sLen; i++) {
            for (int j = 0; j <= tLen; j++) {
                if (i == 0) {
                    // If source is empty, distance is target
                    distance[i][j] = j;
                } else if (j == 0) {
                    // If target is empty, distance is source
                    distance[i][j] = i;
                } else {
                    char sChar = source.charAt(i - 1);
                    char tChar = target.charAt(j - 1);

                    if (sChar == tChar) {
                        //if char match
                        distance[i][j] = distance[i - 1][j - 1];
                    } else {
                        //if char not match, then insert, delete or replace
                        int insertOp = distance[i][j - 1];
                        int deleteOp = distance[i - 1][j];
                        int replaceOp = distance[i - 1][j - 1];

                        //find the minimum distances between operations
                        distance[i][j] = 1 + Math.min(insertOp, Math.min(deleteOp, replaceOp));
                    }
                }
            }
        }
        return distance[sLen][tLen];
    }

}
