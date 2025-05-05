package cw06;

public class LinearSearch {
    public static int findElement(int[] array, int target) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1; // element nie został znaleziony
    }

    public static void main(String[] args) {
        int[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int target = 7;
        int index = findElement(array, target);
        if (index != -1) {
            System.out.println("Element znaleziony na indeksie: " + index);
        } else {
            System.out.println("Element nie został znaleziony.");
        }
    }
}

