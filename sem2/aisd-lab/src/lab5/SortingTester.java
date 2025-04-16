package lab5;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Random;
import core.AbstractSwappingSortingAlgorithm;
import core.AbstractSortingAlgorithm;
import testing.*;
import testing.comparators.*;
import testing.generation.*;
import testing.generation.conversion.*;

public class SortingTester {
    public static void main(String[] args) {
        int[] arraySizes = {0, 5, 10, 15, 20, 30, 40, 50, 60, 70, 80, 100, 150, 200, 250, 500, 1000, 2000, 5000, 10000};

        Comparator<MarkedValue<Integer>> markedComparator = new MarkedValueComparator<Integer>(new IntegerComparator());

        Generator<MarkedValue<Integer>> orderedGenerator = new MarkingGenerator<Integer>(new OrderedIntegerArrayGenerator());
        Generator<MarkedValue<Integer>> reversedGenerator = new MarkingGenerator<Integer>(new ReversedIntegerArrayGenerator());
        Generator<MarkedValue<Integer>> randomGenerator = new MarkingGenerator<Integer>(new RandomIntegerArrayGenerator(10));
        Generator<MarkedValue<Integer>> shuffledGenerator = new MarkingGenerator<Integer>(new ShuffledIntegerArrayGenerator(10));
        Generator<MarkedValue<Integer>> orderedLinkedListGenerator = new MarkingGenerator<Integer>(new LinkedListGenerator(orderedGenerator));
        Generator<MarkedValue<Integer>> reversedLinkedListGenerator = new MarkingGenerator<Integer>(new LinkedListGenerator(reversedGenerator));
        Generator<MarkedValue<Integer>> randomLinkedListGenerator = new MarkingGenerator<Integer>(new LinkedListGenerator(randomGenerator));
        Generator<MarkedValue<Integer>> shuffledLinkedListGenerator = new MarkingGenerator<Integer>(new LinkedListGenerator(shuffledGenerator));

        // ThreeWayMergeSort
        AbstractSortingAlgorithm<MarkedValue<Integer>> mergeSortAlgorithm = new ThreeWayMergeSort<>(markedComparator);
        testSort("ThreeWayMergeSort", mergeSortAlgorithm, markedComparator, orderedGenerator, arraySizes, "OrderedGenerator");
        testSort("ThreeWayMergeSort", mergeSortAlgorithm, markedComparator, reversedGenerator, arraySizes, "ReversedGenerator");
        testSort("ThreeWayMergeSort", mergeSortAlgorithm, markedComparator, randomGenerator, arraySizes, "RandomGenerator");
        testSort("ThreeWayMergeSort", mergeSortAlgorithm, markedComparator, shuffledGenerator, arraySizes, "ShuffledGenerator");
        
        testSort("ThreeWayMergeSort", mergeSortAlgorithm, markedComparator, orderedLinkedListGenerator, arraySizes, "OrderedLinkedListGenerator");
        testSort("ThreeWayMergeSort", mergeSortAlgorithm, markedComparator, reversedLinkedListGenerator, arraySizes, "ReversedLinkedListGenerator");
        testSort("ThreeWayMergeSort", mergeSortAlgorithm, markedComparator, randomLinkedListGenerator, arraySizes, "RandomLinkedListGenerator");
        testSort("ThreeWayMergeSort", mergeSortAlgorithm, markedComparator, shuffledLinkedListGenerator, arraySizes, "ShuffledLinkedListGenerator");

        // OptimizedQuickSort with FirstPivotSelector
        AbstractSwappingSortingAlgorithm<MarkedValue<Integer>> quickSortFirstPivotAlgorithm =
                new OptimizedQuickSort<>(markedComparator, new OptimizedQuickSort.FirstPivotSelector<>());
        testSortSwapping("OptimizedQuickSort (FirstPivotSelector)", quickSortFirstPivotAlgorithm, markedComparator, orderedGenerator, arraySizes, "OrderedGenerator");
        testSortSwapping("OptimizedQuickSort (FirstPivotSelector)", quickSortFirstPivotAlgorithm, markedComparator, reversedGenerator, arraySizes, "ReversedGenerator");
        testSortSwapping("OptimizedQuickSort (FirstPivotSelector)", quickSortFirstPivotAlgorithm, markedComparator, randomGenerator, arraySizes, "RandomGenerator");
        testSortSwapping("OptimizedQuickSort (FirstPivotSelector)", quickSortFirstPivotAlgorithm, markedComparator, shuffledGenerator, arraySizes, "ShuffledGenerator");

        testSortSwapping("OptimizedQuickSort (FirstPivotSelector)", quickSortFirstPivotAlgorithm, markedComparator, orderedLinkedListGenerator, arraySizes, "OrderedLinkedListGenerator");
        testSortSwapping("OptimizedQuickSort (FirstPivotSelector)", quickSortFirstPivotAlgorithm, markedComparator, reversedLinkedListGenerator, arraySizes, "ReversedLinkedListGenerator");
        testSortSwapping("OptimizedQuickSort (FirstPivotSelector)", quickSortFirstPivotAlgorithm, markedComparator, randomLinkedListGenerator, arraySizes, "RandomLinkedListGenerator");
        testSortSwapping("OptimizedQuickSort (FirstPivotSelector)", quickSortFirstPivotAlgorithm, markedComparator, shuffledLinkedListGenerator, arraySizes, "ShuffledLinkedListGenerator");

        // OptimizedQuickSort with RandomPivotSelector
        AbstractSwappingSortingAlgorithm<MarkedValue<Integer>> quickSortRandomPivotAlgorithm =
                new OptimizedQuickSort<>(markedComparator, new OptimizedQuickSort.RandomPivotSelector<>());
        testSortSwapping("OptimizedQuickSort (RandomPivotSelector)", quickSortRandomPivotAlgorithm, markedComparator, orderedGenerator, arraySizes, "OrderedGenerator");
        testSortSwapping("OptimizedQuickSort (RandomPivotSelector)", quickSortRandomPivotAlgorithm, markedComparator, reversedGenerator, arraySizes, "ReversedGenerator");
        testSortSwapping("OptimizedQuickSort (RandomPivotSelector)", quickSortRandomPivotAlgorithm, markedComparator, randomGenerator, arraySizes, "RandomGenerator");
        testSortSwapping("OptimizedQuickSort (RandomPivotSelector)", quickSortRandomPivotAlgorithm, markedComparator, shuffledGenerator, arraySizes, "ShuffledGenerator");

        testSortSwapping("OptimizedQuickSort (RandomPivotSelector)", quickSortRandomPivotAlgorithm, markedComparator, orderedLinkedListGenerator, arraySizes, "OrderedLinkedListGenerator");
        testSortSwapping("OptimizedQuickSort (RandomPivotSelector)", quickSortRandomPivotAlgorithm, markedComparator, reversedLinkedListGenerator, arraySizes, "ReversedLinkedListGenerator");
        testSortSwapping("OptimizedQuickSort (RandomPivotSelector)", quickSortRandomPivotAlgorithm, markedComparator, randomLinkedListGenerator, arraySizes, "RandomLinkedListGenerator");
        testSortSwapping("OptimizedQuickSort (RandomPivotSelector)", quickSortRandomPivotAlgorithm, markedComparator, shuffledLinkedListGenerator, arraySizes, "ShuffledLinkedListGenerator");
    }

    private static void testSortSwapping(String sortType, AbstractSwappingSortingAlgorithm<MarkedValue<Integer>> algorithm, Comparator<MarkedValue<Integer>> markedComparator, Generator<MarkedValue<Integer>> generator, int[] arraySizes, String generatorType) {
        String fileName = sortType + generatorType + ".txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("-- " + sortType + " test; numbers generated by: " + generatorType + " --");
            writer.println("Size\tTime\t\tStddev\t\tCompare\t\tStddev\t\tSwaps\t\tStddev");

            for (int size : arraySizes) {
                testing.results.swapping.Result result = Tester.runNTimes(algorithm, generator, size, 50);
                String formattedOutput = String.format("%5d\t%f\t%f\t%f\t%f\t%f\t%f", size,
                        result.averageTimeInMilliseconds(), result.timeStandardDeviation(),
                        result.averageComparisons(), result.comparisonsStandardDeviation(),
                        result.averageSwaps(), result.swapsStandardDeviation());

                writer.print(formattedOutput.replace('.', ',') + "\n");
            }

            System.out.println("Zapisano wyniki do pliku " + fileName);
        } catch (Exception e) {
            System.err.println("Wystąpił błąd podczas zapisywania wyników do pliku: " + e.getMessage());
        }
    }

    private static void testSort(String sortType, AbstractSortingAlgorithm<MarkedValue<Integer>> algorithm, Comparator<MarkedValue<Integer>> markedComparator, Generator<MarkedValue<Integer>> generator, int[] arraySizes, String generatorType) {
        String fileName = sortType + generatorType + ".txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("-- " + sortType + " test; numbers generated by: " + generatorType + " --");
            writer.println("Size\tTime\t\tStddev\t\tCompare\t\tStddev\t\tSwaps\t\tStddev");

            for (int size : arraySizes) {
                testing.results.Result result = Tester.runNTimes(algorithm, generator, size, 50);
                String formattedOutput = String.format("%5d\t%f\t%f\t%f\t%f", size,
                        result.averageTimeInMilliseconds(), result.timeStandardDeviation(),
                        result.averageComparisons(), result.comparisonsStandardDeviation());

                writer.print(formattedOutput.replace('.', ',') + "\n");
            }

            System.out.println("Zapisano wyniki do pliku " + fileName);
        } catch (Exception e) {
            System.err.println("Wystąpił błąd podczas zapisywania wyników do pliku: " + e.getMessage());
        }
    }
}
