package lab6;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TreeArrayBinaryHeapTest {

    @Test
    void testAddAndMaximumSingleElement() {
        TreeArrayBinaryHeap<Integer> heap = new TreeArrayBinaryHeap<>(2);
        heap.add(42);
        assertEquals(42, heap.maximum());
    }

    @Test
    void testAddAndMaximumMultipleTreeElements() {
        TreeArrayBinaryHeap<Integer> heap = new TreeArrayBinaryHeap<>(2);
        heap.add(10);
        heap.add(20);
        heap.add(5);
        assertEquals(20, heap.maximum());
        assertEquals(10, heap.maximum());
        assertEquals(5, heap.maximum());
        assertNull(heap.maximum());
    }

    @Test
    void testAddBeyondTreeHeight() {
        TreeArrayBinaryHeap<Integer> heap = new TreeArrayBinaryHeap<>(1);
        heap.add(10); // root
        heap.add(20); // left
        heap.add(30); // right
        heap.add(40); // goes to array heap
        heap.add(50); // goes to array heap
        assertEquals(50, heap.maximum());
        assertEquals(40, heap.maximum());
        assertEquals(30, heap.maximum());
    }

    @Test
    void testClear() {
        TreeArrayBinaryHeap<Integer> heap = new TreeArrayBinaryHeap<>(1);
        heap.add(1);
        heap.add(2);
        heap.clear();
        assertNull(heap.maximum());
        heap.add(3);
        assertEquals(3, heap.maximum());
    }

    @Test
    void testNullInsertionThrowsException() {
        TreeArrayBinaryHeap<Integer> heap = new TreeArrayBinaryHeap<>(1);
        assertThrows(IllegalArgumentException.class, () -> heap.add(null));
    }

    @Test
    void testMaximumEmptyHeap() {
        TreeArrayBinaryHeap<Integer> heap = new TreeArrayBinaryHeap<>(2);
        assertNull(heap.maximum());
    }
}
