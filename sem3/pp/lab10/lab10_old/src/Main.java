import java.util.*;

public class Main {

    public static void main(String[] args) {

        // ===== DANE TESTOWE =====
        List<String> v = List.of("A", "B", "C", "D", "E");

        Map<String, Map<String, Integer>> adj = new HashMap<>();
        adj.put("A", Map.of("B", 1, "C", 2));
        adj.put("B", Map.of("D", 3));
        adj.put("C", Map.of("D", 4));
        adj.put("D", Map.of());
        adj.put("E", Map.of());

        zad1.Graf<String, Integer> g =
                new zad1.Graf<>(v, adj);

        // ===== zadANIE 1 – TESTY =====
        System.out.println("zad1:");

        assert g.wierzcholki().size() == 5;
        assert g.krawedz("A", "B") == 1;
        assert g.krawedz("B", "A") == null;
        assert g.outDegree("A") == 2;
        assert g.neighborsIn("D").containsAll(List.of("B", "C"));

        System.out.println("zad1 OK");

        // ===== zadANIE 2 – TESTY =====
        System.out.println("zad2:");

        List<Integer> labels = new ArrayList<>();
        boolean exists = zad2.path(g, "A", "D", labels);

        assert exists;
        assert labels.size() == 2;       // A->B->D albo A->C->D
        assert labels.get(0) == 1 || labels.get(0) == 2;

        List<Integer> labels2 = new ArrayList<>();
        assert !zad2.path(g, "D", "A", labels2);

        System.out.println("zad2 OK");

        // ===== zadANIE 3 – TESTY =====
        System.out.println("zad3:");

        List<Set<String>> comps = zad3.components(g);

        assert comps.size() == 2;

        boolean foundBig = false;
        boolean foundSingle = false;

        for (Set<String> c : comps) {
            if (c.containsAll(List.of("A","B","C","D")))
                foundBig = true;
            if (c.contains("E") && c.size() == 1)
                foundSingle = true;
        }

        assert foundBig && foundSingle;

        System.out.println("zad3 OK");

        System.out.println("\nWSZYSTKIE TESTY ZALICZONE");
    }
}
