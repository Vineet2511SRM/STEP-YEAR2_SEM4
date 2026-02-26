import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class UsernameAvailabilitySystem {

    private final ConcurrentHashMap<String, Integer> users;
    private final ConcurrentHashMap<String, Integer> attempts;

    public UsernameAvailabilitySystem() {
        users = new ConcurrentHashMap<>();
        attempts = new ConcurrentHashMap<>();
    }

    // Check availability
    public boolean checkAvailability(String username) {
        attempts.merge(username, 1, Integer::sum);
        return !users.containsKey(username);
    }

    // Register user
    public void registerUser(String username, int userId) {
        users.putIfAbsent(username, userId);
    }

    // Suggest alternatives
    public List<String> suggestAlternatives(String username) {
        List<String> suggestions = new ArrayList<>();

        for (int i = 1; i <= 3; i++) {
            String suggestion = username + i;
            if (!users.containsKey(suggestion)) {
                suggestions.add(suggestion);
            }
        }

        String dotVersion = username.replace("_", ".");
        if (!users.containsKey(dotVersion)) {
            suggestions.add(dotVersion);
        }

        return suggestions;
    }

    // Get most attempted username
    public String getMostAttempted() {
        String maxUser = null;
        int max = 0;

        for (Map.Entry<String, Integer> entry : attempts.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                maxUser = entry.getKey();
            }
        }

        if (maxUser == null) return "No attempts yet";
        return maxUser + " (" + max + " attempts)";
    }

    // MAIN METHOD TO RUN
    public static void main(String[] args) {

        UsernameAvailabilitySystem system = new UsernameAvailabilitySystem();

        // Register some users
        system.registerUser("john_doe", 101);
        system.registerUser("admin", 1);

        // Check availability
        System.out.println("john_doe available? " + system.checkAvailability("john_doe"));
        System.out.println("jane_smith available? " + system.checkAvailability("jane_smith"));

        // Suggestions
        System.out.println("Suggestions for john_doe: " +
                system.suggestAlternatives("john_doe"));

        // Simulate attempts
        system.checkAvailability("admin");
        system.checkAvailability("admin");
        system.checkAvailability("admin");

        System.out.println("Most attempted: " + system.getMostAttempted());
    }
}