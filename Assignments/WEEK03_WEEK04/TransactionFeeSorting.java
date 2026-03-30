import java.util.*;

// class to store each transaction
class Transactions {

    String id;
    double fee;
    String timestamp;

    // constructor to assign values
    Transactions(String id, double fee, String timestamp) {
        this.id = id;
        this.fee = fee;
        this.timestamp = timestamp;
    }
}


public class TransactionFeeSorting {

    // Bubble Sort based only on fee
    static void bubbleSort(ArrayList<Transactions> list) {

        int n = list.size();

        int passes = 0;
        int swaps = 0;

        // normal bubble sort logic
        for(int i = 0; i < n-1; i++) {

            boolean swapped = false;
            passes++;

            for(int j = 0; j < n-i-1; j++) {

                // compare adjacent fees
                if(list.get(j).fee > list.get(j+1).fee) {

                    // swap the objects
                    Transactions temp = list.get(j);
                    list.set(j, list.get(j+1));
                    list.set(j+1, temp);

                    swaps++;
                    swapped = true;
                }
            }

            // if no swap happens means already sorted
            if(!swapped)
                break;
        }

        // print output format similar to question
        System.out.print("\nBubbleSort (fees): [");

        for(int i=0;i<n;i++) {

            System.out.print(
                    list.get(i).id + ":" +
                            list.get(i).fee
            );

            if(i<n-1) System.out.print(", ");
        }

        System.out.println("]  // " + passes +
                " passes, " + swaps + " swaps");
    }



    // insertion sort using fee + timestamp
    static void insertionSort(ArrayList<Transactions> list) {

        for(int i=1;i<list.size();i++) {

            Transactions key = list.get(i);

            int j = i-1;

            // shift elements if greater
            while(j>=0 &&
                    (list.get(j).fee > key.fee ||
                            (list.get(j).fee == key.fee &&
                                    list.get(j).timestamp.compareTo(key.timestamp)>0))) {

                list.set(j+1, list.get(j));

                j--;
            }

            list.set(j+1, key);
        }

        // print output format similar to question
        System.out.print("\nInsertionSort (fee+ts): [");

        for(int i=0;i<list.size();i++) {

            System.out.print(
                    list.get(i).id + ":" +
                            list.get(i).fee +
                            "@" +
                            list.get(i).timestamp
            );

            if(i<list.size()-1) System.out.print(", ");
        }

        System.out.println("]");
    }



    // find high fee transactions
    static void highFee(ArrayList<Transactions> list) {

        boolean found = false;

        System.out.print("\nHigh-fee outliers: ");

        for(Transactions t : list) {

            if(t.fee > 50) {

                System.out.print(
                        t.id +
                                ":" +
                                t.fee +
                                " "
                );

                found = true;
            }
        }

        if(!found)
            System.out.print("none");

        System.out.println();
    }



    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of transactions: ");
        int n = sc.nextInt();

        // storing transactions in ArrayList
        ArrayList<Transactions> list = new ArrayList<>();


        // taking input
        for(int i=0;i<n;i++) {

            System.out.println("\nTransaction " + (i+1));

            System.out.print("Enter id: ");
            String id = sc.next();

            System.out.print("Enter fee: ");
            double fee = sc.nextDouble();

            System.out.print("Enter timestamp (HH:MM): ");
            String ts = sc.next();

            list.add(new Transactions(id, fee, ts));
        }



        // bubble sort for small batch
        if(n <= 100)
            bubbleSort(list);

        // insertion sort for medium batch
        if(n >= 100 && n <= 1000)
            insertionSort(list);

        // checking high fee
        highFee(list);

        sc.close();
    }
}

