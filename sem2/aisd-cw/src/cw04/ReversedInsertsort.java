package cw04;

import utils.OneWayLinkedList;
import utils.IList;
import utils.ListSorter;

import java.util.Comparator;

public class ReversedInsertsort<T> implements ListSorter<T> {
    private final Comparator<T> _comparator;

    public ReversedInsertsort(Comparator<T> comparator) {
        _comparator = comparator;
    }

    @Override
    public IList<T> sort(IList<T> list) {
        for (int i = list.size() - 1; i >= 0; i--) {
            T value = list.get(i), temp;
            int j;
            for (j = i; j < list.size() - 1 && _comparator.compare(value, temp = list.get(j + 1)) < 0; j++) {
                list.set(j, temp);
            }
            list.set(j, value);
            System.out.println(list);
        }
        return list;
    }

    public static void main(String[] args) {
        OneWayLinkedList<Integer> list = new OneWayLinkedList<>(new Integer[]{3, 76, 71, 5, 57, 12, 50, 20, 93, 20, 4, 62});

        ListSorter<Integer> sorter = new ReversedInsertsort<>(Integer::compareTo);
        sorter.sort(list);

        System.out.println("Sorted list: " + list);
    }
}
