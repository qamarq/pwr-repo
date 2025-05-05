package cw06;

import java.util.Arrays;

public class LinearSearch {
    public static int select(int[] arr, int k) {
        if (k < 1 || k > arr.length) {
            throw new IllegalArgumentException("Nieprawidłowa wartość k");
        }
        return select(arr, 0, arr.length - 1, k - 1); // k-1 dla indeksów 0-based
    }

    private static int select(int[] arr, int left, int right, int k) {
        if (left == right) {
            return arr[left];
        }

        int pivot = findMedianOfMedians(arr, left, right);
        int partitionIndex = partition(arr, left, right, pivot);

        if (k == partitionIndex) {
            return arr[k];
        } else if (k < partitionIndex) {
            return select(arr, left, partitionIndex - 1, k);
        } else {
            return select(arr, partitionIndex + 1, right, k);
        }
    }

    private static int findMedianOfMedians(int[] arr, int left, int right) {
        int numElements = right - left + 1;
        int numGroups = (int) Math.ceil(numElements / 5.0);
        int[] medians = new int[numGroups];

        for (int i = 0; i < numGroups; i++) {
            int groupStart = left + i * 5;
            int groupEnd = Math.min(groupStart + 4, right);
            medians[i] = findGroupMedian(arr, groupStart, groupEnd);
        }

        return select(medians, 0, medians.length - 1, medians.length / 2);
    }

    private static int findGroupMedian(int[] arr, int start, int end) {
        Arrays.sort(arr, start, end + 1);
        int length = end - start + 1;
        int mid = start + (length / 2); // Dla parzystych wybiera wyższą medianę
        return arr[mid];
    }

    private static int partition(int[] arr, int left, int right, int pivotValue) {
        int pivotIndex = findPivotIndex(arr, left, right, pivotValue);
        swap(arr, pivotIndex, right);

        int storeIndex = left;
        for (int i = left; i < right; i++) {
            if (arr[i] < pivotValue) {
                swap(arr, storeIndex, i);
                storeIndex++;
            }
        }
        swap(arr, storeIndex, right);
        return storeIndex;
    }

    private static int findPivotIndex(int[] arr, int left, int right, int pivotValue) {
        for (int i = left; i <= right; i++) {
            if (arr[i] == pivotValue) {
                return i;
            }
        }
        throw new RuntimeException("Pivot nie znaleziony");
    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void main(String[] args) {
        int[] arr = {10, 7, 8, 3, 1, 2, 9, 5, 6, 4};
        int k = 4;
        int result = select(arr, k);
        System.out.println(k + "-ty najmniejszy element: " + result); // Powinno zwrócić 4
    }
}

