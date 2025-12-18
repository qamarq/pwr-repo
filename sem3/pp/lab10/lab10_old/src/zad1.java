import java.util.*;

public class zad1 {
    public interface IGraf<W, S> {
        List<W> wierzcholki();
        S krawedz(W w1, W w2);
        List<W> krawedzie(W w);
    }

    public static class Graf<W, S> implements IGraf<W, S> {

        private final List<W> vertices;
        private final Map<W, Map<W, S>> adj;

        public Graf(List<W> vertices, Map<W, Map<W, S>> adj) {
            this.vertices = List.copyOf(vertices);
            this.adj = new HashMap<>();
            for (W v : vertices)
                this.adj.put(v, Map.copyOf(adj.getOrDefault(v, Map.of())));
        }

        public List<W> wierzcholki() {
            return vertices;
        }

        public S krawedz(W w1, W w2) {
            return adj.getOrDefault(w1, Map.of()).get(w2);
        }

        public List<W> krawedzie(W w) {
            return new ArrayList<>(adj.getOrDefault(w, Map.of()).keySet());
        }

        public int outDegree(W w) {
            return adj.getOrDefault(w, Map.of()).size();
        }

        public List<W> neighborsIn(W w) {
            List<W> res = new ArrayList<>();
            for (W v : vertices)
                if (adj.getOrDefault(v, Map.of()).containsKey(w))
                    res.add(v);
            return res;
        }
    }
}
