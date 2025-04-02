package lab3;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class TwoWayLinkedList<T> implements IList<T> {
    private Node<T> head;
    private Node<T> tail;
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
        Node<T> prevNode = (index == 0) ? head : getNode(index - 1);
        Node<T> nextNode = prevNode.next;
        Node<T> newNode = new Node<>(element, nextNode, prevNode);
        prevNode.next = newNode;
        nextNode.prev = newNode;
        if (index == size) tail.prev = newNode;
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
        Node<T> node = getNode(index);
        T oldValue = node.data;
        node.data = element;
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
        Node<T> node = getNode(index);
        node.prev.next = node.next;
        node.next.prev = node.prev;
        if (index == size - 1) tail.prev = node.prev;
        size--;
        return node.data;
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

    private void printList() {
        Node<T> current = head.next;
        while (current != tail) {
            System.out.print(current.data + " ");
            current = current.next;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        TwoWayLinkedList<Object> list = new TwoWayLinkedList<>();
        System.out.println("Dodawanie elementów: ");
        list.add(1);
        list.add("🚀");
        list.add(null);
        list.add(3.14);
        System.out.println("Rozmiar listy: " + list.size());

        System.out.println("Element na indeksie 1: " + list.get(1));

        System.out.println("Usuwanie elementu o wartości 1");
        list.remove(Integer.valueOf(1));
        System.out.println("Czy lista zawiera 1? " + list.contains(1));

        System.out.println("Aktualizacja elementu na indeksie 1 do wartości 5");
        list.set(1, 5);
        System.out.println("Element na indeksie 1: " + list.get(1));

        System.out.println("Czyszczenie listy");
        list.clear();
        System.out.println("Czy lista jest pusta? " + list.isEmpty());
    }
}