import java.util.*;

class RealTimeAnalytics {

    // Page URL → total views
    private final Map<String, Integer> pageViews = new HashMap<>();

    // Page URL → set of unique users
    private final Map<String, Set<String>> uniqueVisitors = new HashMap<>();

    // Traffic source → count
    private final Map<String, Integer> sourceCount = new HashMap<>();

    // -----------------------------
    // Process incoming page event
    // -----------------------------
    public void processEvent(String url, String userId, String source) {

        //  Update total views
        pageViews.put(url,
                pageViews.getOrDefault(url, 0) + 1);

        //  Update unique visitors
        uniqueVisitors
                .computeIfAbsent(url, k -> new HashSet<>())
                .add(userId);

        //  Update traffic source
        sourceCount.put(source,
                sourceCount.getOrDefault(source, 0) + 1);
    }

    // -----------------------------
    // Get Dashboard
    // -----------------------------
    public void getDashboard() {

        System.out.println("\ngetDashboard() →");

        // -------- Top Pages --------
        System.out.println("Top Pages:");

        PriorityQueue<Map.Entry<String, Integer>> minHeap =
                new PriorityQueue<>(Comparator.comparingInt(Map.Entry::getValue));

        for (Map.Entry<String, Integer> entry : pageViews.entrySet()) {
            minHeap.offer(entry);
            if (minHeap.size() > 10) {
                minHeap.poll();
            }
        }

        List<Map.Entry<String, Integer>> topPages =
                new ArrayList<>(minHeap);

        topPages.sort((a, b) -> b.getValue() - a.getValue());

        int rank = 1;
        for (Map.Entry<String, Integer> entry : topPages) {

            String url = entry.getKey();
            int totalViews = entry.getValue();
            int uniqueCount = uniqueVisitors.get(url).size();

            System.out.println(rank + ". " + url +
                    " - " + totalViews +
                    " views (" + uniqueCount + " unique)");
            rank++;
        }

        // -------- Traffic Sources --------
        System.out.println("\nTraffic Sources:");

        int totalTraffic = sourceCount.values()
                .stream().mapToInt(Integer::intValue).sum();

        for (Map.Entry<String, Integer> entry : sourceCount.entrySet()) {

            double percentage =
                    (double) entry.getValue() / totalTraffic * 100;

            System.out.println(entry.getKey() + ": " +
                    String.format("%.1f", percentage) + "%");
        }
    }

    // -----------------------------
    // Main Method
    // -----------------------------
    public static void main(String[] args) {

        RealTimeAnalytics analytics = new RealTimeAnalytics();

        analytics.processEvent("/article/breaking-news", "user_123", "google");
        analytics.processEvent("/article/breaking-news", "user_456", "facebook");
        analytics.processEvent("/sports/championship", "user_123", "direct");
        analytics.processEvent("/sports/championship", "user_789", "google");
        analytics.processEvent("/article/breaking-news", "user_123", "google");

        analytics.getDashboard();
    }
}