package cw07;

import java.util.*;

class BST {
    // Wewnętrzna klasa węzła
    static class Node {
        int key;
        Node left, right;
        Node(int key) {
            this.key = key;
            left = right = null;
        }
    }

    Node root;

    // Konstruktor
    BST() {
        root = null;
    }

    // Wstawianie elementu
    void insert(int key) {
        root = insertRec(root, key);
    }
    private Node insertRec(Node root, int key) {
        if (root == null) return new Node(key);
        if (key < root.key) root.left = insertRec(root.left, key);
        else if (key > root.key) root.right = insertRec(root.right, key);
        return root;
    }

    // Usuwanie elementu
    void delete(int key) {
        root = deleteRec(root, key);
    }
    private Node deleteRec(Node root, int key) {
        if (root == null) return root;
        if (key < root.key) root.left = deleteRec(root.left, key);
        else if (key > root.key) root.right = deleteRec(root.right, key);
        else {
            if (root.left == null) return root.right;
            else if (root.right == null) return root.left;
            root.key = minValue(root.right);
            root.right = deleteRec(root.right, root.key);
        }
        return root;
    }
    private int minValue(Node root) {
        int minv = root.key;
        while (root.left != null) {
            minv = root.left.key;
            root = root.left;
        }
        return minv;
    }

    // Wyszukiwanie elementu
    boolean search(int key) {
        return searchRec(root, key) != null;
    }
    private Node searchRec(Node root, int key) {
        if (root == null || root.key == key) return root;
        if (key < root.key) return searchRec(root.left, key);
        return searchRec(root.right, key);
    }

    // Przejścia drzewa
    void inorder() { inorderRec(root); System.out.println(); }
    private void inorderRec(Node root) {
        if (root != null) {
            inorderRec(root.left);
            System.out.print(root.key + " ");
            inorderRec(root.right);
        }
    }
    void preorder() { preorderRec(root); System.out.println(); }
    private void preorderRec(Node root) {
        if (root != null) {
            System.out.print(root.key + " ");
            preorderRec(root.left);
            preorderRec(root.right);
        }
    }
    void postorder() { postorderRec(root); System.out.println(); }
    private void postorderRec(Node root) {
        if (root != null) {
            postorderRec(root.left);
            postorderRec(root.right);
            System.out.print(root.key + " ");
        }
    }

    // Liczenie liczby węzłów
    int countNodes() { return countNodesRec(root); }
    private int countNodesRec(Node root) {
        if (root == null) return 0;
        return 1 + countNodesRec(root.left) + countNodesRec(root.right);
    }

    // Wysokość drzewa
    int height() { return heightRec(root); }
    private int heightRec(Node root) {
        if (root == null) return 0;
        return 1 + Math.max(heightRec(root.left), heightRec(root.right));
    }

    // Liczba węzłów z parzystym kluczem
    int countEvenNodes() { return countEvenNodesRec(root); }
    private int countEvenNodesRec(Node root) {
        if (root == null) return 0;
        int count = (root.key % 2 == 0) ? 1 : 0;
        return count + countEvenNodesRec(root.left) + countEvenNodesRec(root.right);
    }

    // Liczba węzłów z jednym dzieckiem
    int countOneChildNodes() { return countOneChildNodesRec(root); }
    private int countOneChildNodesRec(Node root) {
        if (root == null) return 0;
        int count = ((root.left == null) != (root.right == null)) ? 1 : 0;
        return count + countOneChildNodesRec(root.left) + countOneChildNodesRec(root.right);
    }

    // Liczba węzłów z jednym bratem (sibling)
    int countOneSibling() { return countOneSiblingRec(root, null); }
    private int countOneSiblingRec(Node node, Node parent) {
        if (node == null || parent == null) return 0;
        int count = ((parent.left == null) != (parent.right == null)) ? 1 : 0;
        return count + countOneSiblingRec(node.left, node) + countOneSiblingRec(node.right, node);
    }

    // Najdłuższa sekwencja węzłów z jednym dzieckiem
    int longestOneChildSeq() {
        return longestOneChildSeqRec(root, new int[1]);
    }
    private int longestOneChildSeqRec(Node node, int[] max) {
        if (node == null) return 0;
        int left = longestOneChildSeqRec(node.left, max);
        int right = longestOneChildSeqRec(node.right, max);
        int current = ((node.left == null) != (node.right == null)) ? 1 + Math.max(left, right) : 0;
        if (current > max[0]) max[0] = current;
        return current;
    }

    // Wypisywanie poziomami (BFS)
    void printLevelOrder() {
        if (root == null) return;
        Queue<Node> q = new LinkedList<>();
        q.add(root);
        while (!q.isEmpty()) {
            Node n = q.poll();
            System.out.print(n.key + " ");
            if (n.left != null) q.add(n.left);
            if (n.right != null) q.add(n.right);
        }
        System.out.println();
    }

    // Drukowanie drzewa (struktura)
    void printTree() { printTreeRec(root, 0, ""); }
    private void printTreeRec(Node node, int level, String prefix) {
        if (node == null) return;
        System.out.println(" ".repeat(level * 4) + prefix + node.key);
        printTreeRec(node.left, level + 1, "L--- ");
        printTreeRec(node.right, level + 1, "R--- ");
    }
}

public class BSTExample {
    public static void main(String[] args) {
        BST bst = new BST();
        // Przykładowa sekwencja z zadania
        bst.insert(20);
        bst.insert(7);
        bst.insert(10);
        bst.insert(25);
        bst.insert(4);
        bst.insert(1);
        bst.insert(2);
        bst.insert(12);
        bst.insert(30);
        bst.delete(12);
        bst.delete(1);
        bst.delete(20);

        System.out.println("Struktura drzewa:");
        bst.printTree();

        System.out.println("In-order:");
        bst.inorder();
        System.out.println("Pre-order:");
        bst.preorder();
        System.out.println("Post-order:");
        bst.postorder();

        System.out.println("Liczba węzłów: " + bst.countNodes());
        System.out.println("Wysokość drzewa: " + bst.height());
        System.out.println("Liczba węzłów parzystych: " + bst.countEvenNodes());
        System.out.println("Węzły z jednym dzieckiem: " + bst.countOneChildNodes());
        System.out.println("Węzły z jednym bratem: " + bst.countOneSibling());
        System.out.println("Najdłuższa sekwencja z jednym dzieckiem: " + bst.longestOneChildSeq());

        System.out.println("Wypisywanie poziomami:");
        bst.printLevelOrder();
    }
}
