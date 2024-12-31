package strmatch.virus.algo;

import strmatch.util.BitLoader;
import strmatch.util.bitmap.*;
import strmatch.virus.ScanResult;
import strmatch.virus.SimpleScanResult;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class AhoCorasick implements BinAlgo {

    private static class Node {
        Map<Boolean, Node> children = new HashMap<>();
        Node fail;
        List<String> matchedPatterns = new ArrayList<>();
    }

    private final Node root;

    public AhoCorasick() {
        root = new Node();
    }

    @Override
    public void initPattern(String virus, Path file) throws IOException {
        Bitmap pattern = new SimpleBitmap();
        BitLoader.readFileAsBits(pattern, file);
        addPattern(virus, pattern);
    }

    @Override
    public void initAlgo() {
        buildFailLinks();
    }

    @Override
    public ScanResult match(Path file) {
        ScanResult result = new SimpleScanResult();
        Bitmap str = new SimpleBitmap();

        try {
            BitLoader.readFileAsBits(str, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        search(str, file.toString(), result);
        return result;
    }

    /**
     * Adds a pattern to the Trie.
     */
    private void addPattern(String virus, Bitmap pattern) {
        Node current = root;
        for (int i = 0; i < pattern.length(); i++) {
            boolean bit = pattern.get(i);
            current = current.children.computeIfAbsent(bit, k -> new Node());
        }
        current.matchedPatterns.add(virus);
    }

    /**
     * Builds the fail links for the Aho-Corasick automaton.
     */
    private void buildFailLinks() {
        Queue<Node> queue = new LinkedList<>();
        root.fail = root;

        // Initialize the fail links for the first level
        for (Node child : root.children.values()) {
            child.fail = root;
            queue.add(child);
        }

        // Process the rest of the Trie
        while (!queue.isEmpty()) {
            Node current = queue.poll();
            for (Map.Entry<Boolean, Node> entry : current.children.entrySet()) {
                boolean bit = entry.getKey();
                Node child = entry.getValue();

                // Set the fail link
                Node failNode = current.fail;
                while (failNode != root && !failNode.children.containsKey(bit)) {
                    failNode = failNode.fail;
                }
                child.fail = failNode.children.getOrDefault(bit, root);

                // Add the fail node's matches to this node
                child.matchedPatterns.addAll(child.fail.matchedPatterns);

                queue.add(child);
            }
        }
    }

    /**
     * Searches for patterns in the given Bitmap using the Aho-Corasick automaton.
     */
    private void search(Bitmap str, String filename, ScanResult result) {
        Node current = root;
        for (int i = 0; i < str.length(); i++) {
            boolean bit = str.get(i);

            // Follow fail links if necessary
            while (current != root && !current.children.containsKey(bit)) {
                current = current.fail;
            }

            // Move to the next state
            if (current.children.containsKey(bit)) {
                current = current.children.get(bit);
            }

            // Check for matches
            for (String virus : current.matchedPatterns) {
                result.addMatch(filename, virus);
            }
        }
    }
}

