package lab6;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import org.junit.jupiter.api.Test;

public class TreeArrayBinaryHeapTest {

    private <T extends Comparable<? super T>> List<T> drainHeap(
            TreeArrayBinaryHeap<T> heap
    ) {
        List<T> out = new ArrayList<>();
        while (true) {
            try {
                out.add(heap.maximum());
            } catch (IllegalStateException e) {
                break;
            }
        }
        return out;
    }

    @Test
    void testAddAndMaximumSimple() {
        TreeArrayBinaryHeap<Integer> heap = new TreeArrayBinaryHeap<>(2);
        heap.add(10);
        heap.add(8);
        heap.add(5);
        heap.add(6);
        heap.add(7);
        heap.add(1);
        heap.add(4);
        heap.add(3);
        heap.add(2);
        heap.add(0);
        heap.add(1);
        heap.printEntireHeap();
        List<Integer> out = drainHeap(heap);
        List<Integer> exp = Arrays.asList(10, 8, 7, 6, 5, 4, 3, 2, 1, 1, 0);
        assertEquals(exp, out);
    }

    @Test
    void testClear() {
        TreeArrayBinaryHeap<String> h = new TreeArrayBinaryHeap<>(1);
        h.add("a");
        h.add("b");
        h.clear();
        assertThrows(IllegalStateException.class, h::maximum);
        h.add("c");
        assertEquals("c", h.maximum());
    }

    @Test
    void testBoundaryCrossing() {
        TreeArrayBinaryHeap<Integer> h = new TreeArrayBinaryHeap<>(1);
        h.add(1);
        h.add(2);
        h.add(3);
        h.add(4);
        assertEquals(4, h.maximum());
        h.add(5);
        assertEquals(5, h.maximum());
    }

    @Test
    void testRandomized() {
        Random rnd = new Random(123);
        int N = 1000;
        TreeArrayBinaryHeap<Integer> h = new TreeArrayBinaryHeap<>(3);
        List<Integer> base = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            int v = rnd.nextInt(1_000_000);
            h.add(v);
            base.add(v);
        }
        base.sort(Collections.reverseOrder());
        for (int x : base) {
            assertEquals(x, h.maximum());
        }
        assertThrows(IllegalStateException.class, h::maximum);
    }

    @Test
    void testRemoveNonExistentElement() {
        TreeArrayBinaryHeap<Integer> heap = new TreeArrayBinaryHeap<>(2);
        heap.add(10);
        heap.add(20);
        heap.add(5);
        heap.remove(100);
        List<Integer> expected = Arrays.asList(20, 10, 5);
        assertEquals(expected, drainHeap(heap));
    }

    @Test
    void testRemoveFromPointerPart() {
        TreeArrayBinaryHeap<Integer> heap = new TreeArrayBinaryHeap<>(2);
        heap.add(15);
        heap.add(10);
        heap.add(12);
        heap.add(5);
        heap.add(8);
        heap.add(11);

        heap.remove(10);

        List<Integer> expected = Arrays.asList(15, 12, 11, 8, 5);
        assertEquals(expected, drainHeap(heap));
    }

    @Test
    void testRemoveFromArrayPart() {
        TreeArrayBinaryHeap<Integer> heap = new TreeArrayBinaryHeap<>(1);
        heap.add(20);
        heap.add(18);
        heap.add(15);
        heap.add(5);
        heap.add(8);
        heap.add(12);
        heap.add(16);

        heap.remove(5);

        List<Integer> expected = Arrays.asList(20, 18, 16, 15, 12, 8);
        assertEquals(expected, drainHeap(heap));
    }

    @Test
    void testRemoveRoot() {
        TreeArrayBinaryHeap<Integer> heap = new TreeArrayBinaryHeap<>(2);
        heap.add(100);
        heap.add(50);
        heap.add(75);
        heap.add(25);

        heap.remove(100);

        List<Integer> expected = Arrays.asList(75, 50, 25);
        assertEquals(expected, drainHeap(heap));
    }

    @Test
    void testRemoveLastElement() {
        TreeArrayBinaryHeap<Integer> heap = new TreeArrayBinaryHeap<>(1);
        heap.add(10);
        heap.add(20);
        heap.add(5);

        heap.remove(5);

        List<Integer> expected = Arrays.asList(20, 10);
        assertEquals(expected, drainHeap(heap));
    }

    @Test
    void testRemoveTriggersHeapifyDownAcrossBoundary() {
        TreeArrayBinaryHeap<Integer> heap = new TreeArrayBinaryHeap<>(1);
        heap.add(20);
        heap.add(18);
        heap.add(15);
        heap.add(5);
        heap.add(8);
        heap.add(12);
        heap.add(16);

        heap.remove(20);

        List<Integer> expected = Arrays.asList(18, 16, 15, 12, 8, 5);
        assertEquals(expected, drainHeap(heap));
    }

    @Test
    void testRemoveWithDuplicates() {
        TreeArrayBinaryHeap<Integer> heap = new TreeArrayBinaryHeap<>(2);
        heap.add(10);
        heap.add(5);
        heap.add(10);
        heap.add(8);

        heap.remove(10);

        List<Integer> expected = Arrays.asList(10, 8, 5);
        assertEquals(expected, drainHeap(heap));
    }

    @Test
    void testEdgeCaseH0() {
        TreeArrayBinaryHeap<Integer> heap = new TreeArrayBinaryHeap<>(0);
        heap.add(10);
        heap.add(20);
        heap.add(5);
        heap.add(15);
        heap.add(25);

        assertEquals(25, heap.maximum());

        heap.remove(10);

        List<Integer> expected = Arrays.asList(20, 15, 5);
        assertEquals(expected, drainHeap(heap));
    }
}