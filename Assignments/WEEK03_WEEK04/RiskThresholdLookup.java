import java.util.*;

public class RiskThresholdLookup {


    // -------- Linear Search --------

    static void linearSearch(int arr[], int target) {

        int comparisons = 0;
        boolean found = false;

        for(int i=0;i<arr.length;i++) {

            comparisons++;

            if(arr[i] == target) {

                System.out.println(
                        "\nLinear: threshold=" +
                                target +
                                " found at index " +
                                i +
                                " (" +
                                comparisons +
                                " comps)"
                );

                found = true;
                break;
            }
        }


        if(!found) {

            System.out.println(
                    "\nLinear: threshold=" +
                            target +
                            " → not found (" +
                            comparisons +
                            " comps)"
            );
        }
    }



    // -------- Binary Search for floor & ceiling --------

    static void binarySearchFloorCeil(int arr[], int target) {

        int low = 0;
        int high = arr.length-1;

        int floor = -1;
        int ceil = -1;

        int comparisons = 0;

        while(low <= high) {

            comparisons++;

            int mid = (low + high)/2;

            if(arr[mid] == target) {

                floor = arr[mid];
                ceil = arr[mid];
                break;
            }

            else if(arr[mid] < target) {

                floor = arr[mid];
                low = mid + 1;
            }

            else {

                ceil = arr[mid];
                high = mid - 1;
            }
        }



        System.out.println(
                "Binary floor(" +
                        target +
                        "): " +
                        floor +
                        ", ceiling: " +
                        ceil +
                        " (" +
                        comparisons +
                        " comps)"
        );
    }



    static void printArray(int arr[]) {

        System.out.print("Sorted risks: [");

        for(int i=0;i<arr.length;i++) {

            System.out.print(arr[i]);

            if(i<arr.length-1)
                System.out.print(", ");
        }

        System.out.println("]");
    }



    public static void main(String args[]) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of risk bands: ");
        int n = sc.nextInt();

        int risks[] = new int[n];


        for(int i=0;i<n;i++) {

            System.out.print(
                    "Enter risk value " +
                            (i+1) +
                            ": "
            );

            risks[i] = sc.nextInt();
        }


        System.out.print("\nEnter threshold value: ");
        int target = sc.nextInt();


        // sort for binary search
        Arrays.sort(risks);


        printArray(risks);


        // linear search
        linearSearch(risks, target);


        // binary search floor and ceiling
        binarySearchFloorCeil(risks, target);


        sc.close();
    }
}


