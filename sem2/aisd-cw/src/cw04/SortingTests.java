package cw04;

import utils.OneWayLinkedList;
import utils.ListSorter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("DuplicatedCode")
public class SortingTests {

    @Test
    void ReversedInsertsort() {
        OneWayLinkedList<Integer> list = new OneWayLinkedList<>(new Integer[]{3, 76, 71, 5, 57, 12, 50, 20, 93, 20, 4, 62});
        ListSorter<Integer> sorter = new ReversedInsertsort<>(Integer::compareTo);
        sorter.sort(list);

        String expected = "93, 76, 71, 62, 57, 50, 20, 20, 12, 5, 4, 3";
        String actual = list.toString();

        assertEquals(expected, actual);
    }

    @Test
    void ReversedSelectsort() {
        OneWayLinkedList<Integer> list = new OneWayLinkedList<>(new Integer[]{76, 71, 5, 57, 12, 50, 20, 3, 20, 55, 62, 53});
        ListSorter<Integer> sorter = new ReversedSelectsort<>(Integer::compareTo);
        sorter.sort(list);

        String expected = "76, 71, 62, 57, 55, 53, 50, 20, 20, 12, 5, 3";
        String actual = list.toString();

        assertEquals(expected, actual);
    }

    @Test
    void ReversedBubblesort() {
        OneWayLinkedList<Integer> list = new OneWayLinkedList<>(new Integer[]{76, 20, 5, 57, 12, 50, 20, 93, 44, 55, 62, 3});
        ListSorter<Integer> sorter = new ReversedBubblesort<>(Integer::compareTo);
        sorter.sort(list);

        String expected = "93, 76, 62, 57, 55, 50, 44, 20, 20, 12, 5, 3";
        String actual = list.toString();

        assertEquals(expected, actual);
    }

    @Test
    void Shakersort() {
        OneWayLinkedList<Integer> list = new OneWayLinkedList<>(new Integer[]{10, 23, 22, 4});
        ListSorter<Integer> sorter = new Shakersort<>(Integer::compareTo);
        sorter.sort(list);

        String expected = "4, 10, 22, 23";
        String actual = list.toString();

        assertEquals(expected, actual);
    }

    @Test
    void ImprovedShakersort() {
        OneWayLinkedList<Integer> list = new OneWayLinkedList<>(new Integer[]{10, 23, 22, 4});
        ListSorter<Integer> sorter = new ImprovedShakersort<>(Integer::compareTo);
        sorter.sort(list);

        String expected = "4, 10, 22, 23";
        String actual = list.toString();

        assertEquals(expected, actual);
    }

    @Test
    void PermutationSort() {
        OneWayLinkedList<Integer> list = new OneWayLinkedList<>(new Integer[]{10, 23, 22, 4});
        ListSorter<Integer> sorter = new PermutationSort<>(Integer::compareTo);
        sorter.sort(list);

        String expected = "4, 10, 22, 23";
        String actual = list.toString();

        assertEquals(expected, actual);
    }

    @Test
    void Bogosort() {
        OneWayLinkedList<Integer> list = new OneWayLinkedList<>(new Integer[]{10, 23, 22, 4});
        ListSorter<Integer> sorter = new Bogosort<>(Integer::compareTo);
        sorter.sort(list);

        String expected = "4, 10, 22, 23";
        String actual = list.toString();

        assertEquals(expected, actual);
    }
}
