package cw04;

import utils.OneWayLinkedList;
import utils.IList;
import utils.ListSorter;

import java.util.Comparator;

public class ReversedSelectsort<T> implements ListSorter<T> {
    private final Comparator<T> _comparator;

    public ReversedSelectsort(Comparator<T> comparator) {
        _comparator = comparator;
    }

    public IList<T> sort(IList<T> list) {
        int size = list.size();
        for (int slot = size - 1; slot >= 0; slot--) {
            int smallest = slot;
            for (int check = slot - 1; check >= 0; check--) {
                if (_comparator.compare(list.get(check), list.get(smallest)) < 0) {
                    smallest = check;
                }
            }
            swap(list, smallest, slot);
            System.out.println(list);
        }
        return list;
    }

    private void swap(IList<T> list, int left, int right) {
        if (left != right) {
            T temp = list.get(left);
            list.set(left, list.get(right));
            list.set(right, temp);
        }
    }

    public static void main(String[] args) {
        OneWayLinkedList<Integer> list = new OneWayLinkedList<>(new Integer[]{76, 71, 5, 57, 12, 50, 20, 3, 20, 55, 62, 53});

        ListSorter<Integer> sorter = new ReversedSelectsort<>(Integer::compareTo);
        sorter.sort(list);

        System.out.println("Sorted list: " + list);
    }
}
