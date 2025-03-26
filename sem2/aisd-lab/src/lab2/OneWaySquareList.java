package lab2;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class OneWaySquareList<E> implements IList<E> {

    private List<SimpleSinglyLinkedList<E>> rows;

    public OneWaySquareList() {
        rows = new ArrayList<>();
    }

    public boolean add(E e) {
        add(size(), e);
        return true;
    }

    public void add(int index, E element) {
        if (index < 0 || index > size()) {
            throw new IndexOutOfBoundsException("Index " + index + " is out of range. Size: " + size());
        }
        if (rows.isEmpty()) {
            SimpleSinglyLinkedList<E> newRow = new SimpleSinglyLinkedList<>();
            newRow.add(element);
            rows.add(newRow);
        } else if (index == size()) {
            SimpleSinglyLinkedList<E> lastRow = rows.get(rows.size() - 1);
            lastRow.add(element);
        } else {
            Position pos = findPosition(index);
            rows.get(pos.row).add(pos.col, element);
        }
        rebalance();
    }

    public void clear() {
        rows.clear();
    }

    public boolean contains(E element) {
        for (SimpleSinglyLinkedList<E> row : rows) {
            if (row.contains(element)) {
                return true;
            }
        }
        return false;
    }

    public E get(int index) {
        Position pos = findPosition(index);
        return rows.get(pos.row).get(pos.col);
    }

    public E set(int index, E element) {
        Position pos = findPosition(index);
        E old = rows.get(pos.row).get(pos.col);
        rows.get(pos.row).set(pos.col, element);
        return old;
    }

    public int indexOf(E element) {
        int idx = 0;
        for (SimpleSinglyLinkedList<E> row : rows) {
            int pos = row.indexOf(element);
            if (pos != -1) return idx + pos;
            idx += row.size();
        }
        return -1;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public java.util.Iterator<E> iterator() {
        throw new UnsupportedOperationException("Metoda iterator() nie jest zaimplementowana.");
    }

    public ListIterator<E> listIterator() {
        throw new UnsupportedOperationException("Metoda listIterator() nie jest zaimplementowana.");
    }

    public E remove(int index) {
        Position pos = findPosition(index);
        E removed = rows.get(pos.row).remove(pos.col);
        rebalance();
        return removed;
    }

    public boolean remove(E element) {
        int idx = indexOf(element);
        if (idx != -1) {
            remove(idx);
            return true;
        }
        return false;
    }

    public int size() {
        int total = 0;
        for (SimpleSinglyLinkedList<E> row : rows) {
            total += row.size();
        }
        return total;
    }

    private int calculateK() {
        int n = size();
        return (int) Math.ceil(Math.sqrt(n));
    }

    private void rebalance() {
        int total = size();
        if (total == 0) {
            rows.clear();
            return;
        }
        int k = calculateK();
        List<E> temp = new ArrayList<>(total);
        for (SimpleSinglyLinkedList<E> row : rows) {
            for (int i = 0; i < row.size(); i++) {
                temp.add(row.get(i));
            }
        }
        rows.clear();
        for (int i = 0; i < total; i += k) {
            SimpleSinglyLinkedList<E> newRow = new SimpleSinglyLinkedList<>();
            int end = Math.min(i + k, total);
            for (int j = i; j < end; j++) {
                newRow.add(temp.get(j));
            }
            rows.add(newRow);
        }
    }

    private Position findPosition(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException("Index " + index + " is out of range. Size: " + size());
        }
        int pos = index;
        for (int i = 0; i < rows.size(); i++) {
            SimpleSinglyLinkedList<E> row = rows.get(i);
            if (pos < row.size()) {
                return new Position(i, pos);
            } else {
                pos -= row.size();
            }
        }
        throw new NoSuchElementException("Nie znaleziono elementu przy indeksie " + index);
    }

    private static class Position {
        int row;
        int col;
        Position(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

    public void printStructure() {
        System.out.println("+------+-------------------------------+");
        System.out.println("| Row  | Elements                      |");
        System.out.println("+------+-------------------------------+");
        for (int i = 0; i < rows.size(); i++) {
            System.out.printf("| %-4d | %-29s |\n", i, rows.get(i));
        }
        System.out.println("+------+-------------------------------+");
        System.out.println("Total elements: " + size());
    }

    public static void main(String[] args) {
        OneWaySquareList<Object> list = new OneWaySquareList<>();
        System.out.println("Test pustej listy:");
        System.out.println("isEmpty: " + list.isEmpty());
        try {
            list.get(0);
        } catch (Exception e) {
            System.out.println("Błąd przy get(0) na pustej liście: " + e.getMessage());
        }
        System.out.println("\nTest listy jednoelementowej:");
        list.add("jedyny");
        list.printStructure();
        System.out.println("indexOf('jedyny'): " + list.indexOf("jedyny"));
        System.out.println("set(0, 'zmieniony'): " + list.set(0, "zmieniony"));
        list.printStructure();
        list.clear();
        System.out.println("\nTest dodawania wielu elementów (nieparzysta liczba elementów):");
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.printStructure();
        System.out.println("\nTest dodawania wielu elementów (parzysta liczba elementów):");
        list.clear();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        list.printStructure();
        System.out.println("\nTest dodawania na niepoprawnym indeksie:");
        try {
            list.add(100, "out");
        } catch (Exception e) {
            System.out.println("Błąd: " + e.getMessage());
        }
        System.out.println("\nTest dodawania null:");
        list.add(null);
        list.printStructure();
        System.out.println("\nTest dodawania emotki i słowa:");
        list.add("😊");
        list.add("Hello");
        list.printStructure();
        System.out.println("\nTest usuwania elementu:");
        Object removed = list.remove(2);
        System.out.println("Usunięto: " + removed);
        list.printStructure();
        System.out.println("\nTest usuwania metodą remove(element):");
        boolean removedBool = list.remove("b");
        System.out.println("Czy usunięto 'b': " + removedBool);
        list.printStructure();
    }
}