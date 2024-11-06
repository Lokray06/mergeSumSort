# Sum-Based Sorting Algorithm

This repository contains an implementation of a novel sorting algorithm that sorts an array of integers based on the sums of its halves. The algorithm recursively splits the array, compares the sums of the two halves, and rearranges them until the entire array is sorted.

## Algorithm Explanation

The sorting algorithm follows these steps:

1. **Splitting**: The input array is recursively divided into two halves until each half contains one or zero elements (the base case).
2. **Summing**: For each recursive step, the sums of the left and right halves are calculated.
3. **Swapping**: If the left half's sum is greater than the right half's sum, the two halves are swapped.
4. **Recursive Sorting**: The algorithm recursively sorts each half, ensuring that the final sorted array maintains order.
5. **Merging**: Once sorted, the two halves are merged back together into a single sorted array.

This algorithm's core idea is unique because it uses the sums of array segments to dictate their order, rather than relying solely on direct comparisons of individual elements.
![SumMergeSort (1)](https://github.com/user-attachments/assets/cf5ffb75-aba1-476b-91ea-f3ea39f612c9)


## Complexity Analysis

- **Time Complexity**: The time complexity of this algorithm is $\(O(n \log n)\)$. This is due to the recursive splitting (similar to merge sort) and the overhead of summing elements at each level of recursion.
  
- **Space Complexity**: The space complexity is $\(O(n)\)$ due to the temporary storage needed for merging the two halves.

## Usage

To use this algorithm, you can run the provided `main` method, which generates a random array of integers and sorts it using the sum-based sorting algorithm. 

### Example

```java
public static void main(String[] args) {
    int[] array = generateRandomArray(10);
    System.out.println("Original array: " + Arrays.toString(array));
    
    sortBySums(array);
    
    System.out.println("Sorted array: " + Arrays.toString(array));
}
