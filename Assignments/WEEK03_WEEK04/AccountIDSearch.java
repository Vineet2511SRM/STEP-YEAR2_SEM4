import java.util.*;

public class AccountIDSearch {


    // ---------- LINEAR SEARCH ----------

    static void linearSearch(String arr[], String target) {

        int first = -1;
        int last = -1;

        int comparisons = 0;

        for(int i=0;i<arr.length;i++) {

            comparisons++;

            if(arr[i].equals(target)) {

                if(first == -1)
                    first = i;

                last = i;
            }
        }


        if(first == -1) {

            System.out.println("\nLinear " + target + ": Not found (" +
                    comparisons + " comparisons)");
        }
        else {

            System.out.println(
                    "\nLinear first " + target +
                            ": index " + first +
                            " (" + comparisons +
                            " comparisons)"
            );
        }
    }



    // ---------- BINARY SEARCH ----------

    static void binarySearch(String arr[], String target) {

        int low = 0;
        int high = arr.length-1;

        int comparisons = 0;

        int foundIndex = -1;

        while(low <= high) {

            comparisons++;

            int mid = (low + high)/2;

            if(arr[mid].equals(target)) {

                foundIndex = mid;
                break;
            }
            else if(arr[mid].compareTo(target) < 0)
                low = mid+1;

            else
                high = mid-1;
        }



        if(foundIndex == -1) {

            System.out.println(
                    "Binary " + target +
                            ": Not found (" +
                            comparisons +
                            " comparisons)"
            );

            return;
        }



        // count duplicates
        int count = 1;

        int left = foundIndex-1;

        while(left >= 0 &&
                arr[left].equals(target)) {

            count++;
            left--;
        }

        int right = foundIndex+1;

        while(right < arr.length &&
                arr[right].equals(target)) {

            count++;
            right++;
        }


        System.out.println(
                "Binary " + target +
                        ": index " + foundIndex +
                        " (" + comparisons +
                        " comparisons), count=" +
                        count
        );
    }



    static void printArray(String arr[]) {

        System.out.print("Sorted logs: [");

        for(int i=0;i<arr.length;i++) {

            System.out.print(arr[i]);

            if(i<arr.length-1)
                System.out.print(", ");
        }

        System.out.println("]");
    }



    public static void main(String args[]) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of logs: ");
        int n = sc.nextInt();

        String logs[] = new String[n];


        for(int i=0;i<n;i++) {

            System.out.print("Enter account id " + (i+1) + ": ");
            logs[i] = sc.next();
        }



        System.out.print("\nEnter account to search: ");
        String target = sc.next();



        // sort logs for binary search
        Arrays.sort(logs);


        printArray(logs);


        // linear search
        linearSearch(logs, target);


        // binary search
        binarySearch(logs, target);


        sc.close();
    }
}

