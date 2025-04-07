package cw05;

import utils.OneWayLinkedList;
import utils.IList;
import utils.ListSorter;

import java.util.Comparator;

public class Bucketsort<T> implements ListSorter<T> {
    private final Comparator<T> _comparator;

    public Bucketsort(Comparator<T> comparator) {
        this._comparator = comparator;
    }

    private void swap(IList<T> list, int left, int right) {
        T temp = list.get(left);
        list.set(left, list.get(right));
        list.set(right, temp);
    }

    @Override
    public IList<T> sort(IList<T> list) {
        return list;
    }

    public static void main(String[] args) {
        OneWayLinkedList<Integer> list = new OneWayLinkedList<>(new Integer[]{5, 4, 2, 8, 7, 1, 3, 6});

        ListSorter<Integer> sorter = new Bucketsort<>(Integer::compareTo);
        sorter.sort(list);

        System.out.println("Sorted list: " + list);
    }
}