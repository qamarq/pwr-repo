import java.util.*;

public class Main {

    public static void main(String[] args) {

        List<String> v = List.of("A", "B", "C", "D", "E");

        Integer[][] m = {
                { null, 1,    null, null, null },
                { 1,    null, 2,    null, null },
                { null, 2,    null, 3,    null },
                { null, null, 3,    null, null },
                { null, null, null, null, null }
        };

        zad1.Graf<String, Integer> g =
                new zad1.Graf<>(v, m);

        System.out.println("zad1");
        System.out.println(g.stopien("A") == 1);
        System.out.println(g.hasEdge("B", "C"));
        System.out.println(!g.hasEdge("A", "C"));
        System.out.println(g.krawedz("A", "X") == null);
        System.out.println(g.stopien("E") == 0);

        System.out.println("zad2");
        List<Integer> l1 = new ArrayList<>();
        System.out.println(zad2.path(g, "A", "D", l1));
        System.out.println(l1);

        List<Integer> l2 = new ArrayList<>();
        System.out.println(!zad2.path(g, "A", "E", l2));

        List<Integer> l3 = new ArrayList<>();
        System.out.println(zad2.path(g, "A", "A", l3));
        System.out.println(l3.isEmpty());

        System.out.println("zad3");
        List<Set<String>> comps = zad3.components(g);

        System.out.println(comps.size() == 2);

        boolean big = false;
        boolean single = false;

        for (Set<String> c : comps) {
            if (c.containsAll(List.of("A","B","C","D")))
                big = true;
            if (c.equals(Set.of("E")))
                single = true;
        }

        System.out.println(big);
        System.out.println(single);
    }
}
