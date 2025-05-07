package lab7;

public class BST<T extends Comparable<T>> {
    private static class Node<T> {
        T key;
        Node<T> left;
        Node<T> right;
        Node(T key) {
            this.key = key;
        }
    }
    private Node<T> root;

    public boolean search(T key) {
        return searchRec(root, key) != null;
    }
    private Node<T> searchRec(Node<T> node, T key) {
        if (node == null) return null;
        int cmp = key.compareTo(node.key);
        if (cmp == 0) return node;
        return cmp < 0
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
    private void preOrderRec(Node<T> node, Visitor<T> visitor) {
        if (node == null) return;
        visitor.visit(node.key);
        preOrderRec(node.left, visitor);
        preOrderRec(node.right, visitor);
    }

    public T findSuccessor(T key) {
        Node<T> current = root;
        Node<T> succ = null;
        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp < 0) {
                succ = current;
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                if (current.right != null) {
                    Node<T> temp = current.right;
                    while (temp.left != null) temp = temp.left;
                    return temp.key;
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
        Node<T> current = root;
        while (current != null) {
            parent = current;
            int cmp = key.compareTo(current.key);
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                return;
            }
        }
        if (key.compareTo(parent.key) < 0) {
            parent.left = node;
        } else {
            parent.right = node;
        }
    }

    public void delete(T key) {
        Node<T> parent = null;
        Node<T> current = root;
        while (current != null && key.compareTo(current.key) != 0) {
            parent = current;
            if (key.compareTo(current.key) < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        if (current == null) return;
        if (current.left != null && current.right != null) {
            Node<T> succParent = current;
            Node<T> succ = current.right;
            while (succ.left != null) {
                succParent = succ;
                succ = succ.left;
            }
            current.key = succ.key;
            parent = succParent;
            current = succ;
        }
        Node<T> child = current.left != null
                ? current.left
                : current.right;
        if (parent == null) {
            root = child;
        } else if (parent.left == current) {
            parent.left = child;
        } else {
            parent.right = child;
        }
    }
}

