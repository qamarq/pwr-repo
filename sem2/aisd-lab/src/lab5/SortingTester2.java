package lab5;

import core.AbstractSortingAlgorithm;
import testing.generation.*;
import testing.generation.conversion.*;
import java.util.*;

public class SortingTester2 {
    public static void main(String[] args) {
        testAlgorithms(1000);
        testAlgorithms(10000);
    }

    private static void testAlgorithms(int size) {
        // Generator bazowy dla ArrayList
        Generator<Integer> arrayGenerator = new RandomIntegerArrayGenerator(1000);

        // Konwersja do generatora LinkedList
        Generator<Integer> linkedListGenerator = new LinkedListGenerator<>(arrayGenerator);

        // Generowanie danych
        List<Integer> arrayList = arrayGenerator.generate(size);
        List<Integer> linkedList = linkedListGenerator.generate(size);

        // Testowane algorytmy
        ThreeWayMergeSort<Integer> mergeSort = new ThreeWayMergeSort<>(Comparator.naturalOrder());
        OptimizedQuickSort<Integer> quickSortFirst = new OptimizedQuickSort<>(
                Comparator.naturalOrder(),
                new OptimizedQuickSort.FirstPivotSelector<>()
        );
        OptimizedQuickSort<Integer> quickSortRandom = new OptimizedQuickSort<>(
                Comparator.naturalOrder(),
                new OptimizedQuickSort.RandomPivotSelector<>()
        );

        System.out.println("\n=== Testing for size: " + size + " ===");

        // Testowanie na ArrayList
        testImplementation("MergeSort (ArrayList)", mergeSort, new ArrayList<>(arrayList));
        testImplementation("QuickSort-First (ArrayList)", quickSortFirst, new ArrayList<>(arrayList));
        testImplementation("QuickSort-Random (ArrayList)", quickSortRandom, new ArrayList<>(arrayList));

        // Testowanie na LinkedList
        testImplementation("MergeSort (LinkedList)", mergeSort, new LinkedList<>(linkedList));
        testImplementation("QuickSort-First (LinkedList)", quickSortFirst, new LinkedList<>(linkedList));
        testImplementation("QuickSort-Random (LinkedList)", quickSortRandom, new LinkedList<>(linkedList));
    }

    private static void testImplementation(String name, AbstractSortingAlgorithm<Integer> algorithm, List<Integer> list) {
        long start = System.nanoTime();
        algorithm.sort(list);
        long time = System.nanoTime() - start;

        System.out.printf("%-25s: %,12d ns | Valid: %b%n",
                name,
                time,
                isSorted(list));
    }

    private static boolean isSorted(List<Integer> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            if (list.get(i) > list.get(i + 1)) {
                return false;
            }
        }
        return true;
    }
}