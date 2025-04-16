package lab5;

import core.AbstractSortingAlgorithm;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ThreeWayMergeSort<T> extends AbstractSortingAlgorithm<T> {

    public ThreeWayMergeSort(Comparator<? super T> comparator) {
        super(comparator);
    }

    @Override
    public List<T> sort(List<T> list) {
        if (list.size() <= 1) return list;

        int mid1 = list.size() / 3;
        int mid2 = 2 * list.size() / 3;

        List<T> left = sort(new ArrayList<>(list.subList(0, mid1)));
        List<T> middle = sort(new ArrayList<>(list.subList(mid1, mid2)));
        List<T> right = sort(new ArrayList<>(list.subList(mid2, list.size())));

        return merge(left, middle, right);
    }

    private List<T> merge(List<T> left, List<T> middle, List<T> right) {
        List<T> result = new ArrayList<>();
        int i = 0, j = 0, k = 0;

        while (i < left.size() || j < middle.size() || k < right.size()) {
            T min = null;

            if (i < left.size()) min = getMin(left.get(i), min, i);
            if (j < middle.size()) min = getMin(middle.get(j), min, j);
            if (k < right.size()) min = getMin(right.get(k), min, k);

            if (i < left.size() && min == left.get(i)) i++;
            else if (j < middle.size() && min == middle.get(j)) j++;
            else k++;

            result.add(min);
        }
        return result;
    }

    private T getMin(T current, T currentMin, int index) {
        if (currentMin == null) return current;
        return compare(current, currentMin) < 0 ? current : currentMin;
    }
}