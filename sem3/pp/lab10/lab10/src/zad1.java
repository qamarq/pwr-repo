import java.util.*;

public class zad1 {
    public interface IGraf<W,S> {
        List<W> wierzcholki();

        S krawedz(W w1, W w2);

        List<W> krawedzie(W w);
    }

    public static class Graf<W, S> implements IGraf<W, S> {
        private final List<W> wierzcholki;
        private final Map<W, Integer> index;
        private final Object[][] matrix;

        public Graf(List<W> wierzcholki, S[][] edges) {
            this.wierzcholki = wierzcholki;
            int n = wierzcholki.size();

            index = new HashMap<>();
            for (int i = 0; i < n; i++)
                index.put(wierzcholki.get(i), i);

            matrix = new Object[n][n];
            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++)
                    matrix[i][j] = edges[i][j];
        }

        @Override
        public List<W> wierzcholki() {
            return wierzcholki;
        }

        @Override
        @SuppressWarnings("unchecked")
        public S krawedz(W w1, W w2) {
            Integer i = index.get(w1);
            Integer j = index.get(w2);
            if (i == null || j == null) return null;
            return (S) matrix[i][j];
        }

        @Override
        public List<W> krawedzie(W w) {
            List<W> res = new ArrayList<>();
            Integer i = index.get(w);
            if (i == null) return res;

            for (int j = 0; j < wierzcholki.size(); j++)
                if (matrix[i][j] != null)
                    res.add(wierzcholki.get(j));
            return res;
        }

        public int stopien(W w) {
            return krawedzie(w).size();
        }

        public boolean hasEdge(W w1, W w2) {
            return krawedz(w1, w2) != null;
        }
    }
}
