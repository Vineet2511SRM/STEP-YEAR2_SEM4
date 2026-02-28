import java.util.HashMap;
import java.util.Map;

public class TokenBucketRateLimiter {

    private static final int MAX_REQUESTS = 5; // change to 1000 in production
    private static final long WINDOW_SECONDS = 3600;

    private final Map<String, TokenBucket> buckets = new HashMap<>();

    private static class TokenBucket {

        private final int maxTokens;
        private final double refillRatePerSecond;

        private double tokens;
        private long lastRefillTime;

        public TokenBucket(int maxTokens) {
            this.maxTokens = maxTokens;
            this.tokens = maxTokens;
            this.refillRatePerSecond =
                    (double) maxTokens / WINDOW_SECONDS;
            this.lastRefillTime = System.currentTimeMillis();
        }

        public synchronized RateLimitResult allowRequest() {

            refillTokens();

            if (tokens >= 1) {
                tokens -= 1;

                return new RateLimitResult(
                        true,
                        (int) tokens,
                        0
                );
            }

            long retryAfter =
                    (long) Math.ceil(1 / refillRatePerSecond);

            return new RateLimitResult(
                    false,
                    0,
                    retryAfter
            );
        }

        private void refillTokens() {

            long currentTime = System.currentTimeMillis();
            long elapsedMillis = currentTime - lastRefillTime;

            double tokensToAdd =
                    (elapsedMillis / 1000.0) * refillRatePerSecond;

            if (tokensToAdd > 0) {
                tokens = Math.min(maxTokens, tokens + tokensToAdd);
                lastRefillTime = currentTime;
            }
        }
    }

    private static class RateLimitResult {

        boolean allowed;
        int remainingTokens;
        long retryAfterSeconds;

        public RateLimitResult(boolean allowed,
                               int remainingTokens,
                               long retryAfterSeconds) {
            this.allowed = allowed;
            this.remainingTokens = remainingTokens;
            this.retryAfterSeconds = retryAfterSeconds;
        }
    }

    public String checkRateLimit(String clientId) {

        TokenBucket bucket;

        synchronized (buckets) {

            if (!buckets.containsKey(clientId)) {
                buckets.put(clientId,
                        new TokenBucket(MAX_REQUESTS));
            }

            bucket = buckets.get(clientId);
        }

        RateLimitResult result = bucket.allowRequest();

        if (result.allowed) {
            return "checkRateLimit(clientId=\"" + clientId +
                    "\") → Allowed (" +
                    result.remainingTokens +
                    " requests remaining)";
        } else {
            return "checkRateLimit(clientId=\"" + clientId +
                    "\") → Denied (0 requests remaining, retry after " +
                    result.retryAfterSeconds + "s)";
        }
    }

    public String getRateLimitStatus(String clientId) {

        synchronized (buckets) {

            TokenBucket bucket = buckets.get(clientId);

            if (bucket == null) {
                return "No activity for client.";
            }

            int used = MAX_REQUESTS - (int) bucket.tokens;

            long currentTime = System.currentTimeMillis() / 1000;

            double secondsToFullRefill =
                    (MAX_REQUESTS - bucket.tokens)
                            / bucket.refillRatePerSecond;

            long resetTimestamp =
                    currentTime + (long) Math.ceil(secondsToFullRefill);

            return "getRateLimitStatus(\"" + clientId +
                    "\") → {used: " +
                    used +
                    ", limit: " + MAX_REQUESTS +
                    ", reset: " + resetTimestamp + "}";
        }
    }

    public static void main(String[] args) {

        TokenBucketRateLimiter limiter =
                new TokenBucketRateLimiter();

        for (int i = 0; i < 7; i++) {
            System.out.println(
                    limiter.checkRateLimit("abc123"));
        }

        System.out.println(
                limiter.getRateLimitStatus("abc123"));
    }
}