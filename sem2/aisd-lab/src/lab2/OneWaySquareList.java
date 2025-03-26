package lab2;

import org.w3c.dom.Node;

import java.util.*;

public class OneWaySquareList<E> implements IList<E> {

    private OneWayLinkedListWithHead<OneWayLinkedListWithHead<E>> rows;

    public OneWaySquareList() {
        rows = new OneWayLinkedListWithHead<>();
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
            OneWayLinkedListWithHead<E> newRow = new OneWayLinkedListWithHead<>();
            newRow.add(element);
            rows.add(newRow);
        } else if (index == size()) {
            getLastRow().add(element);
        } else {
            Position pos = findPosition(index);
            rows.get(pos.row).add(pos.col, element);
        }
        rebalance();
    }

    private OneWayLinkedListWithHead<E> getLastRow() {
        if (rows.isEmpty()) return null;
        return rows.get(rows.size() - 1);
    }

    public void clear() {
        rows.clear();
    }

    public boolean contains(E element) {
        for (OneWayLinkedListWithHead<E> row : rows) {
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
        for (OneWayLinkedListWithHead<E> row : rows) {
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
        return new Iterator<E>() {
            private int row = 0;
            private int col = 0;

            @Override
            public boolean hasNext() {
                return row < rows.size() && col < rows.get(row).size();
            }

            @Override
            public E next() {
                E element = rows.get(row).get(col);
                col++;
                if (col == rows.get(row).size()) {
                    row++;
                    col = 0;
                }
                return element;
            }
        };
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
        for (OneWayLinkedListWithHead<E> row : rows) {
            total += row.size();
        }
        return total;
    }

    private int calculateK() {
        int n = size();
        return (int) Math.round(Math.sqrt(n));
    }

    private E middle() {
        Iterator<E> slow = this.iterator();
        Iterator<E> fast = this.iterator();

        while (fast.hasNext()) {
            fast.next();
            if (fast.hasNext()) {
                fast.next();
                slow.next();
            }
        }

        return slow.next();
    }

    private void rebalance() {
        int total = size();
        if (total == 0) {
            rows.clear();
            return;
        }

        int k = calculateK();
        List<E> temp = new ArrayList<>(total);

        for (int r = 0; r < rows.size(); r++) {
            OneWayLinkedListWithHead<E> row = rows.get(r);
            for (int i = 0; i < row.size(); i++) {
                temp.add(row.get(i));
            }
        }

        rows.clear();
        for (int i = 0; i < total; i += k) {
            OneWayLinkedListWithHead<E> newRow = new OneWayLinkedListWithHead<>();
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
        for (int r = 0; r < rows.size(); r++) {
            OneWayLinkedListWithHead<E> row = rows.get(r);
            if (pos < row.size()) {
                return new Position(r, pos);
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
        for (int r = 0; r < rows.size(); r++) {
            System.out.printf("| %-4d | %-29s |\n", r, rows.get(r));
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

        System.out.println("\n============================================");
        System.out.println("Test listy jednoelementowej:");
        list.add("jedyny");
        list.printStructure();
        System.out.println("indexOf('jedyny'): " + list.indexOf("jedyny"));
        System.out.println("set(0, 'zmieniony'): " + list.set(0, "zmieniony"));
        list.printStructure();
        list.clear();


        System.out.println("\n============================================");
        System.out.println("Test dodawania wielu elementów (nieparzysta liczba elementów):");
        list.add(1);
        list.printStructure();
        System.out.println("Middle: " + list.middle());
        list.add(2);
        list.printStructure();
        System.out.println("Middle: " + list.middle());
        list.add(3);
        list.printStructure();
        System.out.println("Middle: " + list.middle());
        list.add(4);
        list.printStructure();
        System.out.println("Middle: " + list.middle());
        list.add(5);
        list.printStructure();
        System.out.println("Middle: " + list.middle());
        list.add(6);
        list.printStructure();
        System.out.println("Middle: " + list.middle());
        list.add(7);
        list.printStructure();
        System.out.println("Middle: " + list.middle());
        list.add(8);
        list.printStructure();
        System.out.println("Middle: " + list.middle());
        list.add(9);
        list.printStructure();
        System.out.println("Middle: " + list.middle());

        System.out.println("\n============================================");
        System.out.println("Test dodawania wielu elementów (parzysta liczba elementów):");
        list.clear();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        list.printStructure();


        System.out.println("\n============================================");
        System.out.println("Test dodawania na niepoprawnym indeksie:");
        try {
            list.add(100, "out");
        } catch (Exception e) {
            System.out.println("Błąd: " + e.getMessage());
        }


        System.out.println("\n============================================");
        System.out.println("Test dodawania null:");
        list.add(null);
        list.printStructure();


        System.out.println("\n============================================");
        System.out.println("Test dodawania emotki i słowa:");
        list.add("😊");
        list.add("Hello");
        list.printStructure();


        System.out.println("\n============================================");
        System.out.println("Test usuwania elementu:");
        Object removed = list.remove(2);
        System.out.println("Usunięto: " + removed);
        list.printStructure();


        System.out.println("\n============================================");
        System.out.println("Test usuwania metodą remove(element):");
        boolean removedBool = list.remove("b");
        System.out.println("Czy usunięto 'b': " + removedBool);
        list.printStructure();



        System.out.println("\n============================================");
        System.out.println("Test get middle");
        System.out.println("Middle: " + list.middle());


        System.out.println("\n============================================");
        System.out.println("Test get middle null list");
        list.clear();
        System.out.println("Middle: " + list.middle());
    }
}