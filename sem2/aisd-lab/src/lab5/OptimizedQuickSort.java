package lab5;

import core.AbstractSwappingSortingAlgorithm;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

interface PivotSelector<T> {
    T select(List<T> list);
}

public class OptimizedQuickSort<T> extends AbstractSwappingSortingAlgorithm<T> {
    private final PivotSelector<T> pivotSelector;
    private final Random random = new Random();

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
            quickSort(list, low, pi - 1);
            quickSort(list, pi + 1, high);
        }
    }

    private int partition(List<T> list, int low, int high) {
        T pivot = pivotSelector.select(list.subList(low, high + 1));
        int i = low - 1;

        for (int j = low; j <= high; j++) {
            if (compare(list.get(j), pivot) <= 0) {
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