package utils;

import utils.IList;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

@SuppressWarnings("DuplicatedCode")
public class OneWayLinkedList<E> implements IList<E> {

    private class Element {
        public Element(E e) {
            this.object = e;
        }

        E object;
        Element next = null;
    }

    private final Element sentinel;

    private class InnerIterator implements Iterator<E> {
        private Element actElem;

        public InnerIterator() {
            actElem = sentinel;
        }

        @Override
        public boolean hasNext() {
            return actElem.next != null;
        }

        @Override
        public E next() {
            actElem = actElem.next;
            return actElem.object;
        }
    }

    public OneWayLinkedList() {
        // make a sentinel
        this.sentinel = new Element(null);
    }

    public OneWayLinkedList(E[] elements) {
        // make a sentinel
        this.sentinel = new Element(null);
        for (E element : elements) {
            add(element);
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new InnerIterator();
    }

    @Override
    public ListIterator<E> listIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(E e) {
        Element newElement = new Element(e);
        Element last = sentinel;
        while (last.next != null) {
            last = last.next;
        }
        last.next = newElement;
        return true;
    }

    @Override
    public void add(int index, E element) throws NoSuchElementException {
        if (index < 0 || index > size()) {
            throw new NoSuchElementException();
        }
        int pos = 0;
        Element newElement = new Element(element);
        Element last = sentinel;
        while (last.next != null) {
            if (pos == index) {
                newElement.next = last.next;
                last.next = newElement;
                return;
            }
            last = last.next;
            pos++;
        }
        last.next = newElement;
    }

    @Override
    public void clear() {
        sentinel.next = null;
    }

    @Override
    public boolean contains(E element) {
        Element last = sentinel;
        while (last.next != null) {
            last = last.next;
            if (last.object.equals(element)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public E get(int index) throws NoSuchElementException {
        if (index < 0 || index > size() - 1) {
            throw new NoSuchElementException();
        }
        int pos = 0;
        Element last = sentinel.next;
        while (last != null) {
            if (pos == index) {
                return last.object;
            }
            last = last.next;
            pos++;
        }
        return null;
    }

    @Override
    public E set(int index, E element) throws NoSuchElementException {
        if (index < 0 || index > size() - 1) {
            throw new NoSuchElementException();
        }
        int pos = 0;
        Element last = sentinel.next;
        while (last != null) {
            if (pos == index) {
                E prevElement = last.object;
                last.object = element;
                return prevElement;
            }
            last = last.next;
            pos++;
        }
        return null;
    }

    @Override
    public int indexOf(E element) {
        int pos = 0;
        Element last = sentinel.next;
        while (last != null) {
            if (last.object.equals(element)) {
                return pos;
            }
            last = last.next;
            pos++;
        }
        return -1;
    }

    @Override
    public boolean isEmpty() {
        return sentinel.next == null;
    }

    @Override
    public E remove(int index) throws NoSuchElementException {
        if (index < 0 || index > size() - 1) {
            throw new NoSuchElementException();
        }
        int pos = 0;
        Element last = sentinel;
        while (last != null) {
            if (pos == index) {
                Element removedElement = last.next;
                last.next = last.next.next;
                return removedElement.object;
            }
            last = last.next;
            pos++;
        }
        return null;
    }

    @Override
    public boolean remove(E e) {
        Element last = sentinel;
        while (last != null) {
            if (last.next == null) {
                return false;
            }
            if (last.next.object.equals(e)) {
                last.next = last.next.next;
                return true;
            }
            last = last.next;
        }
        return false;
    }

    @Override
    public int size() {
        int size = 0;
        Element last = sentinel;
        while (last.next != null) {
            size++;
            last = last.next;
        }
        return size;
    }

    public void removeOdd() {
        int skip = 1;
        Element last = sentinel;
        while (last != null && last.next != null) {
            if (skip == 1) {
                last.next = last.next.next;
                skip = 0;
            }
            last = last.next;
            skip++;
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        Element last = sentinel.next;
        while (last != null) {
            result.append(last.object);
            if (last.next != null) {
                result.append(", ");
            }
            last = last.next;
        }
        return result.toString();
    }
}


