package lab4;

import core.AbstractSwappingSortingAlgorithm;

import java.util.Comparator;
import java.util.List;

public class SelectionSortWithMax<T> extends AbstractSwappingSortingAlgorithm<T> {

    public SelectionSortWithMax(Comparator<? super T> comparator) {
        super(comparator);
    }

    @Override
    public List<T> sort(List<T> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            int maxIndex = i;
            for (int j = i + 1; j < list.size(); j++) {
                if (compare(list.get(j), list.get(maxIndex)) > 0) {
                    maxIndex = j;
                }
            }
            if (maxIndex != i) {
                swap(list, i, maxIndex);
            }
        }
        return list;
    }
}