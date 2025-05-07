package lab7;

import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class BSTTest {
    @Test
    void testEmptyTree() {
        BST<Integer> tree = new BST<>(Integer::compareTo);
        assertFalse(tree.search(1));
        assertNull(tree.findMin());
        assertNull(tree.findMax());
        assertNull(tree.findSuccessor(1));
        List<Integer> visited = new ArrayList<>();
        tree.preOrderTraversal(visited::add);
        assertTrue(visited.isEmpty());
    }

    @Test
    void testInsertSearchMinMaxAndSuccessor() {
        BST<Integer> tree = new BST<>(Integer::compareTo);
        int[] vals = {5, 3, 7, 2, 4, 6, 8};
        for (int v : vals) tree.insert(v);
        for (int v : vals) assertTrue(tree.search(v));
        assertEquals(2, tree.findMin());
        assertEquals(8, tree.findMax());
        assertEquals(3, tree.findSuccessor(2));
        assertEquals(4, tree.findSuccessor(3));
        assertEquals(8, tree.findSuccessor(7));
        assertNull(tree.findSuccessor(8));
    }

    @Test
    void testDeleteLeaf() {
        BST<Integer> tree = new BST<>(Integer::compareTo);
        tree.insert(5);
        tree.insert(3);
        tree.insert(7);
        tree.delete(3);
        assertFalse(tree.search(3));
        List<Integer> pre = new ArrayList<>();
        tree.preOrderTraversal(pre::add);
        assertEquals(Arrays.asList(5, 7), pre);
    }

    @Test
    void testDeleteNodeWithOneChild() {
        BST<Integer> tree = new BST<>(Integer::compareTo);
        tree.insert(5);
        tree.insert(3);
        tree.insert(2);
        tree.insert(7);
        tree.delete(3);
        assertFalse(tree.search(3));
        List<Integer> pre = new ArrayList<>();
        tree.preOrderTraversal(pre::add);
        assertEquals(Arrays.asList(5, 2, 7), pre);
    }

    @Test
    void testDeleteNodeWithTwoChildren() {
        BST<Integer> tree = new BST<>(Integer::compareTo);
        int[] vals = {4, 2, 1, 3, 6, 5, 7};
        for (int v : vals) tree.insert(v);
        tree.delete(2);
        assertFalse(tree.search(2));
        List<Integer> pre = new ArrayList<>();
        tree.preOrderTraversal(pre::add);
        assertEquals(Arrays.asList(4, 3, 1, 6, 5, 7), pre);
    }

    @Test
    void testPreOrderTraversal() {
        BST<Integer> tree = new BST<>(Integer::compareTo);
        int[] vals = {4, 2, 1, 3, 6, 5, 7};
        for (int v : vals) tree.insert(v);
        List<Integer> visited = new ArrayList<>();
        tree.preOrderTraversal(visited::add);
        assertEquals(Arrays.asList(4, 2, 1, 3, 6, 5, 7), visited);
    }

    @Test
    void testKthGreatest() {
        BST<Integer> tree = new BST<>(Integer::compareTo);
//        int[] vals = {5, 3, 7, 2, 4, 6, 8};
        int [] vals = {2, 3, 4, 5, 6, 7, 8};
        for (int v : vals) tree.insert(v);
        Integer[] expected = {8, 7, 6, 5, 4, 3, 2};
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], tree.kthGreatest(i + 1));
        }
        assertNull(tree.kthGreatest(0));
        assertNull(tree.kthGreatest(expected.length + 1));
    }
}
