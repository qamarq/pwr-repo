package lab3;

public class Node<T> {
    T data;
    Node<T> next;
    Node<T> prev;

    Node(T data, Node<T> next, Node<T> prev) {
        this.data = data;
        this.next = next;
        this.prev = prev;
    }
}