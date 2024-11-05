import java.util.Arrays;
import java.util.Random;

public class Main {

    static Random random = new Random();
    // Generates an array of length `n` with random values from 0 to 100
    public static int[] generateRandomArray(int n) {
        int[] array = new int[n];
        for (int i = 0; i < n; i++) {
            array[i] = random.nextInt(101); // Random numbers from 0 to 100
        }
        return array;
    }

    // Main sorting method
    public static void sortBySums(int[] array) {
        recursiveSumSort(array, 0, array.length - 1);
    }

    // Recursive method that performs sum-based sorting on the subarray from `start` to `end`
    private static void recursiveSumSort(int[] array, int start, int end) {
        if (end - start < 1) return;  // Base case: single element or empty

        int mid = (start + end) / 2;

        // Sum the left and right halves without calling another method
        int leftSum = 0, rightSum = 0;
        for (int i = start; i <= mid; i++) leftSum += array[i];
        for (int i = mid + 1; i <= end; i++) rightSum += array[i];

        // If left sum is greater, swap the halves in-place
        if (leftSum > rightSum) {
            swapHalvesInPlace(array, start, mid, end);
        }

        // Recursively sort each half
        recursiveSumSort(array, start, mid);
        recursiveSumSort(array, mid + 1, end);

        // Merge the two halves only if necessary
        if (leftSum > rightSum) {
            merge(array, start, mid, end);
        }
    }

    // Helper method to swap elements of the left and right halves in place
    private static void swapHalvesInPlace(int[] array, int start, int mid, int end) {
        int leftSize = mid - start + 1;
        int rightSize = end - mid;
        int limit = Math.min(leftSize, rightSize);

        for (int i = 0; i < limit; i++) {
            int temp = array[start + i];
            array[start + i] = array[mid + 1 + i];
            array[mid + 1 + i] = temp;
        }
    }

    // Merges two sorted halves of the array if necessary
    private static void merge(int[] array, int start, int mid, int end) {
        int[] temp = new int[end - start + 1];
        int i = start, j = mid + 1, k = 0;

        // Merge sorted halves into temporary array
        while (i <= mid && j <= end) {
            if (array[i] <= array[j]) {
                temp[k++] = array[i++];
            } else {
                temp[k++] = array[j++];
            }
        }

        // Copy remaining elements from left half, if any
        while (i <= mid) {
            temp[k++] = array[i++];
        }

        // Copy remaining elements from right half, if any
        while (j <= end) {
            temp[k++] = array[j++];
        }

        // Copy sorted elements back into the original array
        System.arraycopy(temp, 0, array, start, temp.length);
    }

    // Main function for testing
    public static void main(String[] args) {
        int[] array = generateRandomArray(10);
        System.out.println("Original array: " + Arrays.toString(array));
        
        // Record the start time
        long startTime = System.currentTimeMillis();
        
        // Sort the array
        sortBySums(array);
        
        // Record the end time
        long endTime = System.currentTimeMillis();
        
        System.out.println("Sorted array: " + Arrays.toString(array));
        System.out.println("Time taken to sort: " + (endTime - startTime) + " milliseconds");
    }
}
