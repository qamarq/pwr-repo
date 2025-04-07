package cw05;

import utils.OneWayLinkedList;
import utils.IList;
import utils.ListSorter;

import java.util.*;

public class MergesortIterative<T> implements ListSorter<T> {
    private final Comparator<T> _comparator;

    public MergesortIterative(Comparator<T> comparator) {
        this._comparator = comparator;
    }

    @SuppressWarnings("DataFlowIssue")
    public IList<T> sort(IList<T> list) {
        if (list.size() <= 1) {
            return list;
        }

        Queue<IList<T>> queue = new LinkedList<>();

        for (int i = 0; i < list.size(); i++) {
            IList<T> singletonList = new OneWayLinkedList<>();
            singletonList.add(list.get(i));
            queue.offer(singletonList);
        }

        while (queue.size() > 1) {
            queue.offer(merge(queue.poll(), queue.poll()));
        }

        list.clear();
        for (T item : queue.poll()) {
            list.add(item);
        }
        return list;
    }

    @SuppressWarnings({"unchecked", "DuplicatedCode"})
    private IList<T> merge(IList<T> left, IList<T> right) {
        IList<T> result = (IList<T>) new ArrayList<Object>();
        Iterator<T> l = left.iterator();
        Iterator<T> r = right.iterator();
        T elemL = null, elemR = null;
        boolean contL, contR;
        if (contL = l.hasNext()) elemL = l.next();
        if (contR = r.hasNext()) elemR = r.next();
        while (contL && contR) {
            if (_comparator.compare(elemL, elemR) <= 0) {
                result.add(elemL);
                if (contL = l.hasNext()) elemL = l.next();
                else result.add(elemR);
            } else {
                result.add(elemR);
                if (contR = r.hasNext()) elemR = r.next();
                else result.add(elemL);
            }
        }
        while (l.hasNext()) result.add(l.next());
        while (r.hasNext()) result.add(r.next());
        return result;
    }

    public static void main(String[] args) {
        OneWayLinkedList<Integer> list = new OneWayLinkedList<>(new Integer[]{5, 4, 2, 8, 7, 1, 3, 6});

        ListSorter<Integer> sorter = new MergesortIterative<>(Integer::compareTo);
        sorter.sort(list);

        System.out.println("Sorted list: " + list);
    }
}
