package cw04;

import utils.OneWayLinkedList;
import utils.IList;
import utils.ListSorter;

import java.util.Comparator;

public class PermutationSort<T> implements ListSorter<T> {
    private final Comparator<T> _comparator;

    public PermutationSort(Comparator<T> comparator) {
        _comparator = comparator;
    }

    private IList<T> sortedResult = null;

    public IList<T> sort(IList<T> list) {
        sortedResult = null;
        generatePermutations(list, list.size());
        return sortedResult != null ? sortedResult : list;
    }

    private void generatePermutations(IList<T> list, int n) {
        if (sortedResult != null) return;
        if (n == 1) {
            if (isSorted(list)) {
                sortedResult = list;
            }
            return;
        }
        for (int i = 0; i < n; i++) {
            generatePermutations(list, n - 1);
            if (sortedResult != null) return;
            if (n % 2 == 0) {
                swap(list, i, n - 1);
            } else {
                swap(list, 0, n - 1);
            }
        }
    }


    private void swap(IList<T> list, int left, int right) {
        T temp = list.get(left);
        list.set(left, list.get(right));
        list.set(right, temp);
    }

    private boolean isSorted(IList<T> list) {
        for (int i = 1; i < list.size(); i++) {
            if (_comparator.compare(list.get(i - 1), list.get(i)) > 0) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        OneWayLinkedList<Integer> list = new OneWayLinkedList<>(new Integer[]{1, 7, 4});

        ListSorter<Integer> sorter = new PermutationSort<>(Integer::compareTo);
        sorter.sort(list);

        System.out.println("Sorted list: " + list);
    }
}
