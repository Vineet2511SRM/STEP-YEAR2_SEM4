import java.util.*;

public class PlagiarismDetector {

    // Size of n-gram (5 consecutive words)
    private static final int N = 5;

    // HashMap<n-gram, Set of document IDs>
    private final Map<String, Set<String>> nGramIndex;

    public PlagiarismDetector() {
        nGramIndex = new HashMap<>();
    }

    // -------------------------------
    // Add document to index database
    // -------------------------------
    public void addDocument(String documentId, String content) {

        List<String> ngrams = generateNGrams(content);

        for (String gram : ngrams) {
            nGramIndex
                    .computeIfAbsent(gram, k -> new HashSet<>())
                    .add(documentId);
        }
    }

    // -------------------------------
    // Analyze new document
    // -------------------------------
    public void analyzeDocument(String documentId, String content) {

        List<String> ngrams = generateNGrams(content);
        Map<String, Integer> matchCount = new HashMap<>();

        // Count matching n-grams
        for (String gram : ngrams) {
            if (nGramIndex.containsKey(gram)) {

                for (String existingDoc : nGramIndex.get(gram)) {
                    matchCount.put(existingDoc,
                            matchCount.getOrDefault(existingDoc, 0) + 1);
                }
            }
        }

        // ---- Structured Output ----
        System.out.println("analyzeDocument(\"" + documentId + "\")");
        System.out.println("→ Extracted " + ngrams.size() + " n-grams");

        if (matchCount.isEmpty()) {
            System.out.println("→ No matching documents found.");
            return;
        }

        for (Map.Entry<String, Integer> entry : matchCount.entrySet()) {

            String existingDoc = entry.getKey();
            int matches = entry.getValue();

            double similarity =
                    (double) matches / ngrams.size() * 100;

            System.out.println("→ Found " + matches +
                    " matching n-grams with \"" +
                    existingDoc + "\"");

            String status;
            if (similarity >= 60) {
                status = "PLAGIARISM DETECTED";
            } else if (similarity >= 10) {
                status = "suspicious";
            } else {
                status = "low similarity";
            }

            System.out.println("→ Similarity: " +
                    String.format("%.1f", similarity) +
                    "% (" + status + ")");
        }
    }

    // -------------------------------
    // Generate n-grams from content
    // -------------------------------
    private List<String> generateNGrams(String content) {

        List<String> result = new ArrayList<>();

        // Normalize text
        String[] words = content
                .toLowerCase()
                .replaceAll("[^a-z0-9 ]", "")
                .split("\\s+");

        for (int i = 0; i <= words.length - N; i++) {

            StringBuilder gram = new StringBuilder();

            for (int j = 0; j < N; j++) {
                gram.append(words[i + j]).append(" ");
            }

            result.add(gram.toString().trim());
        }

        return result;
    }

    // -------------------------------
    // Main method (Test Example)
    // -------------------------------
    public static void main(String[] args) {

        PlagiarismDetector detector = new PlagiarismDetector();

        // Existing essays
        detector.addDocument("essay_089.txt",
                "machine learning is very powerful and widely used today in education and research");

        detector.addDocument("essay_092.txt",
                "machine learning is very powerful and widely used today in many industries and applications");

        // New submission
        String newEssay =
                "machine learning is very powerful and widely used today in many industries";

        detector.analyzeDocument("essay_123.txt", newEssay);
    }
}