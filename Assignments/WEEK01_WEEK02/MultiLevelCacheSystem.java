import java.util.*;

class VideoData {
    String videoId;
    String content;

    public VideoData(String videoId, String content) {
        this.videoId = videoId;
        this.content = content;
    }
}

public class MultiLevelCacheSystem {

    private static final int L1_CAPACITY = 10000;
    private static final int L2_CAPACITY = 100000;
    private static final int PROMOTION_THRESHOLD = 3;

    private final LinkedHashMap<String, VideoData> L1;
    private final LinkedHashMap<String, VideoData> L2;
    private final Map<String, VideoData> L3;

    private final Map<String, Integer> accessCount;

    private int L1Hits = 0;
    private int L2Hits = 0;
    private int L3Hits = 0;
    private int totalRequests = 0;

    public MultiLevelCacheSystem() {

        // L1 Cache (Memory)
        L1 = new LinkedHashMap<>(L1_CAPACITY, 0.75f, true) {
            protected boolean removeEldestEntry(
                    Map.Entry<String, VideoData> eldest) {
                return size() > L1_CAPACITY;
            }
        };

        // L2 Cache (SSD simulated)
        L2 = new LinkedHashMap<>(L2_CAPACITY, 0.75f, true) {
            protected boolean removeEldestEntry(
                    Map.Entry<String, VideoData> eldest) {
                return size() > L2_CAPACITY;
            }
        };

        // L3 Database (All videos)
        L3 = new HashMap<>();
        accessCount = new HashMap<>();

        // Simulated database
        for (int i = 1; i <= 1000; i++) {
            L3.put("video_" + i,
                    new VideoData("video_" + i,
                            "Content of video " + i));
        }
    }

    // ---------------------------------------------------
    // GET VIDEO
    // ---------------------------------------------------
    public void getVideo(String videoId) {

        System.out.println("getVideo(\"" + videoId + "\")");

        totalRequests++;
        double totalTime = 0;

        // ---- L1 ----
        if (L1.containsKey(videoId)) {

            L1Hits++;
            totalTime += 0.5;

            System.out.println("→ L1 Cache HIT (0.5ms)");
            printTotal(totalTime);
            return;
        }

        System.out.println("→ L1 Cache MISS");

        // ---- L2 ----
        if (L2.containsKey(videoId)) {

            L2Hits++;
            totalTime += 5;

            System.out.println("→ L2 Cache HIT (5ms)");

            promote(videoId);

            totalTime += 0.5; // Access time after promotion
            printTotal(totalTime);
            return;
        }

        System.out.println("→ L2 Cache MISS");

        // ---- L3 ----
        if (L3.containsKey(videoId)) {

            L3Hits++;
            totalTime += 150;

            System.out.println("→ L3 Database HIT (150ms)");

            L2.put(videoId, L3.get(videoId));
            accessCount.put(videoId, 1);

            System.out.println("→ Added to L2 (access count: 1)");

            printTotal(totalTime);
            return;
        }

        System.out.println("→ Video not found.");
    }

    // ---------------------------------------------------
    // PROMOTION LOGIC
    // ---------------------------------------------------
    private void promote(String videoId) {

        int count = accessCount.getOrDefault(videoId, 0) + 1;
        accessCount.put(videoId, count);

        if (count >= PROMOTION_THRESHOLD) {

            L1.put(videoId, L2.get(videoId));
            System.out.println("→ Promoted to L1");
        }
    }

    // ---------------------------------------------------
    // CACHE INVALIDATION
    // ---------------------------------------------------
    public void invalidate(String videoId) {

        L1.remove(videoId);
        L2.remove(videoId);
        accessCount.remove(videoId);

        System.out.println("Cache invalidated for " + videoId);
    }

    // ---------------------------------------------------
    // STATISTICS
    // ---------------------------------------------------
    public void getStatistics() {

        System.out.println("getStatistics() →");

        double L1Rate = (double) L1Hits / totalRequests * 100;
        double L2Rate = (double) L2Hits / totalRequests * 100;
        double L3Rate = (double) L3Hits / totalRequests * 100;

        double avgTime =
                (L1Hits * 0.5 +
                        L2Hits * 5 +
                        L3Hits * 150) / totalRequests;

        System.out.println("L1: Hit Rate "
                + String.format("%.1f", L1Rate)
                + "%, Avg Time: 0.5ms");

        System.out.println("L2: Hit Rate "
                + String.format("%.1f", L2Rate)
                + "%, Avg Time: 5ms");

        System.out.println("L3: Hit Rate "
                + String.format("%.1f", L3Rate)
                + "%, Avg Time: 150ms");

        System.out.println("Overall: Hit Rate "
                + String.format("%.1f",
                (double)(L1Hits + L2Hits) / totalRequests * 100)
                + "%, Avg Time: "
                + String.format("%.1f", avgTime)
                + "ms\n");
    }

    // ---------------------------------------------------
    // PRINT TOTAL TIME
    // ---------------------------------------------------
    private void printTotal(double totalTime) {
        System.out.println("→ Total: " + totalTime + "ms\n");
    }

    // ---------------------------------------------------
    // MAIN
    // ---------------------------------------------------
    public static void main(String[] args) {

        MultiLevelCacheSystem cache =
                new MultiLevelCacheSystem();

        cache.getVideo("video_123");
        cache.getVideo("video_123");
        cache.getVideo("video_123");

        cache.getVideo("video_999");

        cache.getStatistics();
    }
}