package lab10;

// MaxBinomialHeap.java
public class MaxBinomialHeap<T extends Comparable<T>> {
    private Node<T> head;

    private static class Node<T> {
        T key;
        int degree;
        Node<T> parent;
        Node<T> child;
        Node<T> sibling;

        Node(T key) {
            this.key = key;
            this.degree = 0;
        }
    }

    /** Tworzy pusty kopiec. */
    public MaxBinomialHeap() {
        head = null;
    }

    /** Wstawia wartość do kopca. */
    public void insert(T value) {
        MaxBinomialHeap<T> tmp = new MaxBinomialHeap<>();
        tmp.head = new Node<>(value);
        merge(tmp);
    }

    /** Zwraca maksimum (null, jeśli pusty). */
    public T findMax() {
        if (head == null) return null;
        Node<T> curr = head, maxNode = head;
        while (curr != null) {
            if (curr.key.compareTo(maxNode.key) > 0) {
                maxNode = curr;
            }
            curr = curr.sibling;
        }
        return maxNode.key;
    }

    /**
     * Usuwa i zwraca maksimum (null, jeśli pusty).
     */
    public T extractMax() {
        if (head == null) return null;
        // 1) Znajdź korzeń z max
        Node<T> prev = null, curr = head;
        Node<T> maxNode = head, maxPrev = null;
        while (curr != null) {
            if (curr.key.compareTo(maxNode.key) > 0) {
                maxNode = curr;
                maxPrev = prev;
            }
            prev = curr;
            curr = curr.sibling;
        }
        // 2) Usuń maxNode z listy korzeni
        if (maxPrev == null) {
            head = maxNode.sibling;
        } else {
            maxPrev.sibling = maxNode.sibling;
        }
        // 3) Weź dzieci maxNode, odwróć ich listę i utwórz nowy kopiec
        Node<T> child = maxNode.child, rev = null;
        while (child != null) {
            Node<T> next = child.sibling;
            child.sibling = rev;
            child.parent = null;
            rev = child;
            child = next;
        }
        MaxBinomialHeap<T> tmp = new MaxBinomialHeap<>();
        tmp.head = rev;
        // 4) Scal
        merge(tmp);
        return maxNode.key;
    }

    /**
     * Łączy ten kopiec z innym; po scaleniu drugi kopiec
     * zostaje „wyczyszczony” (head = null).
     */
    public void merge(MaxBinomialHeap<T> other) {
        head = mergeRootLists(head, other.head);
        other.head = null;
        if (head == null) return;
        // Teraz łącz drzewa o tej samej stopniu
        Node<T> prev = null;
        Node<T> curr = head;
        Node<T> next = curr.sibling;
        while (next != null) {
            if (curr.degree != next.degree ||
                    (next.sibling != null && next.sibling.degree == curr.degree)) {
                prev = curr;
                curr = next;
            } else {
                // curr.degree == next.degree i nie ma trzeciego drzewa
                if (curr.key.compareTo(next.key) >= 0) {
                    // next dołączamy pod curr
                    curr.sibling = next.sibling;
                    linkTrees(curr, next);
                } else {
                    // curr dołączamy pod next
                    if (prev == null) {
                        head = next;
                    } else {
                        prev.sibling = next;
                    }
                    linkTrees(next, curr);
                    curr = next;
                }
            }
            next = curr.sibling;
        }
    }

    // scala dwie posortowane wg stopnia listy korzeni
    private Node<T> mergeRootLists(Node<T> h1, Node<T> h2) {
        if (h1 == null) return h2;
        if (h2 == null) return h1;
        Node<T> head, tail;
        if (h1.degree <= h2.degree) {
            head = h1;
            h1 = h1.sibling;
        } else {
            head = h2;
            h2 = h2.sibling;
        }
        tail = head;
        while (h1 != null && h2 != null) {
            if (h1.degree <= h2.degree) {
                tail.sibling = h1;
                h1 = h1.sibling;
            } else {
                tail.sibling = h2;
                h2 = h2.sibling;
            }
            tail = tail.sibling;
        }
        tail.sibling = (h1 != null) ? h1 : h2;
        return head;
    }

    // linkuje drzewo child pod parent (max-heap)
    private void linkTrees(Node<T> parent, Node<T> child) {
        child.parent = parent;
        child.sibling = parent.child;
        parent.child = child;
        parent.degree++;
    }
}