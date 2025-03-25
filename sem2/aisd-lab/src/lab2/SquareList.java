package lab2;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SquareList<T> {

    // Lista wierszy, gdzie każdy wiersz jest listą dowiązaniową
    private List<LinkedList<T>> rows;

    public SquareList() {
        rows = new ArrayList<>();
    }

    // Zwraca łączną liczbę elementów w strukturze
    public int size() {
        int total = 0;
        for (LinkedList<T> row : rows) {
            total += row.size();
        }
        return total;
    }

    // Metoda pomocnicza do wyznaczenia optymalnej długości boku kwadratu K
    private int calculateK() {
        int n = size();
        return (int) Math.ceil(Math.sqrt(n));
    }

    // Znalezienie pozycji: przeliczamy indeks globalny na wiersz oraz offset
    private Position findPosition(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException("Index " + index + " out of range.");
        }
        int pos = index;
        for (int i = 0; i < rows.size(); i++) {
            LinkedList<T> row = rows.get(i);
            if (pos < row.size()) {
                return new Position(i, pos);
            } else {
                pos -= row.size();
            }
        }
        throw new RuntimeException("Element not found at index " + index);
    }

    // Pobranie elementu na zadanej pozycji
    public T get(int index) {
        Position pos = findPosition(index);
        return rows.get(pos.row).get(pos.col);
    }

    // Wstawienie elementu na końcu listy
    public void add(T element) {
        add(size(), element);
    }

    // Wstawienie elementu w określone miejsce (indeks globalny)
    public void add(int index, T element) {
        if (index < 0 || index > size()) {
            throw new IndexOutOfBoundsException("Index " + index + " out of range.");
        }
        // Jeżeli struktura jest pusta – dodaj pierwszy wiersz
        if (rows.isEmpty()) {
            LinkedList<T> newRow = new LinkedList<>();
            newRow.add(element);
            rows.add(newRow);
        } else if (index == size()) {
            // Dodanie na końcu – sprawdzamy ostatni wiersz
            LinkedList<T> lastRow = rows.get(rows.size() - 1);
            lastRow.add(element);
        } else {
            Position pos = findPosition(index);
            rows.get(pos.row).add(pos.col, element);
        }
        // Po operacji wywołujemy rebalansowanie, jeśli struktura odbiega od ideału
        rebalance();
    }

    // Usunięcie elementu spod zadanego indeksu (globalny)
    public T remove(int index) {
        Position pos = findPosition(index);
        T removed = rows.get(pos.row).remove(pos.col);
        // Po usunięciu elementu wywołujemy rebalansowanie struktury
        rebalance();
        return removed;
    }

    // Rebalansowanie – zbieramy wszystkie elementy i rozdzielamy na kolejne wiersze o długości nie przekraczającej K.
    private void rebalance() {
        int total = size();
        if (total == 0) {
            rows.clear();
            return;
        }
        int k = calculateK();

        // Zbierz wszystkie elementy do jednej listy tymczasowej
        List<T> temp = new ArrayList<>(total);
        for (LinkedList<T> row : rows) {
            temp.addAll(row);
        }

        // Wyczyść strukturę wierszy i ułóż elementy od nowa
        rows.clear();
        for (int i = 0; i < total; i += k) {
            LinkedList<T> newRow = new LinkedList<>();
            int end = Math.min(i + k, total);
            for (int j = i; j < end; j++) {
                newRow.add(temp.get(j));
            }
            rows.add(newRow);
        }
    }

    // Klasa pomocnicza, przechowująca informację o pozycji: numer wiersza i kolumny (offset w wierszu)
    private class Position {
        int row;
        int col;
        Position(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

    // Metoda do drukowania stanu struktury (użyteczna przy testach)
    public void printStructure() {
        System.out.println("SquareList (total elements: " + size() + "):");
        for (int i = 0; i < rows.size(); i++) {
            System.out.println("Row " + i + ": " + rows.get(i));
        }
    }

    // Przykładowe testy działania struktury
    public static void main(String[] args) {
        SquareList<Integer> squareList = new SquareList<>();

        // Test 1: Dodawanie elementów na końcu
        for (int i = 1; i <= 10; i++) {
            squareList.add(i);
        }
        System.out.println("Po dodaniu elementów 1-10:");
        squareList.printStructure();

        // Test 2: Wstawianie elementów w środek
        squareList.add(5, 99);
        System.out.println("\nPo wstawieniu 99 na indeks 5:");
        squareList.printStructure();

        // Test 3: Usuwanie elementu
        int removed = squareList.remove(3);
        System.out.println("\nPo usunięciu elementu spod indeksu 3 (usunięto: " + removed + "):");
        squareList.printStructure();

        // Test 4: Dostęp do elementu
        System.out.println("\nElement na pozycji 4: " + squareList.get(4));
    }
}
