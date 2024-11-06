import java.util.Arrays;
import java.util.Random;

public class Main {

    // Generates an array of length `n` with random values from 0 to 100
    public static int[] generateRandomArray(int n) {
        Random random = new Random();
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

        // Sum the left and right halves
        int leftSum = sum(array, start, mid);
        int rightSum = sum(array, mid + 1, end);

        // If left sum is greater, swap the halves
        if (leftSum > rightSum) {
            swapHalves(array, start, mid, end);
        }

        // Recursively sort each half
        recursiveSumSort(array, start, mid);
        recursiveSumSort(array, mid + 1, end);

        // Merge the two halves
        merge(array, start, mid, end);
    }

    // Helper method to calculate the sum of elements in the array from `start` to `end`
    private static int sum(int[] array, int start, int end) {
        int total = 0;
        for (int i = start; i <= end; i++) {
            total += array[i];
        }
        return total;
    }

    // Helper method to swap the left and right halves of the array between `start`, `mid`, and `end`
    private static void swapHalves(int[] array, int start, int mid, int end) {
        int leftSize = mid - start + 1;
        int[] temp = new int[leftSize];

        // Copy left half to temporary array
        System.arraycopy(array, start, temp, 0, leftSize);

        // Move right half to left position
        for (int i = 0; i < end - mid; i++) {
            array[start + i] = array[mid + 1 + i];
        }

        // Move temp (left half) to right position
        System.arraycopy(temp, 0, array, start + (end - mid), leftSize);
    }

    // Merges two sorted halves of the array
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
        int[] array = generateRandomArray(50);
        System.out.println("Original array: " + Arrays.toString(array));
        sortBySums(array);
        System.out.println("Sorted array: " + Arrays.toString(array));
    }
}
