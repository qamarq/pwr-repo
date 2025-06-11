package lab10;

import java.util.ArrayList;
import java.util.List;

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

    public MaxBinomialHeap() {
        head = null;
    }

    public void insert(T value) {
        MaxBinomialHeap<T> tmp = new MaxBinomialHeap<>();
        tmp.head = new Node<>(value);
        merge(tmp);
    }

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

    public T extractMax() {
        if (head == null) return null;
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
        if (maxPrev == null) {
            head = maxNode.sibling;
        } else {
            maxPrev.sibling = maxNode.sibling;
        }
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
        merge(tmp);
        return maxNode.key;
    }

    public void merge(MaxBinomialHeap<T> other) {
        head = mergeRootLists(head, other.head);
        other.head = null;
        if (head == null) return;
        Node<T> prev = null;
        Node<T> curr = head;
        Node<T> next = curr.sibling;
        while (next != null) {
            if (curr.degree != next.degree ||
                    (next.sibling != null && next.sibling.degree == curr.degree)) {
                prev = curr;
                curr = next;
            } else {
                if (curr.key.compareTo(next.key) >= 0) {
                    curr.sibling = next.sibling;
                    linkTrees(curr, next);
                } else {
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

    private void linkTrees(Node<T> parent, Node<T> child) {
        child.parent = parent;
        child.sibling = parent.child;
        parent.child = child;
        parent.degree++;
    }

    public List<T> toList() {
        MaxBinomialHeap<T> copy = new MaxBinomialHeap<>();
        copy.head = cloneNode(head, null);
        List<T> result = new ArrayList<>();
        T key;
        while ((key = copy.extractMax()) != null) {
            result.add(key);
        }
        return result;
    }

    private Node<T> cloneNode(Node<T> node, Node<T> parent) {
        if (node == null) return null;
        Node<T> newNode = new Node<>(node.key);
        newNode.degree = node.degree;
        newNode.parent = parent;
        newNode.sibling = cloneNode(node.sibling, parent);
        newNode.child   = cloneNode(node.child, newNode);
        return newNode;
    }

    public void printHeap() {
        if (head == null) {
            System.out.println("Heap is empty.");
            return;
        }
        System.out.println("Binomial Heap:");
        printNode(head, 0);
    }

    private void printNode(Node<T> node, int level) {
        if (node == null) return;
        for (int i = 0; i < level; i++) {
            System.out.print("  ");
        }
        System.out.println(node.key + " (degree: " + node.degree + ")");
        printNode(node.child, level + 1);
        printNode(node.sibling, level);
    }
}