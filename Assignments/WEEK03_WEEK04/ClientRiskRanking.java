import java.util.*;

// class representing each client
class Client {

    String name;
    int riskScore;
    double accountBalance;

    Client(String name, int riskScore, double accountBalance) {

        this.name = name;
        this.riskScore = riskScore;
        this.accountBalance = accountBalance;
    }
}



public class ClientRiskRanking {


    // Bubble sort for ascending order of riskScore
    static void bubbleSort(Client arr[]) {

        int n = arr.length;
        int swaps = 0;

        for(int i=0;i<n-1;i++) {

            for(int j=0;j<n-i-1;j++) {

                // compare adjacent elements
                if(arr[j].riskScore > arr[j+1].riskScore) {

                    // swap clients
                    Client temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;

                    swaps++;
                }
            }
        }

        // print result in required format
        System.out.print("\nBubble (asc): [");

        for(int i=0;i<n;i++) {

            System.out.print(
                    arr[i].name + ":" +
                            arr[i].riskScore
            );

            if(i<n-1) System.out.print(", ");
        }

        System.out.println("] // Swaps: " + swaps);
    }



    // insertion sort for descending riskScore
    // if risk same, consider account balance
    static void insertionSort(Client arr[]) {

        for(int i=1;i<arr.length;i++) {

            Client key = arr[i];

            int j = i-1;

            // descending order logic
            while(j>=0 &&
                    (arr[j].riskScore < key.riskScore ||
                            (arr[j].riskScore == key.riskScore &&
                                    arr[j].accountBalance < key.accountBalance))) {

                arr[j+1] = arr[j];

                j--;
            }

            arr[j+1] = key;
        }

        System.out.print("\nInsertion (desc): [");

        for(int i=0;i<arr.length;i++) {

            System.out.print(
                    arr[i].name + ":" +
                            arr[i].riskScore
            );

            if(i<arr.length-1) System.out.print(", ");
        }

        System.out.println("]");
    }



    // print top 10 highest risk clients
    static void topRisks(Client arr[]) {

        System.out.print("\nTop 10 risks: ");

        int limit = Math.min(10, arr.length);

        for(int i=0;i<limit;i++) {

            System.out.print(
                    arr[i].name +
                            "(" +
                            arr[i].riskScore +
                            ")"
            );

            if(i<limit-1) System.out.print(", ");
        }

        System.out.println();
    }



    public static void main(String args[]) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of clients: ");
        int n = sc.nextInt();

        Client arr[] = new Client[n];


        // input details
        for(int i=0;i<n;i++) {

            System.out.println("\nClient " + (i+1));

            System.out.print("Enter name: ");
            String name = sc.next();

            System.out.print("Enter risk score: ");
            int score = sc.nextInt();

            System.out.print("Enter account balance: ");
            double balance = sc.nextDouble();

            arr[i] = new Client(name, score, balance);
        }


        // bubble sort ascending
        bubbleSort(arr);


        // insertion sort descending
        insertionSort(arr);


        // print top risks
        topRisks(arr);

        sc.close();
    }
}

