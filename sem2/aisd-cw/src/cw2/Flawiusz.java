package cw2;

import java.util.LinkedList;
import java.util.Queue;

public class Flawiusz {
    public static int józek(int n, int k) {
        Queue<Integer> queue = new LinkedList<>();

        // Wypełniamy kolejkę numerami od 1 do n
        for (int i = 1; i <= n; i++) {
            queue.add(i);
        }

        while (queue.size() > 1) {
            // Przesuwamy k-1 osób na koniec kolejki
            for (int i = 1; i < k; i++) {
                queue.add(queue.poll());
            }
            // Usuwamy k-tą osobę
            queue.poll();
        }

        return queue.peek();
    }

    public static void main(String[] args) {
        int n = 6;
        int k = 2;
        System.out.println("Ostatnia osoba, która przetrwa, to: " + józek(n, k));
    }
}
