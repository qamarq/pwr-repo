import java.util.*;

public class zad3 {

    public static <W, S> List<Set<W>> components(
            zad1.IGraf<W, S> g) {

        Set<W> visited = new HashSet<>();
        List<Set<W>> result = new ArrayList<>();

        for (W v : g.wierzcholki()) {
            if (!visited.contains(v)) {
                Set<W> comp = new HashSet<>();
                dfs(g, v, visited, comp);
                result.add(comp);
            }
        }
        return result;
    }

    private static <W, S> void dfs(
            zad1.IGraf<W, S> g,
            W v,
            Set<W> visited,
            Set<W> comp) {

        visited.add(v);
        comp.add(v);

        for (W n : g.krawedzie(v))
            if (!visited.contains(n))
                dfs(g, n, visited, comp);
    }
}
