import java.util.*;

public class DNSCache {

    private static class DNSEntry {
        String domain;
        String ipAddress;
        long expiryTime;

        public DNSEntry(String domain, String ipAddress, long ttlSeconds) {
            this.domain = domain;
            this.ipAddress = ipAddress;
            this.expiryTime = System.currentTimeMillis() + ttlSeconds * 1000;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }
    }

    private final int MAX_SIZE = 3;  // LRU capacity
    private final LinkedHashMap<String, DNSEntry> cache;

    private int hitCount = 0;
    private int missCount = 0;
    private long totalLookupTime = 0;

    public DNSCache() {
        cache = new LinkedHashMap<>(16, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, DNSEntry> eldest) {
                return size() > MAX_SIZE;
            }
        };
    }

    public String resolve(String domain) {

        long startTime = System.nanoTime();

        if (cache.containsKey(domain)) {

            DNSEntry entry = cache.get(domain);

            if (!entry.isExpired()) {
                hitCount++;
                totalLookupTime += (System.nanoTime() - startTime);
                return "Cache HIT → " + entry.ipAddress;
            } else {
                cache.remove(domain);
                System.out.println("Cache EXPIRED → Fetching new IP");
            }
        }

        // Cache MISS
        missCount++;

        String newIP = queryUpstreamDNS(domain);
        cache.put(domain, new DNSEntry(domain, newIP, 5));  // TTL 5 seconds

        totalLookupTime += (System.nanoTime() - startTime);

        return "Cache MISS → " + newIP;
    }

    // Simulated upstream DNS
    private String queryUpstreamDNS(String domain) {
        return "172.217." + new Random().nextInt(100) + "." + new Random().nextInt(255);
    }

    public void getCacheStats() {

        int totalRequests = hitCount + missCount;
        double hitRate = totalRequests == 0 ? 0 :
                (double) hitCount / totalRequests * 100;

        double avgTimeMs = totalRequests == 0 ? 0 :
                (totalLookupTime / totalRequests) / 1_000_000.0;

        System.out.println("Hit Rate: " + String.format("%.2f", hitRate) + "%");
        System.out.println("Avg Lookup Time: " + String.format("%.4f", avgTimeMs) + " ms");
    }

    public static void main(String[] args) throws InterruptedException {

        DNSCache dns = new DNSCache();

        System.out.println(dns.resolve("google.com"));
        System.out.println(dns.resolve("google.com"));

        Thread.sleep(6000);  // wait for TTL to expire

        System.out.println(dns.resolve("google.com"));

        dns.getCacheStats();
    }
}