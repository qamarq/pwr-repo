package cw05;

import utils.OneWayLinkedList;
import utils.IList;
import utils.ListSorter;

import java.util.Comparator;

public class Shellsort<T> implements ListSorter<T> {
    private final Comparator<T> _comparator;

    public Shellsort(Comparator<T> comparator) {
        this._comparator = comparator;
    }

    @Override
    public IList<T> sort(IList<T> list) {
        int gap = list.size();
        while (gap > 1) {
            gap /= 2;

            System.out.println("\nGap: " + gap);
            System.out.println("Przed sortowaniem:");
            drawGroups(list, gap);

            for (int i = 0; i < gap; i++) {
                for (int j = i; j < list.size(); j += gap) {
                    T value = list.get(j);
                    int k = j;
                    while (k >= gap && _comparator.compare(value, list.get(k - gap)) < 0) {
                        list.set(k, list.get(k - gap));
                        k -= gap;
                    }
                    list.set(k, value);

                }
            }

            System.out.println("Po sortowaniu:");
            drawGroups(list, gap);
        }
        return list;
    }

    private void drawGroups(IList<T> list, int gap) {
        for (int i = 0; i < gap; i++) {
            for (int j = i; j < list.size(); j += gap) {
                drawValue(gap, i, j, list.get(j));
            }
            System.out.println();
        }
    }

    private void drawValue(int gap, int offset, int offsetControl, T value) {
        if (offset != offsetControl) {
            for (int i = gap * 3 - 1; i > 0; i--) {
                System.out.print(" ");
            }
        } else {
            for (int i = 0; i < offset; i++) {
                System.out.print("   ");
            }
        }
        System.out.print(value);
    }

    public static void main(String[] args) {
        OneWayLinkedList<Integer> list = new OneWayLinkedList<>(new Integer[]{5, 4, 2, 8, 7, 1, 3, 6});

        ListSorter<Integer> sorter = new Shellsort<>(Integer::compareTo);
        sorter.sort(list);

        System.out.println("Sorted list: " + list);
    }
}