import java.util.*;

public class zad3 {

    public static <W, S> List<Set<W>> components(zad1.IGraf<W, S> g) {
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
            Set<W> vis,
            Set<W> comp) {

        vis.add(v);
        comp.add(v);

        for (W n : g.krawedzie(v))
            if (!vis.contains(n))
                dfs(g, n, vis, comp);

        for (W n : g.wierzcholki())
            if (g.krawedz(n, v) != null && !vis.contains(n))
                dfs(g, n, vis, comp);
    }
}
