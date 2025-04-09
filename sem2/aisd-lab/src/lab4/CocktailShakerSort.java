package lab4;

import core.AbstractSwappingSortingAlgorithm;

import java.util.Comparator;
import java.util.List;

public class CocktailShakerSort<T> extends AbstractSwappingSortingAlgorithm<T> {

    public CocktailShakerSort(Comparator<? super T> comparator) {
        super(comparator);
    }

    @Override
    public List<T> sort(List<T> list) {
        boolean swapped;
        int start = 0;
        int end = list.size() - 1;

        do {
            swapped = false;

            // lewo -> prawo
            for (int i = start; i < end; i++) {
                if (compare(list.get(i), list.get(i + 1)) > 0) {
                    swap(list, i, i + 1);
                    swapped = true;
                }
            }
            end--;

            // prawo -> lewo
            for (int i = end; i > start; i--) {
                if (compare(list.get(i), list.get(i - 1)) < 0) {
                    swap(list, i, i - 1);
                    swapped = true;
                }
            }
            start++;

        } while (swapped);
        return list;
    }
}