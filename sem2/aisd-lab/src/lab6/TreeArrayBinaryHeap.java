package lab6;

import java.util.*;

public class TreeArrayBinaryHeap<T extends Comparable<T>> {
    private final int maxTreeHeight;
    private Node root;
    private final List<ArrayHeap> arrayHeaps = new ArrayList<>();
    private int size = 0;
    private boolean removedFlag = false;

    public TreeArrayBinaryHeap(int maxTreeHeight) {
        this.maxTreeHeight = maxTreeHeight;
    }

    public void clear() {
        root = null;
        arrayHeaps.clear();
        size = 0;
    }

    public void add(T element) {
        if (element == null) throw new IllegalArgumentException("Null values are not allowed");
        size++;
        String path = Integer.toBinaryString(size);
        if (path.length() - 1 <= maxTreeHeight) {
            root = insertTree(root, element, path.substring(1), 0);
        } else {
            int heapIndex = getHeapIndex(path);
            ensureArrayHeapExists(heapIndex);
            arrayHeaps.get(heapIndex).add(element);
        }
    }

    public T maximum() {
        if (size == 0) return null;
        T max = root != null ? root.value : null;
        for (ArrayHeap heap : arrayHeaps) {
            T candidate = heap.peek();
            if (candidate != null && (max == null || candidate.compareTo(max) > 0)) {
                max = candidate;
            }
        }

        if (max != null && root != null && max.equals(root.value)) {
            root = removeRoot(root);
        } else {
            for (ArrayHeap heap : arrayHeaps) {
                if (heap.remove(max)) break;
            }
        }

        size--;
        return max;
    }

    public void remove(T element) {
        if (element == null || size == 0) return;
        removedFlag = false;
        root = removeFromTree(root, element);
        if (removedFlag) {
            size--;
            return;
        }
        for (ArrayHeap heap : arrayHeaps) {
            if (heap.remove(element)) {
                size--;
                return;
            }
        }
    }

    private Node removeFromTree(Node node, T value) {
        if (node == null || removedFlag) return node;
        if (node.value.equals(value) && !removedFlag) {
            removedFlag = true;
            return removeRoot(node);
        }
        node.left = removeFromTree(node.left, value);
        node.right = removeFromTree(node.right, value);
        return node;
    }

    private Node insertTree(Node node, T value, String path, int depth) {
        if (node == null) return new Node(value);
        if (value.compareTo(node.value) > 0) {
            T tmp = node.value;
            node.value = value;
            value = tmp;
        }

        if (path.charAt(0) == '0') {
            node.left = insertTree(node.left, value, path.substring(1), depth + 1);
        } else {
            node.right = insertTree(node.right, value, path.substring(1), depth + 1);
        }
        return node;
    }

    private Node removeRoot(Node node) {
        if (node.left == null && node.right == null) return null;
        if (node.left != null && (node.right == null || node.left.value.compareTo(node.right.value) >= 0)) {
            node.value = node.left.value;
            node.left = removeRoot(node.left);
        } else {
            node.value = node.right.value;
            node.right = removeRoot(node.right);
        }
        return node;
    }

    private int getHeapIndex(String path) {
        return (Integer.parseInt(path, 2) - (1 << maxTreeHeight)) / (1 << maxTreeHeight);
    }

    private void ensureArrayHeapExists(int index) {
        while (arrayHeaps.size() <= index) {
            arrayHeaps.add(new ArrayHeap());
        }
    }

    private class Node {
        T value;
        Node left, right;
        Node(T value) {
            this.value = value;
        }
    }

    private class ArrayHeap {
        private final List<T> data = new ArrayList<>();

        void add(T value) {
            data.add(value);
            heapifyUp(data.size() - 1);
        }

        T peek() {
            return data.isEmpty() ? null : data.get(0);
        }

        boolean remove(T value) {
            int idx = data.indexOf(value);
            if (idx == -1) return false;
            Collections.swap(data, idx, data.size() - 1);
            data.remove(data.size() - 1);
            heapifyDown(idx);
            return true;
        }

        private void heapifyUp(int i) {
            while (i > 0) {
                int parent = (i - 1) / 2;
                if (data.get(i).compareTo(data.get(parent)) <= 0) break;
                Collections.swap(data, i, parent);
                i = parent;
            }
        }

        private void heapifyDown(int i) {
            int left, right, largest;
            while (true) {
                left = 2 * i + 1;
                right = 2 * i + 2;
                largest = i;
                if (left < data.size() && data.get(left).compareTo(data.get(largest)) > 0) largest = left;
                if (right < data.size() && data.get(right).compareTo(data.get(largest)) > 0) largest = right;
                if (largest == i) break;
                Collections.swap(data, i, largest);
                i = largest;
            }
        }
    }
}
