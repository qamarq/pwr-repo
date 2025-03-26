package lab2;

import java.util.Objects;

class SimpleSinglyLinkedList<E> {

    private Node<E> head;
    private int size;

    public SimpleSinglyLinkedList() {
        head = null;
        size = 0;
    }

    public int size() {
        return size;
    }

    public void add(E element) {
        if (head == null) {
            head = new Node<>(element);
        } else {
            Node<E> current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = new Node<>(element);
        }
        size++;
    }

    public void add(int index, E element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index " + index + " is out of range. Size: " + size);
        }
        if (index == 0) {
            head = new Node<>(element, head);
        } else {
            Node<E> current = head;
            for (int i = 0; i < index - 1; i++) {
                current = current.next;
            }
            current.next = new Node<>(element, current.next);
        }
        size++;
    }

    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index " + index + " is out of range. Size: " + size);
        }
        Node<E> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }

    public E set(int index, E element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index " + index + " is out of range. Size: " + size);
        }
        Node<E> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        E old = current.data;
        current.data = element;
        return old;
    }

    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index " + index + " is out of range. Size: " + size);
        }
        E removed;
        if (index == 0) {
            removed = head.data;
            head = head.next;
        } else {
            Node<E> current = head;
            for (int i = 0; i < index - 1; i++) {
                current = current.next;
            }
            removed = current.next.data;
            current.next = current.next.next;
        }
        size--;
        return removed;
    }

    public boolean contains(E element) {
        Node<E> current = head;
        while (current != null) {
            if (Objects.equals(element, current.data)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public int indexOf(E element) {
        Node<E> current = head;
        int index = 0;
        while (current != null) {
            if (Objects.equals(element, current.data)) {
                return index;
            }
            current = current.next;
            index++;
        }
        return -1;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Node<E> current = head;
        while (current != null) {
            sb.append(current.data);
            if (current.next != null) {
                sb.append(", ");
            }
            current = current.next;
        }
        sb.append("]");
        return sb.toString();
    }

    private static class Node<E> {
        E data;
        Node<E> next;
        Node(E data) {
            this.data = data;
            this.next = null;
        }
        Node(E data, Node<E> next) {
            this.data = data;
            this.next = next;
        }
    }
}