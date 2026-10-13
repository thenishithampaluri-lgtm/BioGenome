package biogenome.m1;

import java.util.*;

/**
 * M1 - String Algorithms (CO1)
 * Aho-Corasick Automaton for Simultaneous DNA Multi-Motif Detection.
 * Time Complexity: O(N + sum(M_i) + K)
 * Space Complexity: O(sum(M_i) * alphabet)
 */
public class AhoCorasickSearcher {

    static class Node {
        Map<Character, Node> children = new HashMap<>();
        Node fail = null;
        List<String> output = new ArrayList<>();
    }

    private final Node root;
    private final List<String> patterns;

    public AhoCorasickSearcher(List<String> patterns) {
        this.patterns = new ArrayList<>(patterns);
        this.root = new Node();
        buildTrie();
        buildFailureLinks();
    }

    private void buildTrie() {
        for (String pat : patterns) {
            Node curr = root;
            for (char ch : pat.toCharArray()) {
                curr = curr.children.computeIfAbsent(ch, c -> new Node());
            }
            curr.output.add(pat);
        }
    }

    private void buildFailureLinks() {
        Queue<Node> queue = new ArrayDeque<>();
        for (Node child : root.children.values()) {
            child.fail = root;
            queue.add(child);
        }

        while (!queue.isEmpty()) {
            Node curr = queue.poll();
            for (Map.Entry<Character, Node> entry : curr.children.entrySet()) {
                char ch = entry.getKey();
                Node child = entry.getValue();

                Node failNode = curr.fail;
                while (failNode != null && !failNode.children.containsKey(ch)) {
                    failNode = failNode.fail;
                }
                child.fail = (failNode != null) ? failNode.children.get(ch) : root;
                child.output.addAll(child.fail.output);
                queue.add(child);
            }
        }
    }

    public Map<String, Object> search(String text) {
        long t0 = System.nanoTime();
        Map<String, List<Integer>> results = new HashMap<>();
        for (String p : patterns) {
            results.put(p, new ArrayList<>());
        }

        Node curr = root;
        int totalMatches = 0;

        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            while (curr != null && !curr.children.containsKey(ch)) {
                curr = curr.fail;
            }
            if (curr == null) {
                curr = root;
                continue;
            }
            curr = curr.children.get(ch);
            for (String pat : curr.output) {
                int startIdx = i - pat.length() + 1;
                results.get(pat).add(startIdx);
                totalMatches++;
            }
        }

        double elapsedMs = (System.nanoTime() - t0) / 1_000_000.0;
        Map<String, Object> output = new HashMap<>();
        output.put("algorithm", "Aho-Corasick Multi-Motif Automaton");
        output.put("patterns", patterns);
        output.put("results", results);
        output.put("totalMatches", totalMatches);
        output.put("elapsedMs", elapsedMs);
        output.put("complexity", "O(N + sum(M_i) + K) time");
        return output;
    }
}
