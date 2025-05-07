package lab7;

import java.util.Comparator;

public class BST<T> {
    private static class Node<T> {
        T key;
        Node<T> left;
        Node<T> right;
        Node(T key) {
            this.key = key;
        }
    }

    private final Comparator<? super T> cmp;
    private Node<T> root;

    public BST(Comparator<? super T> cmp) {
        this.cmp = cmp;
    }

    private int compare(T a, T b) {
        return cmp.compare(a, b);
    }

    public boolean search(T key) {
        return searchRec(root, key) != null;
    }

    private Node<T> searchRec(Node<T> node, T key) {
        if (node == null) return null;
        int c = compare(key, node.key);
        if (c == 0) return node;
        return c < 0
                ? searchRec(node.left, key)
                : searchRec(node.right, key);
    }

    public T findMin() {
        if (root == null) return null;
        return findMinRec(root).key;
    }

    private Node<T> findMinRec(Node<T> node) {
        return node.left == null
                ? node
                : findMinRec(node.left);
    }

    public T findMax() {
        if (root == null) return null;
        return findMaxRec(root).key;
    }

    private Node<T> findMaxRec(Node<T> node) {
        return node.right == null
                ? node
                : findMaxRec(node.right);
    }

    public void preOrderTraversal(Visitor<T> visitor) {
        preOrderRec(root, visitor);
    }

    private void preOrderRec(Node<T> node, Visitor<T> v) {
        if (node == null) return;
        v.visit(node.key);
        preOrderRec(node.left, v);
        preOrderRec(node.right, v);
    }

    public T findSuccessor(T key) {
        Node<T> current = root;
        Node<T> succ = null;
        while (current != null) {
            int c = compare(key, current.key);
            if (c < 0) {
                succ = current;
                current = current.left;
            } else if (c > 0) {
                current = current.right;
            } else {
                if (current.right != null) {
                    Node<T> tmp = current.right;
                    while (tmp.left != null) tmp = tmp.left;
                    return tmp.key;
                }
                break;
            }
        }
        return succ == null ? null : succ.key;
    }

    public void insert(T key) {
        Node<T> node = new Node<>(key);
        if (root == null) {
            root = node;
            return;
        }
        Node<T> parent = null;
        Node<T> curr = root;
        while (curr != null) {
            parent = curr;
            int c = compare(key, curr.key);
            if (c < 0) curr = curr.left;
            else if (c > 0) curr = curr.right;
            else return;
        }
        if (compare(key, parent.key) < 0) parent.left = node;
        else parent.right = node;
    }

    public void delete(T key) {
        Node<T> parent = null;
        Node<T> curr = root;
        while (curr != null && compare(key, curr.key) != 0) {
            parent = curr;
            if (compare(key, curr.key) < 0) curr = curr.left;
            else curr = curr.right;
        }
        if (curr == null) return;
        if (curr.left != null && curr.right != null) {
            Node<T> succParent = curr;
            Node<T> succ = curr.right;
            while (succ.left != null) {
                succParent = succ;
                succ = succ.left;
            }
            curr.key = succ.key;
            parent = succParent;
            curr = succ;
        }
        Node<T> child = (curr.left != null) ? curr.left : curr.right;
        if (parent == null) root = child;
        else if (parent.left == curr) parent.left = child;
        else parent.right = child;
    }

    public T kthGreatest(int k) {
        OurInt h = new OurInt(k);
        Node<T> res = kthNode(root, h);
        return res == null ? null : res.key;
    }

    private Node<T> kthNode(Node<T> node, OurInt h) {
        if (node == null) return null;
        Node<T> r = kthNode(node.right, h);
        if (r != null) return r;
        h.k--;
        if (h.k == 0) return node;
        return kthNode(node.left, h);
    }

    private static class OurInt {
        int k;
        OurInt(int k) { this.k = k; }
    }
}
