import java.util.Collection;

class Drukarka {
    public static void drukuj(Collection<? extends Pojazd> kolekcja) {
        for (Pojazd p : kolekcja) {
            p.drukuj();
        }
    }
}
