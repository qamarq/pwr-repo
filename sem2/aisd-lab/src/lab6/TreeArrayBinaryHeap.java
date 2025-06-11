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

    /** Usuwa wszystkie elementy. */
    public void clear() {
        root = null;
        size = 0;
    }

    /** Dodaje nowy element do kopca. */
    public void add(T element) {
        if (element == null) throw new IllegalArgumentException("Null not allowed");
        size++;
        int idx = size;
        List<Boolean> bits = getPathBits(idx);
        // 1) Wstawiamy do części wskaźnikowej, o ile głębokość ≤ H
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
            // 2) Wstawiamy do jednej z tablic na poziomie H
            Node<T> parentH = findPointerNode(bits, H);
            boolean toRight = bits.get(H);
            ArrayHeap<T> arr = toRight ? parentH.rightHeap : parentH.leftHeap;
            arr.add(element);
            // ewentualna wymiana z węzłem wskaźnikowym
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

    public T maximum() {
        if (size == 0) throw new IllegalStateException("Heap is empty");
        T maxVal = root.value;
        if (size == 1) {
            clear();
            return maxVal;
        }
        int oldSize = size;
        size--;
        List<Boolean> bits = getPathBits(oldSize);
        T replacement;
        if (bits.size() <= H) {
            // usuwamy ostatni węzeł wskaźnikowy
            List<Boolean> parentBits = bits.subList(0, bits.size() - 1);
            Node<T> parent = findPointerNode(parentBits, parentBits.size());
            boolean isRight = bits.get(bits.size() - 1);
            Node<T> child = isRight ? parent.right : parent.left;
            replacement = child.value;
            if (isRight) parent.right = null;
            else parent.left = null;
        } else {
            // usuwamy ostatni element z odpowiedniej tablicy
            Node<T> parentH = findPointerNode(bits, H);
            boolean toRight = bits.get(H);
            ArrayHeap<T> arr = toRight ? parentH.rightHeap : parentH.leftHeap;
            replacement = arr.removeLast();
        }
        root.value = replacement;
        heapifyDownPointer(root, 0);
        return maxVal;
    }

    // ----- metody pomocnicze dla części wskaźnikowej -----

    /** Buduje ścieżkę (lista true=right, false=left) z indeksu w "heap-array". */
    private List<Boolean> getPathBits(int idx) {
        char[] c = Integer.toBinaryString(idx).toCharArray();
        List<Boolean> bits = new ArrayList<>(c.length - 1);
        for (int i = 1; i < c.length; i++) bits.add(c[i] == '1');
        return bits;
    }

    /** Wstawia nowy węzeł wskaźnikowy w określoną pozycję (głębokość ≤ H). */
    private Node<T> insertPointerNode(T element, List<Boolean> bits) {
        Node<T> cur = root;
        // tworzymy po drodze ewentualnie braku­jące węzły
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
        boolean toRight = bits.get(bits.size() - 1);
        Node<T> newNode = new Node<>(element);
        if (!toRight) cur.left = newNode;
        else cur.right = newNode;
        return newNode;
    }

    /** "Bąbelkuje" nowy węzeł w górę drzewa wskaźnikowego. */
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

    /**
     * Znajduje węzeł wskaźnikowy na głębokości 'length'
     * wg pierwszych 'length' kroków z listy bits.
     */
    private Node<T> findPointerNode(List<Boolean> bits, int length) {
        Node<T> cur = root;
        for (int i = 0; i < length; i++) {
            boolean toRight = bits.get(i);
            cur = toRight ? cur.right : cur.left;
        }
        return cur;
    }

    /** Odnajduje i naprawia własność kopca od danego węzła w dół. */
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
            // depth == H: dzieci są w tablicach
            ArrayHeap<T> La = node.leftHeap, Ra = node.rightHeap;
            T lv = (La.size() > 0 ? La.peek() : null);
            T rv = (Ra.size() > 0 ? Ra.peek() : null);
            // wybieramy większe z lv,rv
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

/** Prosty pomocnik: tablicowy kopiec maksymalny z operacjami add, peek,
 * removeLast, siftUp, siftDown. */
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

    /** Zastępuje wartość na pozycji i (1-based). */
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
}