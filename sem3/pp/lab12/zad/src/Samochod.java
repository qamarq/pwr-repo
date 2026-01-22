class Samochod extends Pojazd {
    protected int drzwi;

    public Samochod(String nazwa, int drzwi) {
        super(nazwa);
        this.drzwi = drzwi;
    }

    @Override
    public void drukuj() {
        System.out.println("Samochod: " + nazwa + ", drzwi=" + drzwi);
    }
}
