import java.util.*;

void main() {
    Collection<Pojazd> c1 = Arrays.asList(
            new Pojazd("Rower"),
            new Samochod("Mazda", 4),
            new ElektrycznySamochod("BYD", 5, 40)
    );

    Collection<Samochod> c2 = Arrays.asList(
            new Samochod("Fiat", 3),
            new ElektrycznySamochod("Tesla", 4, 90)
    );

    Collection<ElektrycznySamochod> c3 = Arrays.asList(
            new ElektrycznySamochod("Mercedes", 4, 100),
            new ElektrycznySamochod("BYD", 5, 40)
    );

    Drukarka.drukuj(c1);
    Drukarka.drukuj(c2);
    Drukarka.drukuj(c3);
}