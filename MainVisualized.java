import javax.swing.*;
import java.awt.*;
import java.util.Random;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainVisualized {
    private static SortingVisualizer visualizer;
    private static int arrayAccesses = 0;
    private static int comparisons = 0;
    private static int updateFrequency = 1000; // Frequency of repainting based on accesses
    private static int delay = 10; // Default delay in milliseconds
    private static byte[] array; // The array to be sorted

    // Generates an array of length `n` with random byte values from 0 to 100
    public static byte[] generateRandomArray(int n) {
        Random random = new Random();
        byte[] array = new byte[n];
        for (int i = 0; i < n; i++) {
            array[i] = (byte) random.nextInt(101); // Random numbers from 0 to 100
        }
        return array;
    }

    // Main sorting method
    public static void sortBySums(byte[] array) {
        arrayAccesses = 0;
        comparisons = 0;
        recursiveSumSort(array, 0, array.length - 1);
    }

    // Recursive method that performs sum-based sorting on the subarray from `start` to `end`
    private static void recursiveSumSort(byte[] array, int start, int end) {
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

        // Only repaint periodically to improve performance
        if (arrayAccesses % updateFrequency == 0) {
            visualizer.repaint();
            try {
                Thread.sleep(delay); // Delay between updates, controlled by the slider
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Helper method to calculate the sum of elements in the array from `start` to `end`
    private static int sum(byte[] array, int start, int end) {
        int total = 0;
        for (int i = start; i <= end; i++) {
            total += array[i];
            arrayAccesses++;
        }
        return total;
    }

    // Helper method to swap the left and right halves of the array between `start`, `mid`, and `end`
    private static void swapHalves(byte[] array, int start, int mid, int end) {
        int leftSize = mid - start + 1;
        byte[] temp = new byte[leftSize];

        // Copy left half to temporary array
        System.arraycopy(array, start, temp, 0, leftSize);
        arrayAccesses += leftSize;

        // Move right half to left position
        for (int i = 0; i < end - mid; i++) {
            array[start + i] = array[mid + 1 + i];
            arrayAccesses++;
        }

        // Move temp (left half) to right position
        System.arraycopy(temp, 0, array, start + (end - mid), leftSize);
        arrayAccesses += leftSize;
    }

    // Merges two sorted halves of the array
    private static void merge(byte[] array, int start, int mid, int end) {
        byte[] temp = new byte[end - start + 1];
        int i = start, j = mid + 1, k = 0;

        // Merge sorted halves into temporary array
        while (i <= mid && j <= end) {
            comparisons++;
            if (array[i] <= array[j]) {
                temp[k++] = array[i++];
            } else {
                temp[k++] = array[j++];
            }
            arrayAccesses++;
        }

        // Copy remaining elements from left half, if any
        while (i <= mid) {
            temp[k++] = array[i++];
            arrayAccesses++;
        }

        // Copy remaining elements from right half, if any
        while (j <= end) {
            temp[k++] = array[j++];
            arrayAccesses++;
        }

        // Copy sorted elements back into the original array
        System.arraycopy(temp, 0, array, start, temp.length);
        arrayAccesses += temp.length;
    }

    // Inner class for visualizing the sorting process
    private static class SortingVisualizer extends JPanel {
        private byte[] array;

        public SortingVisualizer(byte[] array) {
            this.array = array;
            setBackground(Color.BLACK);
            setDoubleBuffered(true); // Enable double buffering
        }

        public void setArray(byte[] array) {
            this.array = array;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.WHITE);

            int panelWidth = getWidth();
            int panelHeight = getHeight();
            double widthScale = (double) panelWidth / array.length; // Calculate width scale for bars
            double heightScale = (double) panelHeight / 100; // Assume max array value is 100

            for (int i = 0; i < array.length; i++) {
                int height = (int) (array[i] * heightScale); // Scale height for visibility
                int x = (int) (i * widthScale);
                int barWidth = (int) Math.max(1, widthScale); // Ensure bars are at least 1 pixel wide
                g.fillRect(x, panelHeight - height, barWidth, height);
            }

            g.drawString("Array Accesses: " + arrayAccesses + " Comparisons: " + comparisons, 10, 20);
        }
    }

    // Main function for testing
    public static void main(String[] args) {
        array = generateRandomArray(200); // Initial array

        // Set up JFrame and add SortingVisualizer panel
        JFrame frame = new JFrame("Sorting Visualizer");
        visualizer = new SortingVisualizer(array);

        // Create speed slider
        JSlider speedSlider = new JSlider(JSlider.HORIZONTAL, 1, 1000, 10);
        speedSlider.setMajorTickSpacing(10);
        speedSlider.setMinorTickSpacing(1);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);
        speedSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                delay = speedSlider.getValue(); // Adjust delay based on slider value
            }
        });

        // Create text field for array size and rerun button
        JTextField sizeField = new JTextField("200", 5);
        JButton rerunButton = new JButton("Rerun");
        rerunButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int newSize = Integer.parseInt(sizeField.getText());
                    array = generateRandomArray(newSize);
                    visualizer.setArray(array);
                    arrayAccesses = 0;
                    comparisons = 0;

                    // Start sorting in a new thread to prevent blocking the UI
                    new Thread(() -> {
                        sortBySums(array);
                        visualizer.repaint(); // Final repaint after sorting is complete
                    }).start();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid integer for the array size.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Layout setup
        JPanel controlPanel = new JPanel();
        controlPanel.add(new JLabel("Array Size:"));
        controlPanel.add(sizeField);
        controlPanel.add(rerunButton);
        controlPanel.add(new JLabel("Speed:"));
        controlPanel.add(speedSlider);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(visualizer, BorderLayout.CENTER);
        frame.add(controlPanel, BorderLayout.SOUTH);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
