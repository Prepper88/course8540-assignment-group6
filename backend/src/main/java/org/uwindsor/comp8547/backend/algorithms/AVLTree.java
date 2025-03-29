package org.uwindsor.comp8547.backend.algorithms;

public class AVLTree {
    private AVLNode root;

    public void insert(String key) {
        root = insert(root, key);
    }

    private AVLNode insert(AVLNode node, String key) {
        if (node == null) return new AVLNode(key);
        if (key.equals(node.key)) node.freq++;
        else if (key.compareTo(node.key) < 0) node.left = insert(node.left, key);
        else node.right = insert(node.right, key);

        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
        return balance(node, key);
    }

    private AVLNode balance(AVLNode node, String key) {
        int balance = getBalance(node);
        if (balance > 1 && key.compareTo(node.left.key) < 0) return rightRotate(node);
        if (balance < -1 && key.compareTo(node.right.key) > 0) return leftRotate(node);
        if (balance > 1 && key.compareTo(node.left.key) > 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
        if (balance < -1 && key.compareTo(node.right.key) < 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }
        return node;
    }

    private int getHeight(AVLNode node) {
        return node == null ? 0 : node.height;
    }

    private int getBalance(AVLNode node) {
        return node == null ? 0 : getHeight(node.left) - getHeight(node.right);
    }

    private AVLNode rightRotate(AVLNode y) {
        AVLNode x = y.left;
        AVLNode T2 = x.right;
        x.right = y;
        y.left = T2;
        y.height = 1 + Math.max(getHeight(y.left), getHeight(y.right));
        x.height = 1 + Math.max(getHeight(x.left), getHeight(x.right));
        return x;
    }

    private AVLNode leftRotate(AVLNode x) {
        AVLNode y = x.right;
        AVLNode T2 = y.left;
        y.left = x;
        x.right = T2;
        x.height = 1 + Math.max(getHeight(x.left), getHeight(x.right));
        y.height = 1 + Math.max(getHeight(y.left), getHeight(y.right));
        return y;
    }

    public int search(String key) {
        return search(root, key);
    }

    private int search(AVLNode node, String key) {
        if (node == null) return 0;
        if (key.equals(node.key)) return node.freq;
        return key.compareTo(node.key) < 0 ? search(node.left, key) : search(node.right, key);
    }
}