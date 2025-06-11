package lab10;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MaxBinomialHeapTest {

    @Test
    public void testInsertAndFindMax() {
        MaxBinomialHeap<Integer> h = new MaxBinomialHeap<>();
        assertNull(h.findMax());
        h.insert(5);
        assertEquals(5, h.findMax());
        h.insert(10);
        assertEquals(10, h.findMax());
        h.insert(7);
        assertEquals(10, h.findMax());
    }

    @Test
    public void testExtractMax() {
        MaxBinomialHeap<Integer> h = new MaxBinomialHeap<>();
        assertNull(h.extractMax());
        h.insert(4);
        h.insert(9);
        h.insert(2);
        assertEquals(9, h.extractMax());
        assertEquals(4, h.findMax());
        assertEquals(4, h.extractMax());
        assertEquals(2, h.extractMax());
        assertNull(h.findMax());
        assertNull(h.extractMax());
    }

    @Test
    public void testMergeHeaps() {
        MaxBinomialHeap<Integer> h1 = new MaxBinomialHeap<>();
        MaxBinomialHeap<Integer> h2 = new MaxBinomialHeap<>();
        for (int i = 0; i < 5; i++) h1.insert(i);
        for (int i = 5; i < 10; i++) h2.insert(i);
        h1.merge(h2);
        assertNull(h2.findMax());
        assertEquals(9, h1.findMax());
        for (int expected = 9; expected >= 0; expected--) {
            assertEquals(expected, h1.extractMax());
        }
        assertNull(h1.findMax());
    }

    @Test
    void testToListOnEmptyHeap() {
        MaxBinomialHeap<Integer> heap = new MaxBinomialHeap<>();
        List<Integer> list = heap.toList();
        assertTrue(list.isEmpty());
    }

    @Test
    void testToListSingleElement() {
        MaxBinomialHeap<String> heap = new MaxBinomialHeap<>();
        heap.insert("Ala");
        List<String> list = heap.toList();
        assertEquals(1, list.size());
        assertEquals("Ala", list.get(0));
    }

    @Test
    void testToListMultipleElements() {
        MaxBinomialHeap<Integer> heap = new MaxBinomialHeap<>();
        int[] vals = {5, 1, 3, 8, 7};
        for (int v : vals) {
            heap.insert(v);
        }
//        heap.printHeap();
        List<Integer> list = heap.toList();
        List<Integer> expected = Arrays.asList(8, 7, 5, 3, 1);
//        heap.printHeap();
        assertEquals(expected, list);
    }

    @Test
    void testToListMultipleElements2() {
        MaxBinomialHeap<Integer> heap = new MaxBinomialHeap<>();
        int[] vals = {3, 1, 2, 7, 5, 9};
        for (int v : vals) {
            heap.insert(v);
        }
        heap.printHeap();
        List<Integer> list = heap.toList();
        List<Integer> expected = Arrays.asList(9, 7, 5, 3, 2, 1);
        heap.printHeap();
        assertEquals(expected, list);
    }

    @Test
    void testOriginalHeapNotModified() {
        MaxBinomialHeap<Integer> heap = new MaxBinomialHeap<>();
        List<Integer> input = Arrays.asList(2, 9, 4);
        input.forEach(heap::insert);
        List<Integer> extracted = new ArrayList<>();
        Integer x;
        while ((x = heap.extractMax()) != null) {
            extracted.add(x);
        }
        List<Integer> expected = Arrays.asList(9, 4, 2);
        assertEquals(expected, extracted);
    }

    @Test
    void testWithDuplicates() {
        MaxBinomialHeap<Integer> heap = new MaxBinomialHeap<>();
        List<Integer> vals = Arrays.asList(3, 1, 3, 2, 3);
        vals.forEach(heap::insert);
        List<Integer> list = heap.toList();
        List<Integer> expected = Arrays.asList(3, 3, 3, 2, 1);
        assertEquals(expected, list);
    }
}