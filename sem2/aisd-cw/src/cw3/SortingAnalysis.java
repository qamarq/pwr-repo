package cw3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class SortingAnalysis {

    // --- Helper Methods ---
    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    private static void printArray(int[] arr, String prefix) {
        System.out.print(prefix + "[");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + (i == arr.length - 1 ? "" : ", "));
        }
        System.out.println("]");
    }

    private static void printArrayState(int[] arr, int step, String description) {
        System.out.printf("Krok %2d (%-30s): %s\n", step, description, Arrays.toString(arr));
    }

    private static void printArrayStatePass(int[] arr, int pass, String description) {
        System.out.printf("Przejście %2d (%-20s): %s\n", pass, description, Arrays.toString(arr));
    }


    // --- Task 1: Amortized Analysis of ArrayList.add() ---
    public static void explainArrayListAddAmortized() {
        System.out.println("--- Zadanie 1: Koszt zamortyzowany ArrayList.add() ---");
        System.out.println("Uzasadnienie złożoności O(1) dla ArrayList.add():");
        System.out.println("Operacja add() w ArrayList dodaje element na końcu listy.");
        System.out.println("1. Przypadek trywialny: Jeśli w wewnętrznej tablicy ArrayList jest wolne miejsce,");
        System.out.println("   dodanie elementu polega jedynie na wpisaniu go pod kolejny indeks i ");
        System.out.println("   inkrementacji licznika rozmiaru. Koszt tej operacji wynosi O(1).");
        System.out.println("2. Przypadek rozszerzenia: Jeśli wewnętrzna tablica jest pełna, ArrayList musi");
        System.out.println("   zwiększyć swoją pojemność. Zazwyczaj implementacja tworzy nową, większą");
        System.out.println("   tablicę (często o podwójnej pojemności), kopiuje wszystkie istniejące ");
        System.out.println("   elementy ze starej tablicy do nowej, a następnie dodaje nowy element.");
        System.out.println("   Koszt tej operacji (głównie kopiowanie) jest proporcjonalny do aktualnej ");
        System.out.println("   liczby elementów N, czyli wynosi O(N).");
        System.out.println();
        System.out.println("Analiza zamortyzowana:");
        System.out.println("Chociaż pojedyncza operacja add() może kosztować O(N), zdarza się to stosunkowo rzadko.");
        System.out.println("Rozważmy dodanie N elementów do pustej ArrayList, zaczynając od małej pojemności (np. C=1).");
        System.out.println("Załóżmy, że pojemność jest podwajana przy każdym przepełnieniu.");
        System.out.println("Operacje rozszerzenia (kosztowne) wystąpią, gdy rozmiar osiągnie 1, 2, 4, 8, ..., 2^k, gdzie 2^k < N.");
        System.out.println("Koszt N operacji add() to suma kosztów O(1) dla każdego dodania oraz kosztów O(i) dla operacji rozszerzenia przy rozmiarach i = 1, 2, 4, ..., 2^k.");
        System.out.println("Całkowity koszt kopiowania to C * (1 + 2 + 4 + ... + 2^k), gdzie C to stała.");
        System.out.println("Suma tej serii geometrycznej to C * (2^(k+1) - 1), co jest mniejsze niż C * 2N.");
        System.out.println("Całkowity koszt dodania N elementów to: N (za proste dodania) + koszt_kopiowania < N + C * 2N = (1 + 2C) * N.");
        System.out.println("Średni koszt (koszt zamortyzowany) pojedynczej operacji add() to całkowity koszt podzielony przez N:");
        System.out.println("   Koszt_zamortyzowany = Całkowity_koszt / N < ((1 + 2C) * N) / N = 1 + 2C");
        System.out.println("Ponieważ 1 + 2C jest stałą, zamortyzowany koszt operacji add() wynosi O(1).");
        System.out.println("Oznacza to, że średnio, w dłuższej sekwencji operacji, koszt dodania elementu jest stały.");
        System.out.println();
    }

    // --- Task 2: Amortized Analysis of nextPermutation() ---
    public static void explainNextPermutationAmortized() {
        System.out.println("--- Zadanie 2: Koszt zamortyzowany nextPermutation() ---");
        System.out.println("Uzasadnienie złożoności O(1) dla nextPermutation():");
        System.out.println("Standardowy algorytm generowania następnej permutacji leksykograficznej działa następująco:");
        System.out.println("1. Znajdź największy indeks `k` taki, że `a[k] < a[k+1]`.");
        System.out.println("2. Znajdź największy indeks `l > k` taki, że `a[k] < a[l]`.");
        System.out.println("3. Zamień `a[k]` z `a[l]`.");
        System.out.println("4. Odwróć kolejność elementów w podciągu od `a[k+1]` do końca.");
        System.out.println();
        System.out.println("Analiza zamortyzowana:");
        System.out.println("Rozważmy sekwencję wywołań nextPermutation() zaczynając od permutacji posortowanej rosnąco, aż do ostatniej permutacji (posortowanej malejąco).");
        System.out.println("Koszt pojedynczego wywołania zależy od tego, jak daleko w lewo musimy szukać indeksu `k` (krok 1) oraz jak długi jest podciąg do odwrócenia (krok 4).");
        System.out.println("W najgorszym przypadku (np. przejście z [1, 4, 3, 2] do [2, 1, 3, 4]) musimy przejrzeć prawie całą tablicę i odwrócić długi podciąg, co daje koszt O(N).");
        System.out.println("Jednak takie przypadki są rzadkie. Większość wywołań nextPermutation() modyfikuje tylko krótki sufiks tablicy.");
        System.out.println("Przykład: dla [1, 2, 3, 4] -> [1, 2, 4, 3], tylko 2 ostatnie elementy są zmieniane (O(1) operacji).");
        System.out.println("             dla [1, 3, 4, 2] -> [1, 4, 2, 3], modyfikowane są 3 ostatnie elementy.");
        System.out.println();
        System.out.println("Bardziej formalna analiza (np. metodą potencjału lub agregatową) pokazuje, że całkowita liczba operacji (porównań, zamian) wykonanych podczas generowania *wszystkich* N! permutacji jest proporcjonalna do N!.");
        System.out.println("Intuicja: Każdy element jest przesuwany (zamieniany lub odwracany) średnio stałą liczbę razy w całym procesie generowania permutacji.");
        System.out.println("Całkowity koszt wygenerowania N! permutacji to O(N!).");
        System.out.println("Średni koszt (koszt zamortyzowany) pojedynczego wywołania nextPermutation() to:");
        System.out.println("   Koszt_zamortyzowany = Całkowity_koszt / Liczba_wywołań = O(N!) / N! = O(1).");
        System.out.println("Oznacza to, że średni koszt przejścia do następnej permutacji jest stały.");
        System.out.println();
    }

    // --- Task 3: Insertion Sort (Descending, Right-to-Left Sorted Part) ---
    public static void insertionSortDescendingRight(int[] arr) {
        System.out.println("--- Zadanie 3: Sortowanie przez wstawianie (malejąco, część posortowana od prawej) ---");
        int n = arr.length;
        if (n == 0) return;

        printArrayState(arr, 0, "Stan początkowy");

        // Zaczynamy od przedostatniego elementu, idąc w lewo.
        // Element a[n-1] jest trywialnie posortowaną częścią.
        for (int i = n - 2; i >= 0; i--) {
            int key = arr[i]; // Element do wstawienia w posortowaną część
            int j = i + 1;    // Indeks pierwszego elementu w posortowanej części (na prawo od i)

            // Przesuwamy elementy w posortowanej części (a[j...n-1]), które są WIĘKSZE od key,
            // o jedno miejsce w lewo (a[j-1] = a[j]), aby zrobić miejsce dla key.
            // Przesuwamy, dopóki j < n i element a[j] jest większy niż key.
            while (j < n && arr[j] > key) {
                arr[j - 1] = arr[j];
                j++;
            }
            // Wstawiamy key na właściwą pozycję (tam, gdzie skończyło się przesuwanie)
            arr[j - 1] = key;

            printArrayState(arr, n - 1 - i, "Po wstawieniu arr[" + i + "]=" + key);
        }
        System.out.println();
    }

    // --- Task 4: Selection Sort (Descending, Right-to-Left Sorted Part) ---
    public static void selectionSortDescendingRight(int[] arr) {
        System.out.println("--- Zadanie 4: Sortowanie przez wybór (malejąco, część posortowana od prawej) ---");
        int n = arr.length;
        if (n == 0) return;

        printArrayState(arr, 0, "Stan początkowy");

        // Iterujemy od końca tablicy (i = n-1) do drugiego elementu (i = 1).
        // W każdej iteracji umieszczamy na pozycji a[i] odpowiedni element (największy z pozostałych).
        // Posortowana część rośnie od prawej: a[i...n-1].
        for (int i = n - 1; i > 0; i--) {
            // Znajdujemy indeks największego elementu w nieposortowanej części a[0...i]
            int maxIndex = 0;
            for (int j = 1; j <= i; j++) {
                if (arr[j] > arr[maxIndex]) {
                    maxIndex = j;
                }
            }

            // Zamieniamy znaleziony największy element (arr[maxIndex]) z elementem na końcu
            // nieposortowanej części (arr[i])
            swap(arr, maxIndex, i);

            printArrayState(arr, n - i, "Po znalezieniu max w a[0.." + i + "] i zamianie z a[" + i + "]");
        }
        System.out.println();
    }

    // --- Task 5: Bubble Sort (Descending, Left-to-Right Sorted Part) ---
    public static void bubbleSortDescendingLeft(int[] arr) {
        System.out.println("--- Zadanie 5: Sortowanie bąbelkowe (malejąco, część posortowana od lewej) ---");
        int n = arr.length;
        if (n == 0) return;

        printArrayStatePass(arr, 0, "Stan początkowy");

        // W każdej zewnętrznej pętli 'i', największy element z nieposortowanej części
        // 'wypływa' na pozycję 'i'. Posortowana część a[0...i] rośnie od lewej.
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            // Pętla wewnętrzna przesuwa się od końca do aktualnej granicy posortowanej części 'i'
            // Porównujemy sąsiadów a[j] i a[j-1]. Chcemy malejąco, więc jeśli a[j] > a[j-1], zamieniamy.
            // To powoduje, że największy element 'wędruje' w lewo.
            for (int j = n - 1; j > i; j--) {
                if (arr[j] > arr[j - 1]) {
                    swap(arr, j, j - 1);
                    swapped = true;
                }
            }
            printArrayStatePass(arr, i + 1, "Po przejściu " + (i+1));
            // Optymalizacja: jeśli w danym przejściu nie było zamian, tablica jest posortowana
            if (!swapped) {
                System.out.println("   (Tablica posortowana, przerwanie)");
                break;
            }
        }
        System.out.println();
    }

    // --- Task 6a: Shaker Sort (Basic) ---
    public static void shakerSortBasic(int[] arr) {
        System.out.println("--- Zadanie 6a: ShakerSort (wersja podstawowa, sortowanie rosnąco) ---");
        int n = arr.length;
        if (n == 0) return;

        int left = 0;
        int right = n - 1;
        int pass = 0;

        printArrayStatePass(arr, pass, "Stan początkowy");

        do {
            boolean swapped = false;
            pass++;
            // Przejście w prawo (jak w BubbleSort - największe na koniec)
            for (int i = left; i < right; i++) {
                if (arr[i] > arr[i + 1]) {
                    swap(arr, i, i + 1);
                    swapped = true;
                }
            }
            printArrayStatePass(arr, pass, "Przejście w prawo");
            if (!swapped) break; // Optymalizacja: jeśli nie było zamian, zakończ
            right--; // Największy element jest na miejscu, zmniejszamy prawy kraniec

            swapped = false; // Reset flagi dla przejścia w lewo
            pass++;
            // Przejście w lewo (najmniejsze na początek)
            for (int i = right; i > left; i--) {
                if (arr[i] < arr[i - 1]) {
                    swap(arr, i, i - 1);
                    swapped = true;
                }
            }
            printArrayStatePass(arr, pass, "Przejście w lewo");
            if (!swapped) break; // Optymalizacja: jeśli nie było zamian, zakończ
            left++; // Najmniejszy element jest na miejscu, zwiększamy lewy kraniec

        } while (left < right); // Kontynuuj, dopóki granice się nie spotkają
        System.out.println();
    }

    // --- Task 6b/7: Shaker Sort (Improved) with trace ---
    public static void shakerSortImproved(int[] arr) {
        System.out.println("--- Zadanie 6b/7: ShakerSort (ulepszony, sortowanie rosnąco) ---");
        int n = arr.length;
        if (n == 0) return;

        int left = 0;
        int right = n - 1;
        int lastSwap;
        int pass = 0;

        printArrayStatePass(arr, pass, "Stan początkowy");

        do {
            pass++;
            lastSwap = left; // Ulepszenie 1: Zapamiętaj ostatnią zamianę
            boolean swappedRight = false; // Ulepszenie 2: Flaga wczesnego wyjścia

            // --- Przejście w prawo ---
            for (int i = left; i < right; i++) {
                if (arr[i] > arr[i + 1]) {
                    swap(arr, i, i + 1);
                    lastSwap = i; // Zapamiętaj pozycję ostatniej zamiany
                    swappedRight = true;
                }
            }
            right = lastSwap; // Zaktualizuj prawą granicę na pozycję ostatniej zamiany
            printArrayStatePass(arr, pass, "Przejście w prawo");

            // Ulepszenie 2: Sprawdź, czy były zamiany w tym przejściu
            if (!swappedRight) {
                System.out.println("   (Brak zamian w prawo, przerwanie)");
                break;
            }

            pass++;
            lastSwap = right; // Ulepszenie 1: Zapamiętaj ostatnią zamianę
            boolean swappedLeft = false; // Ulepszenie 2: Flaga wczesnego wyjścia

            // --- Przejście w lewo ---
            for (int i = right; i > left; i--) {
                if (arr[i] < arr[i - 1]) {
                    swap(arr, i, i - 1);
                    lastSwap = i; // Zapamiętaj pozycję ostatniej zamiany
                    swappedLeft = true;
                }
            }
            left = lastSwap; // Zaktualizuj lewą granicę na pozycję ostatniej zamiany
            printArrayStatePass(arr, pass, "Przejście w lewo");

            // Ulepszenie 2: Sprawdź, czy były zamiany w tym przejściu
            if (!swappedLeft) {
                System.out.println("   (Brak zamian w lewo, przerwanie)");
                break;
            }

        } while (left < right); // Kontynuuj, dopóki granice się nie spotkają
        System.out.println();
    }

    // --- Task 8: Permutation Sort ---

    // Helper: Checks if array is sorted ascending
    private static boolean isSorted(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] > arr[i + 1]) {
                return false;
            }
        }
        return true;
    }

    // Helper: Generates the next lexicographical permutation
    // Returns false if it was the last permutation
    private static boolean nextPermutation(int[] arr) {
        int n = arr.length;
        // 1. Find k
        int k = n - 2;
        while (k >= 0 && arr[k] >= arr[k + 1]) {
            k--;
        }
        if (k < 0) {
            return false; // Last permutation
        }

        // 2. Find l
        int l = n - 1;
        while (arr[k] >= arr[l]) {
            l--;
        }

        // 3. Swap a[k] and a[l]
        swap(arr, k, l);

        // 4. Reverse from k+1 to end
        int left = k + 1;
        int right = n - 1;
        while (left < right) {
            swap(arr, left, right);
            left++;
            right--;
        }
        return true;
    }

    public static void permutationSort(int[] arr) {
        System.out.println("--- Zadanie 8: Sortowanie przez permutacje ---");
        long startTime = System.nanoTime();
        long permutationsChecked = 0;
        int[] currentPermutation = Arrays.copyOf(arr, arr.length);
        // Sort initial array to start generation from the beginning (optional, safer)
        // Arrays.sort(currentPermutation);

        System.out.println("Rozpoczęcie sortowania przez permutacje dla n = " + arr.length);
        printArray(currentPermutation, "Stan początkowy: ");

        if (isSorted(currentPermutation)) {
            System.out.println("Tablica początkowa jest już posortowana.");
        } else {
            // Generate permutations until sorted
            // Use a copy to iterate, modify the original 'arr' inside the loop if needed
            // Or better: Keep modifying 'currentPermutation'
            do {
                permutationsChecked++;
                if (isSorted(currentPermutation)) {
                    System.arraycopy(currentPermutation, 0, arr, 0, arr.length); // Copy result back
                    break;
                }
                // Add a check for excessive time or permutations for safety
                if (permutationsChecked % 1_000_000 == 0) { // Print progress
                    long elapsed = System.nanoTime() - startTime;
                    System.out.printf("... Sprawdzono %,d permutacji (%.2f s)\n",
                            permutationsChecked, elapsed / 1_000_000_000.0);
                    if (TimeUnit.NANOSECONDS.toSeconds(elapsed) > 90) { // Timeout check
                        System.out.println("Przekroczono limit czasu (90s). Przerywam.");
                        return; // Exit if taking too long
                    }
                }

            } while (nextPermutation(currentPermutation)); // Generate next one
        }


        long endTime = System.nanoTime();
        long durationNanos = endTime - startTime;
        double durationSec = durationNanos / 1_000_000_000.0;

        printArray(arr, "Wynik sortowania: ");
        System.out.printf("Liczba sprawdzonych permutacji: %,d\n", permutationsChecked + 1); // +1 for initial check
        System.out.printf("Czas wykonania dla n=%d: %.4f sekund\n", arr.length, durationSec);

        // Finding n where time > 1 minute (worst case: reverse sorted array)
        if (durationSec < 60) {
            System.out.println("\nPróba znalezienia n, dla którego czas > 60 sekund (najgorszy przypadek)...");
            for (int n = arr.length + 1; ; n++) {
                System.out.println("Testowanie dla n = " + n);
                int[] worstCaseArr = new int[n];
                for (int i = 0; i < n; i++) {
                    worstCaseArr[i] = n - i; // Reverse sorted
                }
                long startN = System.nanoTime();
                long pCheckedN = 0;
                int[] currentP = Arrays.copyOf(worstCaseArr, n);
                // Start permutation generation explicitly from the worst case
                do {
                    pCheckedN++;
                    if (isSorted(currentP)) {
                        break;
                    }
                    // Safety break for very large n
                    long elapsedN = System.nanoTime() - startN;
                    if (pCheckedN % 50_000_000 == 0) { // Less frequent progress
                        System.out.printf("... [n=%d] Sprawdzono %,d permutacji (%.2f s)\n",
                                n, pCheckedN, elapsedN / 1_000_000_000.0);
                    }
                    if (TimeUnit.NANOSECONDS.toSeconds(elapsedN) > 90) { // Safety timeout
                        System.out.printf("... [n=%d] Przekroczono limit czasu (90s) po %,d permutacjach. Przerywam.\n", n, pCheckedN);
                        pCheckedN = -1; // Indicate timeout
                        break;
                    }
                } while (nextPermutation(currentP));

                long endN = System.nanoTime();
                long durationNanosN = endN - startN;
                double durationSecN = durationNanosN / 1_000_000_000.0;

                if (pCheckedN != -1) {
                    System.out.printf("Czas dla n=%d (najgorszy przypadek): %.4f sekund (sprawdzono %,d permutacji)\n", n, durationSecN, pCheckedN +1);
                }

                if (durationSecN > 60.0 || pCheckedN == -1) {
                    System.out.println("\n>>> Dla n = " + n + " czas wykonania w najgorszym przypadku przekroczył 1 minutę.");
                    break;
                }
                if (n > 13) { // Factorial grows extremely fast, limit search
                    System.out.println("\n>>> Przerywam poszukiwanie n (osiągnięto n=14). Czas rośnie bardzo szybko.");
                    break;
                }
            }
        }
        System.out.println();
    }

    // --- Task 9: Bogo Sort (Shuffle Sort) ---

    // Helper: Fisher-Yates shuffle
    private static void shuffle(int[] arr, Random rand) {
        for (int i = arr.length - 1; i > 0; i--) {
            int index = rand.nextInt(i + 1);
            swap(arr, i, index);
        }
    }

    public static void bogoSort(int[] arr) {
        System.out.println("--- Zadanie 9: Sortowanie zwariowane (BogoSort) ---");
        Random random = new Random();
        long startTime = System.nanoTime();
        long shuffles = 0;
        int n = arr.length;

        System.out.println("Rozpoczęcie BogoSort dla n = " + n);
        printArray(arr, "Stan początkowy: ");

        while (!isSorted(arr)) {
            shuffle(arr, random);
            shuffles++;
            // Add a check for excessive time or shuffles
            if (shuffles % 500_000 == 0) { // Print progress occasionally
                long elapsed = System.nanoTime() - startTime;
                System.out.printf("... Wykonano %,d przetasowań (%.2f s)\n",
                        shuffles, elapsed / 1_000_000_000.0);
                if (TimeUnit.NANOSECONDS.toSeconds(elapsed) > 90) { // Timeout check
                    System.out.println("Przekroczono limit czasu (90s). Przerywam BogoSort.");
                    return; // Exit if taking too long
                }
            }
        }

        long endTime = System.nanoTime();
        long durationNanos = endTime - startTime;
        double durationSec = durationNanos / 1_000_000_000.0;

        printArray(arr, "Wynik sortowania: ");
        System.out.printf("Liczba przetasowań: %,d\n", shuffles);
        System.out.printf("Czas wykonania dla n=%d: %.4f sekund\n", n, durationSec);

        // Finding n where time > 1 minute
        if (durationSec < 60) {
            System.out.println("\nPróba znalezienia n, dla którego czas > 60 sekund...");
            Random randN = new Random();
            for (int currentN = n + 1; ; currentN++) {
                System.out.println("Testowanie BogoSort dla n = " + currentN);
                int[] testArr = new int[currentN];
                // Fill with random (or worst case like reverse, but random is typical for bogo)
                for (int i = 0; i < currentN; i++) {
                    testArr[i] = randN.nextInt(1000); // Example random data
                }
                // Make a copy to sort
                int[] arrToSort = Arrays.copyOf(testArr, currentN);

                long startN = System.nanoTime();
                long shufflesN = 0;
                while (!isSorted(arrToSort)) {
                    shuffle(arrToSort, randN);
                    shufflesN++;

                    long elapsedN = System.nanoTime() - startN;
                    // Check time frequently for BogoSort as it's unpredictable
                    if (shufflesN % 1_000_000 == 0) { // Progress check
                        System.out.printf("... [n=%d] %,d przetasowań (%.2f s)\n",
                                currentN, shufflesN, elapsedN / 1_000_000_000.0);
                    }
                    if (TimeUnit.NANOSECONDS.toSeconds(elapsedN) > 70) { // Stop a bit before 1 min to report
                        System.out.printf("Przekroczono 70 sekund dla n=%d po %,d przetasowaniach. Przerywam test.\n", currentN, shufflesN);
                        shufflesN = -1; // Indicate timeout
                        break;
                    }
                }

                long endN = System.nanoTime();
                long durationNanosN = endN - startN;
                double durationSecN = durationNanosN / 1_000_000_000.0;

                if (shufflesN != -1) {
                    System.out.printf("Czas BogoSort dla n=%d: %.4f sekund (%,d przetasowań)\n", currentN, durationSecN, shufflesN);
                }

                if (durationSecN > 60.0 || shufflesN == -1) {
                    System.out.println("\n>>> Dla n = " + currentN + " czas wykonania BogoSort przekroczył 1 minutę (lub przerwano test).");
                    break;
                }
                if (currentN >= 11) { // BogoSort becomes infeasible very quickly
                    System.out.println("\n>>> Przerywam poszukiwanie n (osiągnięto n=11). BogoSort jest zbyt wolny.");
                    break;
                }
            }
        }
        System.out.println();
    }


    public static void main(String[] args) {
        // Task 1 & 2: Explanations
        explainArrayListAddAmortized();
        explainNextPermutationAmortized();

        // --- Task 3 ---
        int[] arr3 = {3, 76, 71, 5, 57, 12, 50, 20, 93, 20, 4, 62};
        insertionSortDescendingRight(Arrays.copyOf(arr3, arr3.length));

        // --- Task 4 ---
        int[] arr4 = {76, 71, 5, 57, 12, 50, 20, 3, 20, 55, 62, 53};
        selectionSortDescendingRight(Arrays.copyOf(arr4, arr4.length));

        // --- Task 5 ---
        int[] arr5 = {76, 20, 5, 57, 12, 50, 20, 93, 44, 55, 62, 3};
        bubbleSortDescendingLeft(Arrays.copyOf(arr5, arr5.length));

        // --- Task 6 & 7 ---
        int[] arr7 = {76, 71, 5, 57, 12, 50, 20, 93, 20, 55, 62, 3};
        System.out.println("Tablica dla ShakerSort: " + Arrays.toString(arr7));
        // shakerSortBasic(Arrays.copyOf(arr7, arr7.length)); // Task 6a - optional run
        shakerSortImproved(Arrays.copyOf(arr7, arr7.length)); // Task 6b + Task 7 trace

        // --- Task 8 ---
        // Użyj małej tablicy do demonstracji, a potem poszukaj n > 1 min
        int[] arr8_small = {3, 1, 4, 2};
        permutationSort(Arrays.copyOf(arr8_small, arr8_small.length)); // Includes finding n > 60s

        // --- Task 9 ---
        // Użyj małej tablicy do demonstracji, a potem poszukaj n > 1 min
        int[] arr9_small = {7, 1, 5, 2};
        bogoSort(Arrays.copyOf(arr9_small, arr9_small.length)); // Includes finding n > 60s

        System.out.println("--- Zakończono wszystkie zadania ---");
    }
}