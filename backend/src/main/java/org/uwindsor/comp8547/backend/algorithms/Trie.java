package org.uwindsor.comp8547.backend.algorithms;
import java.util.ArrayList;

public class Trie {
    //node of the trie, containing an array of its children, containing all characters
    static class TrieNode {
        TrieNode[] children = new TrieNode[256];
        Boolean wordEnd = false;    //determine whether the node is the last character of a word
    }
    TrieNode root;

    //insert a word to the trie
    public void insert(String word) {
        //if no root, create one
        if (root == null) {
            root = new TrieNode();
        }
        TrieNode current = root;

        //iterate each character in the word
        for (char c : word.toCharArray()) {
            //if the char not exist in the trie, create a new node on the corresponding location
            if (current.children[c] == null) {
                current.children[c] = new TrieNode();
            }
            //go deeper
            current = current.children[c];
        }
        //after insert the whole word, turn the end of word flag to true
        current.wordEnd = true;
    }

    //retrieve the node of the last char in a string
    public TrieNode getLastNode(String prefix){
        TrieNode current = root;
        //iterate the string
        for (char c : prefix.toCharArray()) {
            //if not found, stop and return null
            if (current.children[c] == null) {
                return null;
            }
            //go deeper
            current = current.children[c];
        }
        return current;
    }


    //get all words below a node
    public void getWords(TrieNode node, ArrayList<String> words, StringBuilder sb) {
        //return if the node not exist
        if(node==null){
            return;
        }
        //add a complete word to the list if meet the end of word flag
        if(node.wordEnd){
            words.add(sb.toString());
        }
        //iterate all characters
        for(char i=0;i<256;i++){
            //append current char
            sb.append(i);
            getWords(node.children[i], words, sb);
            //remove the last char
            sb.deleteCharAt(sb.length()-1);
        }

    }

    //get all words starting with the provided prefix and save them to an arraylist
    public void getMatchedWords(String prefix, ArrayList<String> words) {
        //get the node of the last char in the prefix
        TrieNode lastNode = getLastNode(prefix);
        StringBuilder sb = new StringBuilder(prefix);
        //get all words below the "lastNode"
        getWords(lastNode,words, sb);
    }

}
