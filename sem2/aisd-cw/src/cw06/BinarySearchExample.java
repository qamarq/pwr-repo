package cw06;

import java.util.Arrays;

public class BinarySearchExample {
    public static void main(String[] args) {
        int[] array = {3,7,1,8,5,4,2,6};
        int target = 7;
        int index = Arrays.binarySearch(array, target);

        if (index >= 0) {
            System.out.println("Element " + target + " znaleziony na indeksie: " + index);
        } else {
            System.out.println("Element " + target + " nie został znaleziony.");
        }
    }
}
