package lab10;

// MaxBinomialHeapTest.java
import org.junit.jupiter.api.Test;
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
}