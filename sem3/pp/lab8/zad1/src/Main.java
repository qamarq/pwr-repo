import java.util.Arrays;

public class Main {
    public static int[] insert(int[] arr, int x) {
        return insertHelper(arr, x, 0);
    }

    private static int[] insertHelper(int[] arr, int x, int index) {
        if (index == arr.length) {
            int[] result = new int[arr.length + 1];
            for (int i = 0; i < arr.length; i++) {
                result[i] = arr[i];
            }
            result[arr.length] = x;
            return result;
        }

        if (x <= arr[index]) {
            int[] result = new int[arr.length + 1];
            result[index] = x;
            for (int i = index; i < arr.length; i++) {
                result[i + 1] = arr[i];
            }
            for (int i = 0; i < index; i++) {
                result[i] = arr[i];
            }
            return result;
        } else {
            return insertHelper(arr, x, index + 1);
        }
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(insert(new int[]{1, 3, 5, 7}, 4)));
        System.out.println(Arrays.toString(insert(new int[]{}, 10)));
        System.out.println(Arrays.toString(insert(new int[]{2, 4, 6}, 1)));
        System.out.println(Arrays.toString(insert(new int[]{2, 4, 6}, 8)));
        System.out.println(Arrays.toString(insert(new int[]{1, 2, 3}, 2)));
        System.out.println(Arrays.toString(insert(new int[]{1, 2, 3}, -2)));
    }
}