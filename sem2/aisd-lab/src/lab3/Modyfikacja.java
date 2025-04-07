package lab3;

public class Modyfikacja {
    public static void main(String[] args) {
        // Example usage of the TwoWayCycledListWithSentinel class
        TwoWayCycledListWithSentinel<Integer> list = new TwoWayCycledListWithSentinel<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);

        System.out.println("List size: " + list.size());
        System.out.println("Middle elem: " + list.middle());

        System.out.println("List size: " + list.size());
        System.out.println("Element at index 1: " + list.get(1));

        list.remove(1);
        System.out.println("List size after removal: " + list.size());

        list.clear();
        System.out.println(list.middle());
    }
}
