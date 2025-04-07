package cw05;

import utils.OneWayLinkedList;
import utils.IList;
import utils.ListSorter;

import java.util.Comparator;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;

public class Quicksort<T> implements ListSorter<T> {
    private final Comparator<T> _comparator;
    private final Random random = new Random();

    public Quicksort(Comparator<T> comparator) {
        this._comparator = comparator;
    }

    public IList<T> sort(IList<T> list) {
        quicksort(list, 0, list.size());
        return list;
    }

    private void quicksort(IList<T> list, int startIndex, int endIndex) {
        if (endIndex - startIndex > 1) {
            int partition = partition(list, startIndex, endIndex);
            quicksort(list, startIndex, partition);
            quicksort(list, partition + 1, endIndex);
        }
    }

    private int partition(IList<T> list, int nFrom, int nTo) {
        int pivotIndex = choosePivotIndex(list, nFrom, nTo);
        swap(list, nFrom, pivotIndex);
        T value = list.get(nFrom);
        int idxBigger = nFrom + 1, idxLower = nTo - 1;
        do {
            while (idxBigger <= idxLower && _comparator.compare(list.get(idxBigger), value) <= 0)
                idxBigger++;
            while (_comparator.compare(list.get(idxLower), value) > 0)
                idxLower--;
            if (idxBigger < idxLower)
                swap(list, idxBigger, idxLower);
        } while (idxBigger < idxLower);
        swap(list, idxLower, nFrom);
        return idxLower;
    }

    private int choosePivotIndex(IList<T> list, int nFrom, int nTo) {
        int size = nTo - nFrom;
        if (size > 100) {
            ArrayList<Integer> indices = new ArrayList<>();
            while (indices.size() < 3) {
                int candidate = nFrom + random.nextInt(size);
                if (!indices.contains(candidate)) indices.add(candidate);
            }
            indices.sort((i1, i2) -> _comparator.compare(list.get(i1), list.get(i2)));
            return indices.get(1); // middle value
        } else {
            return nFrom + random.nextInt(size);
        }
    }

    private void swap(IList<T> list, int left, int right) {
        if (left != right) {
            T temp = list.get(left);
            list.set(left, list.get(right));
            list.set(right, temp);
        }
    }

    public static void main(String[] args) {
        OneWayLinkedList<Integer> list = new OneWayLinkedList<>(new Integer[]{5, 4, 2, 8, 7, 1, 3, 6});

        ListSorter<Integer> sorter = new Quicksort<>(Integer::compareTo);
        sorter.sort(list);

        System.out.println("Sorted list: " + list);
    }
}