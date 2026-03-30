import java.util.*;

// class to represent each trade
class Trade {

    String id;
    int volume;

    Trade(String id, int volume) {
        this.id = id;
        this.volume = volume;
    }
}

public class TradeVolumeAnalysis {


    // ---------------- MERGE SORT ----------------

    static void mergeSort(Trade arr[], int left, int right) {

        if(left < right) {

            int mid = (left + right) / 2;

            mergeSort(arr, left, mid);
            mergeSort(arr, mid+1, right);

            merge(arr, left, mid, right);
        }
    }


    static void merge(Trade arr[], int left, int mid, int right) {

        int n1 = mid - left + 1;
        int n2 = right - mid;

        Trade L[] = new Trade[n1];
        Trade R[] = new Trade[n2];

        for(int i=0;i<n1;i++)
            L[i] = arr[left+i];

        for(int j=0;j<n2;j++)
            R[j] = arr[mid+1+j];


        int i=0, j=0, k=left;

        while(i<n1 && j<n2) {

            if(L[i].volume <= R[j].volume) {

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



    // ---------------- QUICK SORT ----------------

    static void quickSort(Trade arr[], int low, int high) {

        if(low < high) {

            int pivotIndex = partition(arr, low, high);

            quickSort(arr, low, pivotIndex-1);

            quickSort(arr, pivotIndex+1, high);
        }
    }


    // descending order partition
    static int partition(Trade arr[], int low, int high) {

        int pivot = arr[high].volume;

        int i = low - 1;

        for(int j=low;j<high;j++) {

            if(arr[j].volume >= pivot) {

                i++;

                Trade temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        Trade temp = arr[i+1];
        arr[i+1] = arr[high];
        arr[high] = temp;

        return i+1;
    }



    // merge two sorted lists
    static Trade[] mergeLists(Trade a[], Trade b[]) {

        Trade merged[] = new Trade[a.length + b.length];

        int i=0,j=0,k=0;

        while(i<a.length && j<b.length) {

            if(a[i].volume <= b[j].volume)
                merged[k++] = a[i++];

            else
                merged[k++] = b[j++];
        }

        while(i<a.length)
            merged[k++] = a[i++];

        while(j<b.length)
            merged[k++] = b[j++];

        return merged;
    }



    static int totalVolume(Trade arr[]) {

        int sum = 0;

        for(Trade t : arr)
            sum += t.volume;

        return sum;
    }



    static void printTrades(Trade arr[]) {

        for(int i=0;i<arr.length;i++) {

            System.out.print(
                    arr[i].id +
                            ":" +
                            arr[i].volume
            );

            if(i<arr.length-1)
                System.out.print(", ");
        }
    }



    public static void main(String args[]) {

        Scanner sc = new Scanner(System.in);


        System.out.print("Enter number of morning trades: ");
        int n1 = sc.nextInt();

        Trade morning[] = new Trade[n1];

        for(int i=0;i<n1;i++) {

            System.out.println("\nMorning trade " + (i+1));

            System.out.print("Enter trade id: ");
            String id = sc.next();

            System.out.print("Enter volume: ");
            int vol = sc.nextInt();

            morning[i] = new Trade(id, vol);
        }



        System.out.print("\nEnter number of afternoon trades: ");
        int n2 = sc.nextInt();

        Trade afternoon[] = new Trade[n2];

        for(int i=0;i<n2;i++) {

            System.out.println("\nAfternoon trade " + (i+1));

            System.out.print("Enter trade id: ");
            String id = sc.next();

            System.out.print("Enter volume: ");
            int vol = sc.nextInt();

            afternoon[i] = new Trade(id, vol);
        }



        // ---- Merge Sort Ascending ----
        mergeSort(morning, 0, n1-1);

        System.out.print("\nMergeSort: [");
        printTrades(morning);
        System.out.println("] // Stable");



        // ---- Quick Sort Descending ----
        quickSort(morning, 0, n1-1);

        System.out.print("\nQuickSort (desc): [");
        printTrades(morning);
        System.out.println("]");



        // ---- Merge morning + afternoon ----
        mergeSort(afternoon, 0, n2-1);

        Trade merged[] = mergeLists(morning, afternoon);

        int total = totalVolume(merged);

        System.out.println(
                "\nMerged morning+afternoon total: "
                        + total
        );


        sc.close();
    }
}

