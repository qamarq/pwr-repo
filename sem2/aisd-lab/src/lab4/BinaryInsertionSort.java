package lab4;

import core.AbstractSwappingSortingAlgorithm;

import java.util.Comparator;
import java.util.List;

public class BinaryInsertionSort<T> extends AbstractSwappingSortingAlgorithm<T> {

    public BinaryInsertionSort(Comparator<? super T> comparator) {
        super(comparator);
    }

    @Override
    public List<T> sort(List<T> list) {
        for (int i = 1; i < list.size(); i++) {
            T key = list.get(i);
            int insertionPoint = binarySearch(list, key, 0, i - 1);

            // jedno w prawo
            for (int j = i; j > insertionPoint; j--) {
                swap(list, j, j - 1);
            }

            // i wstawismy w odpowiednie miejsce
            list.set(insertionPoint, key);
        }
        return list;
    }

    private int binarySearch(List<T> list, T key, int low, int high) {
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (compare(key, list.get(mid)) < 0) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return low;
    }
}
