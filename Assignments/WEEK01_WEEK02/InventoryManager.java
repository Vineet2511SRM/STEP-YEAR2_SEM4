import java.util.*;

public class InventoryManager {

    // Product stock map
    private final Map<String, Integer> stock;

    // Waiting list for each product
    private final Map<String, Queue<Integer>> waitingList;

    public InventoryManager() {
        stock = new HashMap<>();
        waitingList = new HashMap<>();
    }

    // Add product with stock
    public void addProduct(String productId, int quantity) {
        stock.put(productId, quantity);
        waitingList.put(productId, new LinkedList<>());
    }

    // Check stock
    public int checkStock(String productId) {
        return stock.getOrDefault(productId, 0);
    }

    // Purchase item (synchronized to prevent overselling)
    public synchronized String purchaseItem(String productId, int userId) {

        int available = stock.getOrDefault(productId, 0);

        if (available > 0) {
            stock.put(productId, available - 1);
            return "Success! Remaining stock: " + (available - 1);
        } else {
            waitingList.get(productId).offer(userId);
            return "Out of stock. Added to waiting list. Position: " +
                    waitingList.get(productId).size();
        }
    }

    // Main method to test
    public static void main(String[] args) {

        InventoryManager manager = new InventoryManager();

        manager.addProduct("IPHONE15_256GB", 3);

        System.out.println("Stock: " +
                manager.checkStock("IPHONE15_256GB"));

        System.out.println(manager.purchaseItem("IPHONE15_256GB", 101));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 102));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 103));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 104));
    }
}