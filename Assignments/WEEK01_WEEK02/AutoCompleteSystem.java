import java.util.*;

public class AutoCompleteSystem {

    // Global frequency map
    private final Map<String, Integer> frequencyMap = new HashMap<>();

    // Trie root
    private final TrieNode root = new TrieNode();

    private static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        Set<String> queries = new HashSet<>();
    }

    // -------------------------------
    // Add or update search query
    // -------------------------------
    public void updateFrequency(String query) {

        int newFreq = frequencyMap.getOrDefault(query, 0) + 1;
        frequencyMap.put(query, newFreq);

        insertIntoTrie(query);

        System.out.println("updateFrequency(\"" + query +
                "\") → Frequency: " + newFreq);
    }

    private void insertIntoTrie(String query) {

        TrieNode current = root;

        for (char ch : query.toCharArray()) {

            current.children.putIfAbsent(ch, new TrieNode());
            current = current.children.get(ch);

            current.queries.add(query);
        }
    }

    // -------------------------------
    // Search top 10 suggestions
    // -------------------------------
    public void search(String prefix) {

        TrieNode current = root;

        for (char ch : prefix.toCharArray()) {

            if (!current.children.containsKey(ch)) {
                System.out.println("No suggestions found.");
                return;
            }

            current = current.children.get(ch);
        }

        PriorityQueue<String> minHeap =
                new PriorityQueue<>(
                        Comparator.comparingInt(frequencyMap::get)
                );

        for (String query : current.queries) {

            minHeap.offer(query);

            if (minHeap.size() > 10) {
                minHeap.poll();
            }
        }

        List<String> results = new ArrayList<>(minHeap);
        results.sort((a, b) ->
                frequencyMap.get(b) - frequencyMap.get(a));

        System.out.println("search(\"" + prefix + "\") →");

        int rank = 1;
        for (String result : results) {
            System.out.println(rank + ". \"" +
                    result + "\" (" +
                    frequencyMap.get(result) +
                    " searches)");
            rank++;
        }
    }

    // -------------------------------
    // Main Method
    // -------------------------------
    public static void main(String[] args) {

        AutoCompleteSystem system =
                new AutoCompleteSystem();

        system.updateFrequency("java tutorial");
        system.updateFrequency("javascript");
        system.updateFrequency("java download");
        system.updateFrequency("java tutorial");
        system.updateFrequency("java tutorial");

        system.search("jav");
    }
}