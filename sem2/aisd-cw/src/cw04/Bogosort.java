package cw04;

import utils.OneWayLinkedList;
import utils.IList;
import utils.ListSorter;

import java.util.Comparator;

public class Bogosort<T> implements ListSorter<T> {
    private final Comparator<T> _comparator;

    public Bogosort(Comparator<T> comparator) {
        _comparator = comparator;
    }

    public IList<T> sort(IList<T> list) {
        int numberOfIterations = 0;
        while(!isSorted(list)) {
            shuffle(list);
            numberOfIterations++;
        }
        System.out.println("Number of iterations: " + numberOfIterations);
        return list;
    }

    public void shuffle(IList<T> list) {
        for (int i = 0; i < list.size(); i++) {
            int randomIndex = (int) (Math.random() * list.size());
            swap(list, i, randomIndex);
        }
    }

    private boolean isSorted(IList<T> list) {
        for (int i = 1; i < list.size(); i++) {
            if (_comparator.compare(list.get(i - 1), list.get(i)) > 0) {
                return false;
            }
        }
        return true;
    }

    private void swap(IList<T> list, int left, int right) {
        T temp = list.get(left);
        list.set(left, list.get(right));
        list.set(right, temp);
    }

    public static void main(String[] args) {
        OneWayLinkedList<Integer> list = new OneWayLinkedList<>(new Integer[]{6, 7, 4});

        ListSorter<Integer> sorter = new Bogosort<>(Integer::compareTo);
        sorter.sort(list);

        System.out.println("Sorted list: " + list);
    }
}