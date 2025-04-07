package cw05;

import utils.OneWayLinkedList;
import utils.IList;
import utils.ListSorter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

public class MergesortRecursive<T> implements ListSorter<T> {
    private final Comparator<T> _comparator;

    public MergesortRecursive(Comparator<T> comparator) {
        this._comparator = comparator;
    }

    // wynikiem jest nowa lista
    public IList<T> sort(IList<T> list) {
        IList<T> sortedList = mergesort(list, 0, list.size() - 1);
        list.clear();
        for (T item : sortedList) {
            list.add(item);
        }
        return list;
    }

    private IList<T> mergesort(IList<T> list, int startIndex, int endIndex) {
        if (startIndex == endIndex) {
            IList<T> result = new OneWayLinkedList<>();
            result.add(list.get(startIndex));
            return result;
        }
        int splitIndex = startIndex + (endIndex - startIndex) / 2;
        return merge(mergesort(list, startIndex, splitIndex), mergesort(list, splitIndex + 1, endIndex));
    }

    @SuppressWarnings("unchecked")
    private IList<T> merge(IList<T> left, IList<T> right) {
        // mimo wszystko musimy się zdecydować na konkretną implementację listy
        IList<T> result = (IList<T>) new ArrayList<Object>();
        Iterator<T> l = left.iterator();
        Iterator<T> r = right.iterator();
        T elemL = null, elemR = null;
        // musimy opóźnić wychodzenie z pętli do czasu dodania do wyniku
        // ostatniego elementu jednego z ciągów
        boolean contL, contR;
        if (contL = l.hasNext()) elemL = l.next();
        if (contR = r.hasNext()) elemR = r.next();
        while (contL && contR) {
            if (_comparator.compare(elemL, elemR) <= 0) {
                result.add(elemL);
                if (contL = l.hasNext()) elemL = l.next();
                else result.add(elemR);
            } //już odczytany element drugiej listy do wyniku
            else {
                result.add(elemR);
                if (contR = r.hasNext()) elemR = r.next();
                else result.add(elemL);
            } //już odczytany element pierwszej listy do wyniku
        }
        while (l.hasNext()) result.add(l.next());
        while (r.hasNext()) result.add(r.next());
        return result;
    }

    public static void main(String[] args) {
        OneWayLinkedList<Integer> list = new OneWayLinkedList<>(new Integer[]{5, 4, 2, 8, 7, 1, 3, 6});

        ListSorter<Integer> sorter = new MergesortRecursive<>(Integer::compareTo);
        sorter.sort(list);

        System.out.println("Sorted list: " + list);
    }
}
