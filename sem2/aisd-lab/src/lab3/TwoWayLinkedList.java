package lab3;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class TwoWayLinkedList<T> implements IList<T> {
    public Node<T> head;
    public Node<T> tail;
    private int size;

    public TwoWayLinkedList() {
        head = new Node<>(null, null, null);
        tail = new Node<>(null, null, head);
        head.next = tail;
        size = 0;
    }

    @Override
    public boolean add(T e) {
        add(size, e);
        return true;
    }

    @Override
    public void add(int index, T element) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        Node<T> newNode = new Node<>(element, current.next, current == head ? head : current.next.prev);
        if(current.next != tail)
            current.next.next.prev = newNode;
        current.next.prev = current;
        current.next = newNode;
        size++;
    }

    @Override
    public void clear() {
        head.next = tail;
        tail.prev = head;
        size = 0;
    }

    @Override
    public boolean contains(T element) {
        return indexOf(element) != -1;
    }

    @Override
    public T get(int index) {
        return getNode(index).data;
    }

    @Override
    public T set(int index, T element) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        Node<T> current = head.next;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        T oldValue = current.data;
        current.data = element;
        return oldValue;
    }

    @Override
    public int indexOf(T element) {
        Node<T> current = head.next;
        for (int i = 0; i < size; i++) {
            if ((current.data == null && element == null) ||
                    (current.data != null && current.data.equals(element))) {
                return i;
            }
            current = current.next;
        }
        return -1;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node<T> current = head.next;

            @Override
            public boolean hasNext() {
                return current != tail;
            }

            @Override
            public T next() {
                if (!hasNext()) throw new NoSuchElementException();
                T data = current.data;
                current = current.next;
                return data;
            }
        };
    }

    @Override
    public ListIterator<T> listIterator() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public T remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        Node<T> current = head.next;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        if(current.prev == head && current.next.prev == head){
            current.prev.next = current.next;
            if(current.next != tail)
                current.next.next.prev = current.prev;
        } else {
            current.prev.next.next = current.next;
            current.next.prev = current.prev;
            if(current.next != tail)
                current.next.next.prev = current.prev.next;
        }
        size--;
        return current.data;
    }

    @Override
    public boolean remove(T element) {
        int index = indexOf(element);
        if (index == -1) return false;
        remove(index);
        return true;
    }

    @Override
    public int size() {
        return size;
    }

    private Node<T> getNode(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        Node<T> current = head.next;
        for (int i = 0; i < index; i++) current = current.next;
        return current;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<T> current = head.next;
        while (current != tail) {
            sb.append(current.data);
            if (current.next != tail) sb.append(", ");
            current = current.next;
        }
        sb.append("]");
        return sb.toString();
    }
}