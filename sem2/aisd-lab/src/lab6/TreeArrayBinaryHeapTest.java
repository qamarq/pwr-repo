package lab6;

import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class TreeArrayBinaryHeapTest {
    @Test
    void testAddAndMaximumSimple() {
        TreeArrayBinaryHeap<Integer> heap = new TreeArrayBinaryHeap<>(2);
        // wstawiamy kilka elementów
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
        List<Integer> out = new ArrayList<>();
        while (true) {
            try {
                out.add(heap.maximum());
            } catch (IllegalStateException e) {
                break;
            }
        }
        List<Integer> exp = Arrays.asList(10, 8, 7, 6, 5, 4, 3, 2, 1, 1, 0);
        assertEquals(exp, out);
    }

    @Test
    void testClear() {
        TreeArrayBinaryHeap<String> h = new TreeArrayBinaryHeap<>(1);
        h.add("a"); h.add("b");
        h.clear();
        assertThrows(IllegalStateException.class, () -> h.maximum());
        h.add("c");
        assertEquals("c", h.maximum());
    }

    @Test
    void testBoundaryCrossing() {
        TreeArrayBinaryHeap<Integer> h = new TreeArrayBinaryHeap<>(1);
        h.add(1); h.add(2); h.add(3); h.add(4);
        // 4 zostaje wstawione do subkopca tablicowego i bąbelkuje do góry
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
        assertThrows(IllegalStateException.class, () -> h.maximum());
    }
}