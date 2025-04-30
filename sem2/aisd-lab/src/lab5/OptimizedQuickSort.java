package lab5;

import core.AbstractSwappingSortingAlgorithm;
import java.util.*;

interface PivotSelector<T> {
    T select(List<T> list);
}

public class OptimizedQuickSort<T> extends AbstractSwappingSortingAlgorithm<T> {
    private final PivotSelector<T> pivotSelector;

    public OptimizedQuickSort(Comparator<? super T> comparator, PivotSelector<T> pivotSelector) {
        super(comparator);
        this.pivotSelector = pivotSelector;
    }

    @Override
    public List<T> sort(List<T> list) {
        quickSort(list, 0, list.size() - 1);
        return list;
    }

    private void quickSort(List<T> list, int low, int high) {
        if (low < high) {
            int pi = partition(list, low, high);

            if (pi == low) {
                quickSort(list, pi + 1, high);
            } else if (pi == high) {
                quickSort(list, low, pi - 1);
            } else {
                quickSort(list, low, pi - 1);
                quickSort(list, pi + 1, high);
            }
        }
    }

    private int partition(List<T> list, int low, int high) {
        T pivot = pivotSelector.select(list.subList(low, high + 1));
        int pivotIndex = -1;

        for (int i = low; i <= high; i++) {
            if (compare(list.get(i), pivot) == 0) {
                pivotIndex = i;
                break;
            }
        }

        if (pivotIndex == -1) {
            throw new IllegalStateException("Pivot not found in list range");
        }

        swap(list, pivotIndex, high);

        int i = low - 1;
        ListIterator<T> jIter = list.listIterator(low);
        for (int j = low; j < high; j++) {
            T current = jIter.next();
            if (compare(current, pivot) <= 0) {
                i++;
                swap(list, i, j);
            }
        }

        swap(list, i + 1, high);
        return i + 1;
    }


    public static class FirstPivotSelector<T> implements PivotSelector<T> {
        public T select(List<T> list) {
            return list.get(0);
        }
    }

    public static class RandomPivotSelector<T> implements PivotSelector<T> {
        public T select(List<T> list) {
            return list.get(new Random().nextInt(list.size()));
        }
    }
}
