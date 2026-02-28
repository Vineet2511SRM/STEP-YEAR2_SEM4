import java.util.*;

class Transaction {
    int id;
    int amount;
    String merchant;
    String account;
    long timestamp;

    public Transaction(int id, int amount, String merchant,
                       String account, long timestamp) {
        this.id = id;
        this.amount = amount;
        this.merchant = merchant;
        this.account = account;
        this.timestamp = timestamp;
    }
}

public class TwoSumFinancialTransactionSystem {

    private List<Transaction> transactions;

    public TwoSumFinancialTransactionSystem(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    // -------------------------------
    // Classic Two-Sum
    // -------------------------------
    public List<String> findTwoSum(int target) {

        Map<Integer, Transaction> map = new HashMap<>();
        List<String> result = new ArrayList<>();

        for (Transaction t : transactions) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {
                Transaction match = map.get(complement);

                result.add("(id:" + match.id +
                        ", id:" + t.id + ")");
            }

            map.put(t.amount, t);
        }

        return result;
    }

    // -------------------------------
    // K-Sum
    // -------------------------------
    public List<List<Integer>> findKSum(int k, int target) {

        List<List<Integer>> result = new ArrayList<>();
        kSumHelper(0, k, target,
                new ArrayList<>(), result);
        return result;
    }

    private void kSumHelper(int start,
                            int k,
                            int target,
                            List<Integer> current,
                            List<List<Integer>> result) {

        if (k == 0 && target == 0) {
            result.add(new ArrayList<>(current));
            return;
        }

        if (k <= 0) return;

        for (int i = start;
             i < transactions.size(); i++) {

            current.add(transactions.get(i).id);

            kSumHelper(i + 1,
                    k - 1,
                    target - transactions.get(i).amount,
                    current,
                    result);

            current.remove(current.size() - 1);
        }
    }

    // -------------------------------
    // Duplicate Detection
    // -------------------------------
    public List<String> detectDuplicates() {

        Map<String, List<Transaction>> map = new HashMap<>();
        List<String> result = new ArrayList<>();

        for (Transaction t : transactions) {

            String key = t.amount + "_" + t.merchant;

            map.putIfAbsent(key, new ArrayList<>());
            map.get(key).add(t);
        }

        for (Map.Entry<String, List<Transaction>> entry :
                map.entrySet()) {

            if (entry.getValue().size() > 1) {

                Set<String> accounts = new HashSet<>();

                for (Transaction t : entry.getValue()) {
                    accounts.add(t.account);
                }

                if (accounts.size() > 1) {
                    result.add("Duplicate: " + entry.getKey());
                }
            }
        }

        return result;
    }

    // -------------------------------
    // Main
    // -------------------------------
    public static void main(String[] args) {

        List<Transaction> txns = new ArrayList<>();
        long now = System.currentTimeMillis();

        txns.add(new Transaction(1, 500,
                "Store A", "acc1", now));
        txns.add(new Transaction(2, 300,
                "Store B", "acc2", now));
        txns.add(new Transaction(3, 200,
                "Store C", "acc3", now));
        txns.add(new Transaction(4, 500,
                "Store A", "acc2", now));

        TwoSumFinancialTransactionSystem system =
                new TwoSumFinancialTransactionSystem(txns);

        System.out.println(
                "findTwoSum(500) → " +
                        system.findTwoSum(500));

        System.out.println(
                "findKSum(3,1000) → " +
                        system.findKSum(3, 1000));

        System.out.println(
                "detectDuplicates() → " +
                        system.detectDuplicates());
    }
}