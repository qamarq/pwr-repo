package cw04;

import utils.OneWayLinkedList;
import utils.IList;
import utils.ListSorter;

import java.util.Comparator;

public class Shakersort<T> implements ListSorter<T> {
    private final Comparator<T> _comparator;

    public Shakersort(Comparator<T> comparator) {
        _comparator = comparator;
    }

    public IList<T> sort(IList<T> list) {
        for (int i = 1; i <= list.size() / 2; i++) {
            for (int j = 0; j < list.size() - i; j++) {
                if (_comparator.compare(list.get(j), list.get(j + 1)) > 0) {
                    swap(list, j, j + 1);
                }
            }
            System.out.println(list);

            for (int j = list.size() - 1; j >= 1; j--) {
                if (_comparator.compare(list.get(j), list.get(j - 1)) < 0) {
                    swap(list, j, j - 1);
                }
            }
            System.out.println(list);
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

        ListSorter<Integer> sorter = new Shakersort<>(Integer::compareTo);
        sorter.sort(list);

        System.out.println("Sorted list: " + list);
    }
}