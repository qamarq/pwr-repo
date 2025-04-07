package cw04;

import utils.OneWayLinkedList;
import utils.IList;
import utils.ListSorter;

import java.util.Comparator;

public class ImprovedShakersort<T> implements ListSorter<T> {
    private final Comparator<T> _comparator;

    public ImprovedShakersort(Comparator<T> comparator) {
        _comparator = comparator;
    }

    public IList<T> sort(IList<T> list) {
        for (int i = 1; i <= list.size() / 2; i++) {
            boolean swapped = false;
            for (int j = i - 1; j < list.size() - i; j++) {
                if (_comparator.compare(list.get(j), list.get(j + 1)) > 0) {
                    swap(list, j, j + 1);
                    swapped = true;
                }
            }
            System.out.println(list);
            if (!swapped) break;

            swapped = false;
            for (int j = list.size() - i - 1; j >= i; j--) {
                if (_comparator.compare(list.get(j), list.get(j - 1)) < 0) {
                    swap(list, j, j - 1);
                    swapped = true;
                }
            }
            System.out.println(list);
            if (!swapped) break;
        }
        return list;
    }

    private void swap(IList<T> list, int left, int right) {
        T temp = list.get(left);
        list.set(left, list.get(right));
        list.set(right, temp);
    }

    public static void main(String[] args) {
        OneWayLinkedList<Integer> list = new OneWayLinkedList<>(new Integer[]{76, 71, 5, 57, 12, 50, 20, 93, 20, 55, 62, 3});

        ListSorter<Integer> sorter = new ImprovedShakersort<>(Integer::compareTo);
        sorter.sort(list);

        System.out.println("Sorted list: " + list);
    }
}
