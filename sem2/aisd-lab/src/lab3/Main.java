package lab3;

public class Main {
    public static void main(String[] args) {
        TwoWayLinkedList<Object> list = new TwoWayLinkedList<>();
        System.out.println("Dodawanie elementów: ");
        list.add(1);
        list.add("🚀");
        list.add(null);
        list.add(3.14);
        System.out.println(list);
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
