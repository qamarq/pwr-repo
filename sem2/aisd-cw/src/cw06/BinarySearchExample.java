package cw06;

import java.util.Arrays;

public class BinarySearchExample {
    public static void main(String[] args) {
        int[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int target = 7;
        int index = Arrays.binarySearch(array, target);

        if (index >= 0) {
            System.out.println("Element " + target + " znaleziony na indeksie: " + index);
        } else {
            System.out.println("Element " + target + " nie został znaleziony.");
        }
    }
}
