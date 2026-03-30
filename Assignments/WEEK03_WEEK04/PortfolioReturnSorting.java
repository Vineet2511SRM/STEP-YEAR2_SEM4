import java.util.*;

// class representing asset
class Asset {

    String name;
    double returnRate;
    double volatility;

    Asset(String name, double returnRate, double volatility) {

        this.name = name;
        this.returnRate = returnRate;
        this.volatility = volatility;
    }
}



public class PortfolioReturnSorting {


    // ---------- MERGE SORT (ascending return) ----------

    static void mergeSort(Asset arr[], int left, int right) {

        if(left < right) {

            int mid = (left + right)/2;

            mergeSort(arr, left, mid);
            mergeSort(arr, mid+1, right);

            merge(arr, left, mid, right);
        }
    }



    static void merge(Asset arr[], int left, int mid, int right) {

        int n1 = mid-left+1;
        int n2 = right-mid;

        Asset L[] = new Asset[n1];
        Asset R[] = new Asset[n2];


        for(int i=0;i<n1;i++)
            L[i] = arr[left+i];

        for(int j=0;j<n2;j++)
            R[j] = arr[mid+1+j];


        int i=0,j=0,k=left;


        // ascending order based on return rate
        while(i<n1 && j<n2) {

            if(L[i].returnRate <= R[j].returnRate) {

                arr[k] = L[i];
                i++;
            }
            else {

                arr[k] = R[j];
                j++;
            }

            k++;
        }


        while(i<n1) {
            arr[k] = L[i];
            i++;
            k++;
        }

        while(j<n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
    }



    // ---------- QUICK SORT (descending return + low volatility) ----------

    static void quickSort(Asset arr[], int low, int high) {

        if(low < high) {

            int pivotIndex = partition(arr, low, high);

            quickSort(arr, low, pivotIndex-1);
            quickSort(arr, pivotIndex+1, high);
        }
    }



    static int partition(Asset arr[], int low, int high) {

        Asset pivot = arr[high];

        int i = low-1;

        for(int j=low;j<high;j++) {

            // descending return
            // if equal return, smaller volatility first
            if(arr[j].returnRate > pivot.returnRate ||
                    (arr[j].returnRate == pivot.returnRate &&
                            arr[j].volatility < pivot.volatility)) {

                i++;

                Asset temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }


        Asset temp = arr[i+1];
        arr[i+1] = arr[high];
        arr[high] = temp;

        return i+1;
    }



    static void printAssets(Asset arr[]) {

        for(int i=0;i<arr.length;i++) {

            System.out.print(
                    arr[i].name +
                            ":" +
                            arr[i].returnRate +
                            "%"
            );

            if(i<arr.length-1)
                System.out.print(", ");
        }
    }



    public static void main(String args[]) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of assets: ");
        int n = sc.nextInt();

        Asset arr[] = new Asset[n];


        for(int i=0;i<n;i++) {

            System.out.println("\nAsset " + (i+1));

            System.out.print("Enter name: ");
            String name = sc.next();

            System.out.print("Enter return rate (%): ");
            double r = sc.nextDouble();

            System.out.print("Enter volatility: ");
            double v = sc.nextDouble();

            arr[i] = new Asset(name, r, v);
        }



        // merge sort
        mergeSort(arr,0,n-1);

        System.out.print("\nMerge: [");
        printAssets(arr);
        System.out.println("]");



        // quick sort
        quickSort(arr,0,n-1);

        System.out.print("\nQuick (desc): [");
        printAssets(arr);
        System.out.println("]");


        sc.close();
    }
}

