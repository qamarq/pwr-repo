package cw05;

import utils.OneWayLinkedList;
import utils.IList;
import utils.ListSorter;

import java.util.Comparator;

public class Heapsort<T> implements ListSorter<T> {
    private final Comparator<T> _comparator;

    public Heapsort(Comparator<T> comparator) {
        this._comparator = comparator;
    }

    private void swap(IList<T> list, int left, int right) {
        T temp = list.get(left);
        list.set(left, list.get(right));
        list.set(right, temp);
    }

    public void sink(IList<T> heap, int idx, int n) {
        int idxOfBigger = 2 * idx + 1;
        if (idxOfBigger < n) {
            if (idxOfBigger + 1 < n &&
                    _comparator.compare(heap.get(idxOfBigger), heap.get(idxOfBigger + 1)) < 0)
                idxOfBigger++;
            if (_comparator.compare(heap.get(idx), heap.get(idxOfBigger)) < 0) {
                swap(heap, idx, idxOfBigger);
                sink(heap, idxOfBigger, n);
            }
        }
    }

    void heapAdjustment(IList<T> heap, int n) {
        for (int i = (n - 1) / 2; i >= 0; i--)
            sink(heap, i, n);
        System.out.println("Po zbudowaniu kopca: " + heap);
    }

    @Override
    public IList<T> sort(IList<T> list) {
        heapsort(list, list.size());
        return list;
    }

    private void heapsort(IList<T> heap, int n) {
        heapAdjustment(heap, n);
        for (int i = n - 1; i > 0; i--) {
            swap(heap, i, 0);
            sink(heap, 0, i);
            System.out.println("Po przeniesieniu max do końca i rekopcowaniu: " + heap);
        }
    }

    public static void main(String[] args) {
        OneWayLinkedList<Integer> list = new OneWayLinkedList<>(new Integer[]{5, 4, 2, 8, 7, 1, 3, 6});

        ListSorter<Integer> sorter = new Heapsort<>(Integer::compareTo);
        sorter.sort(list);

        System.out.println("Sorted list: " + list);
    }
}
