import java.util.*;

public class zad2 {

    public static <W, S> boolean path(
            zad1.IGraf<W, S> g,
            W start, W end,
            List<S> labels) {

        Set<W> visited = new HashSet<>();
        return dfs(g, start, end, visited, labels);
    }

    private static <W, S> boolean dfs(
            zad1.IGraf<W, S> g,
            W cur, W end,
            Set<W> vis,
            List<S> labels) {

        if (cur.equals(end)) return true;
        vis.add(cur);

        for (W nxt : g.krawedzie(cur)) {
            if (!vis.contains(nxt)) {
                labels.add(g.krawedz(cur, nxt));
                if (dfs(g, nxt, end, vis, labels))
                    return true;
                labels.remove(labels.size() - 1);
            }
        }
        return false;
    }
}
