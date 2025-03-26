package lab2;

import java.util.ListIterator;

interface IList<E> extends Iterable<E> {
    boolean add(E e);
    void add(int index, E element);
    void clear();
    boolean contains(E element);
    E get(int index);
    E set(int index, E element);
    int indexOf(E element);
    boolean isEmpty();
    java.util.Iterator<E> iterator();
    ListIterator<E> listIterator();
    E remove(int index);
    boolean remove(E element);
    int size();
}