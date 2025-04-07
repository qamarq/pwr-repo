package cw04;

import utils.OneWayLinkedList;
import utils.IList;
import utils.ListSorter;

import java.util.Comparator;

public class ReversedBubblesort<T> implements ListSorter<T> {
    private final Comparator<T> _comparator;

    public ReversedBubblesort(Comparator<T> comparator) {
        _comparator = comparator;
    }

    public IList<T> sort(IList<T> list) {
        int lastSwap = list.size() - 1;
        while (lastSwap > 0) {
            int end = lastSwap;
            lastSwap = 0;
            for (int left = 0; left < end; ++left) {
                if (_comparator.compare(list.get(left), list.get(left + 1)) < 0) {
                    T temp = list.get(left);
                    while (left < end && _comparator.compare(temp, list.get(left + 1)) < 0) {
                        list.set(left, list.get(left + 1));
                        left++;
                    }
                    lastSwap = left;
                    list.set(left, temp);
                }
            }
            System.out.println(list);
        }
        return list;
    }

    public static void main(String[] args) {
        OneWayLinkedList<Integer> list = new OneWayLinkedList<>(new Integer[]{76, 20, 5, 57, 12, 50, 20, 93, 44, 55, 62, 3});

        ListSorter<Integer> sorter = new ReversedBubblesort<>(Integer::compareTo);
        sorter.sort(list);

        System.out.println("Sorted list: " + list);
    }
}