package cw05;

import utils.OneWayLinkedList;
import utils.IList;
import utils.ListSorter;

import java.util.Comparator;

public class Countingsort<T> implements ListSorter<T> {
    private final Comparator<T> _comparator;

    public Countingsort(Comparator<T> comparator) {
        this._comparator = comparator;
    }

    private void swap(IList<T> list, int left, int right) {
        T temp = list.get(left);
        list.set(left, list.get(right));
        list.set(right, temp);
    }

    @Override
    public IList<T> sort(IList<T> list) {
        if (list == null || list.size() == 0) {
            return list;
        }

        int n = list.size();
        int max = (Integer) list.get(0);
        for (int i = 1; i < n; i++) {
            if (_comparator.compare(list.get(i), (T) Integer.valueOf(max)) > 0) {
                max = (Integer) list.get(i);
            }
        }

        countingSort(list, max);
        return list;
    }

    private void countingSort(IList<T> list, int k) {
        k++;
        int n = list.size();
        int[] pos = new int[k];
        T[] result = (T[]) new Object[n];
        int i, j;
        for (i = 0; i < k; i++) {
            pos[i] = 0;
        }

        for (j = 0; j < n; j++) {
            pos[(Integer) list.get(j)]++;
        }
        System.out.println("tablica pos (poczatkowo):\n" + java.util.Arrays.toString(pos));

        pos[0]--;
        for (i = 1; i < k; i++) {
            pos[i] += pos[i - 1];
        }
        System.out.println("tablica pos (po akumulacji):\n" + java.util.Arrays.toString(pos));

        for (j = n - 1; j >= 0; j--) {
            result[pos[(Integer) list.get(j)]] = list.get(j);
            pos[(Integer) list.get(j)]--;
            System.out.println(java.util.Arrays.toString(result));
        }

        for (j = 0; j < n; j++) {
            list.set(j, result[j]);
        }
    }

    public static void main(String[] args) {
        OneWayLinkedList<Integer> list = new OneWayLinkedList<>(new Integer[]{0, 2, 1, 0, 4, 4, 2, 1, 1, 1});

        ListSorter<Integer> sorter = new Countingsort<>(Integer::compareTo);
        sorter.sort(list);

        System.out.println("Sorted list: " + list);
    }
}
