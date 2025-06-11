package lab6;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TreeArrayBinaryHeap<T extends Comparable<? super T>> {
    private final int H;
    private Node<T> root;
    private int size;

    public TreeArrayBinaryHeap(int H) {
        if (H < 0) throw new IllegalArgumentException("H must be >= 0");
        this.H = H;
        this.size = 0;
        this.root = null;
    }

    public void clear() {
        root = null;
        size = 0;
    }

    public void printEntireHeap() {
        if (root == null) {
            System.out.println("Heap is empty");
            return;
        }
        printNode(root, 0);
    }

    private void printNode(Node<T> node, int depth) {
        if (node == null) return;
        for (int i = 0; i < depth; i++) System.out.print("  ");
        System.out.println(node.value);
        if (depth < H) {
            printNode(node.left, depth + 1);
            printNode(node.right, depth + 1);
        } else {
            if (node.leftHeap != null) node.leftHeap.printEntireHeap();
            if (node.rightHeap != null) node.rightHeap.printEntireHeap();
        }
    }

    public void add(T element) {
        if (element == null) throw new IllegalArgumentException("Null not allowed");
        size++;
        int idx = size;
        List<Boolean> bits = getPathBits(idx);
        if (bits.size() <= H) {
            if (root == null) {
                root = new Node<>(element);
                if (bits.size() == H) {
                    root.leftHeap = new ArrayHeap<>();
                    root.rightHeap = new ArrayHeap<>();
                }
            } else {
                Node<T> newNode = insertPointerNode(element, bits);
                if (bits.size() == H) {
                    newNode.leftHeap = new ArrayHeap<>();
                    newNode.rightHeap = new ArrayHeap<>();
                }
            }
            bubbleUpPointer(bits);
        } else {
            Node<T> parentH = findPointerNode(bits, H);
            boolean toRight = bits.get(H);
            ArrayHeap<T> arr = toRight ? parentH.rightHeap : parentH.leftHeap;
            arr.add(element);
            T childRoot = arr.peek();
            if (childRoot.compareTo(parentH.value) > 0) {
                T tmp = parentH.value;
                parentH.value = childRoot;
                arr.set(1, tmp);
                arr.shiftDown(1);
                bubbleUpPointer(bits.subList(0, H));
            }
        }
    }

    private T removeLastElement() {
        int lastIdx = size;
        size--;
        List<Boolean> bits = getPathBits(lastIdx);
        T replacement;

        if (bits.size() <= H) {
            if (bits.isEmpty()) {
                replacement = root.value;
                root = null;
                return replacement;
            }
            List<Boolean> parentBits = bits.subList(0, bits.size() - 1);
            Node<T> parent = findPointerNode(parentBits, parentBits.size());
            boolean isRight = bits.getLast();
            Node<T> child;
            if (isRight) {
                child = parent.right;
                parent.right = null;
            } else {
                child = parent.left;
                parent.left = null;
            }
            replacement = child.value;
        } else {
            Node<T> parentH = findPointerNode(bits, H);
            boolean toRight = bits.get(H);
            ArrayHeap<T> arr = toRight ? parentH.rightHeap : parentH.leftHeap;
            replacement = arr.removeLast();
        }
        return replacement;
    }

    public T maximum() {
        if (size == 0) throw new IllegalStateException("Heap is empty");
        T maxVal = root.value;
        if (size == 1) {
            clear();
            return maxVal;
        }

        root.value = removeLastElement();
        heapifyDownPointer(root, 0);
        return maxVal;
    }

    // ----- metody pomocnicze dla części wskaźnikowej -----

    private List<Boolean> getPathBits(int idx) {
        char[] c = Integer.toBinaryString(idx).toCharArray();
        List<Boolean> bits = new ArrayList<>(c.length - 1);
        for (int i = 1; i < c.length; i++) bits.add(c[i] == '1');
        return bits;
    }

    private Node<T> insertPointerNode(T element, List<Boolean> bits) {
        Node<T> cur = root;
        for (int i = 0; i < bits.size() - 1; i++) {
            boolean toRight = bits.get(i);
            if (!toRight) {
                if (cur.left == null) cur.left = new Node<>(null);
                cur = cur.left;
            } else {
                if (cur.right == null) cur.right = new Node<>(null);
                cur = cur.right;
            }
        }
        boolean toRight = bits.getLast();
        Node<T> newNode = new Node<>(element);
        if (!toRight) cur.left = newNode;
        else cur.right = newNode;
        return newNode;
    }

    private void bubbleUpPointer(List<Boolean> bits) {
        if (root == null || bits.isEmpty()) return;
        List<Node<T>> path = new ArrayList<>(bits.size() + 1);
        Node<T> cur = root;
        path.add(cur);
        for (boolean toRight : bits) {
            cur = toRight ? cur.right : cur.left;
            path.add(cur);
        }
        for (int i = path.size() - 1; i > 0; i--) {
            Node<T> child = path.get(i);
            Node<T> parent = path.get(i - 1);
            if (child.value.compareTo(parent.value) > 0) {
                T tmp = child.value;
                child.value = parent.value;
                parent.value = tmp;
            } else {
                break;
            }
        }
    }

    private Node<T> findPointerNode(List<Boolean> bits, int length) {
        Node<T> cur = root;
        for (int i = 0; i < length; i++) {
            boolean toRight = bits.get(i);
            cur = toRight ? cur.right : cur.left;
        }
        return cur;
    }

    private void heapifyDownPointer(Node<T> node, int depth) {
        if (node == null) return;
        if (depth < H) {
            Node<T> L = node.left, R = node.right, largest = node;
            if (L != null && L.value.compareTo(largest.value) > 0) largest = L;
            if (R != null && R.value.compareTo(largest.value) > 0) largest = R;
            if (largest != node) {
                T tmp = node.value;
                node.value = largest.value;
                largest.value = tmp;
                heapifyDownPointer(largest, depth + 1);
            }
        } else {
            ArrayHeap<T> La = node.leftHeap, Ra = node.rightHeap;
            T lv = (La.size() > 0 ? La.peek() : null);
            T rv = (Ra.size() > 0 ? Ra.peek() : null);
            if (lv != null
                    && (rv == null || lv.compareTo(rv) >= 0)
                    && lv.compareTo(node.value) > 0) {
                T tmp = node.value;
                node.value = lv;
                La.set(1, tmp);
                La.shiftDown(1);
            } else if (rv != null && rv.compareTo(node.value) > 0) {
                T tmp = node.value;
                node.value = rv;
                Ra.set(1, tmp);
                Ra.shiftDown(1);
            }
        }
    }

    private int[] findElement(T element) {
        return findElementRecursive(root, 1, 0, element);
    }

    private int[] findElementRecursive(Node<T> node, int index, int depth, T element) {
        if (node == null) return null;

        if (node.value.equals(element)) {
            return new int[] { index, -1 };
        }

        if (element.compareTo(node.value) > 0) {
            return null;
        }

        if (depth < H) {
            int[] found = findElementRecursive(node.left, index * 2, depth + 1, element);
            if (found != null) return found;
            return findElementRecursive(node.right, index * 2 + 1, depth + 1, element);
        } else {
            if (node.leftHeap != null) {
                int arrayIndex = node.leftHeap.findIndex(element);
                if (arrayIndex != -1) {
                    List<Boolean> path = new ArrayList<>(getPathBits(index));
                    path.add(false);
                    path.addAll(getPathBits(arrayIndex));
                    int globalIndex = 1;
                    for (boolean bit : path) {
                        globalIndex = (globalIndex << 1) | (bit ? 1 : 0);
                    }
                    return new int[] { globalIndex, arrayIndex };
                }
            }
            if (node.rightHeap != null) {
                int arrayIndex = node.rightHeap.findIndex(element);
                if (arrayIndex != -1) {
                    List<Boolean> path = new ArrayList<>(getPathBits(index));
                    path.add(true);
                    path.addAll(getPathBits(arrayIndex));
                    int globalIndex = 1;
                    for (boolean bit : path) {
                        globalIndex = (globalIndex << 1) | (bit ? 1 : 0);
                    }
                    return new int[] { globalIndex, arrayIndex };
                }
            }
            return null;
        }
    }

    public void remove(T element) {
        if (element == null || root == null) return;

        int[] location = findElement(element);
        if (location == null) return;

        int index = location[0];
        int arrayIndex = location[1];

        if (index == size) {
            removeLastElement();
            return;
        }

        T replacement = removeLastElement();

        if (arrayIndex == -1) {
            List<Boolean> bits = getPathBits(index);
            Node<T> nodeToReplace = findPointerNode(bits, bits.size());
            T oldValue = nodeToReplace.value;
            nodeToReplace.value = replacement;

            if (replacement.compareTo(oldValue) > 0) {
                bubbleUpPointer(bits);
            } else {
                heapifyDownPointer(nodeToReplace, bits.size());
            }
        } else {
            List<Boolean> bitsToParent = getPathBits(index).subList(0, H);
            Node<T> parentH = findPointerNode(bitsToParent, H);
            boolean toRight = getPathBits(index).get(H);
            ArrayHeap<T> heap = toRight ? parentH.rightHeap : parentH.leftHeap;

            T oldValue = heap.setAndGet(arrayIndex, replacement);

            if (replacement.compareTo(oldValue) > 0) {
                heap.shiftUp(arrayIndex);
            } else {
                heap.shiftDown(arrayIndex);
            }

            T childRoot = heap.peek();
            if (childRoot != null && childRoot.compareTo(parentH.value) > 0) {
                T tmp = parentH.value;
                parentH.value = childRoot;
                heap.set(1, tmp);
                heap.shiftDown(1);
                bubbleUpPointer(bitsToParent);
            }
        }
    }

    // ----- definicja węzła wskaźnikowego -----

    private static class Node<E extends Comparable<? super E>> {
        E value;
        Node<E> left, right;
        ArrayHeap<E> leftHeap, rightHeap;

        Node(E v) {
            this.value = v;
            this.left = this.right = null;
            this.leftHeap = this.rightHeap = null;
        }
    }
}

class ArrayHeap<T extends Comparable<? super T>> {
    private static final int DEFAULT_CAP = 8;
    private Object[] data;
    private int size;

    public ArrayHeap() {
        data = new Object[DEFAULT_CAP];
        size = 0;
    }

    public void add(T elem) {
        if (elem == null) throw new IllegalArgumentException("Null not allowed");
        ensureCapacity(size + 2);
        size++;
        data[size] = elem;
        shiftUp(size);
    }

    @SuppressWarnings("unchecked")
    public T peek() {
        return size > 0 ? (T) data[1] : null;
    }

    @SuppressWarnings("unchecked")
    public T removeLast() {
        if (size == 0) throw new IllegalStateException("Heap empty");
        T v = (T) data[size];
        data[size] = null;
        size--;
        return v;
    }

    public void set(int i, T elem) {
        if (i < 1 || i > size) throw new IndexOutOfBoundsException();
        data[i] = elem;
    }

    public int size() {
        return size;
    }

    @SuppressWarnings("unchecked")
    public void shiftUp(int i) {
        while (i > 1) {
            int p = i >> 1;
            T cv = (T) data[i], pv = (T) data[p];
            if (cv.compareTo(pv) > 0) {
                data[i] = pv;
                data[p] = cv;
                i = p;
            } else {
                break;
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void shiftDown(int i) {
        while (true) {
            int l = i << 1, r = l + 1, mx = i;
            if (l <= size) {
                T lv = (T) data[l], mv = (T) data[mx];
                if (lv.compareTo(mv) > 0) mx = l;
            }
            if (r <= size) {
                T rv = (T) data[r], mv = (T) data[mx];
                if (rv.compareTo(mv) > 0) mx = r;
            }
            if (mx != i) {
                Object tmp = data[i];
                data[i] = data[mx];
                data[mx] = tmp;
                i = mx;
            } else {
                break;
            }
        }
    }

    private void ensureCapacity(int minCap) {
        if (minCap > data.length) {
            int newCap = Math.max(data.length << 1, minCap);
            data = Arrays.copyOf(data, newCap);
        }
    }

    public void printEntireHeap() {
        if (size == 0) {
            System.out.println("Heap is empty");
            return;
        }
        for (int i = 1; i <= size; i++) {
            System.out.print(data[i] + " ");
        }
        System.out.println();
    }

    public int findIndex(T elem) {
        for (int i = 1; i <= size; i++) {
            if (data[i].equals(elem)) {
                return i;
            }
        }
        return -1;
    }

    @SuppressWarnings("unchecked")
    public T setAndGet(int i, T elem) {
        if (i < 1 || i > size) throw new IndexOutOfBoundsException();
        T oldValue = (T) data[i];
        data[i] = elem;
        return oldValue;
    }
}