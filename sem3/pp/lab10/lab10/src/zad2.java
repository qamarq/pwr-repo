import java.util.*;

public class zad2 {

    public static <W, S> boolean path(
            zad1.IGraf<W, S> g,
            W start,
            W end,
            List<S> labels) {

        Set<W> visited = new HashSet<>();
        return dfs(g, start, end, visited, labels);
    }

    private static <W, S> boolean dfs(
            zad1.IGraf<W, S> g,
            W current,
            W end,
            Set<W> visited,
            List<S> labels) {

        if (current.equals(end))
            return true;

        visited.add(current);

        for (W next : g.krawedzie(current)) {
            if (!visited.contains(next)) {
                labels.add(g.krawedz(current, next));
                if (dfs(g, next, end, visited, labels))
                    return true;
                labels.removeLast();
            }
        }
        return false;
    }
}
