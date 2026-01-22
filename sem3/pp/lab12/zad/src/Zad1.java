void main() {
    Pojazd[] tablica = new Samochod[3];

    tablica[0] = new Samochod("BMW", 4);
    tablica[1] = new ElektrycznySamochod("Tesla", 4, 75);
    tablica[2] = new Samochod("Audi", 5);

    for (Pojazd p : tablica) {
        p.drukuj();
    }
}