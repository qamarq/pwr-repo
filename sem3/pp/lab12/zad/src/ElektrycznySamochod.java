class ElektrycznySamochod extends Samochod {
    private int pojemnoscBaterii;

    public ElektrycznySamochod(String nazwa, int drzwi, int pojemnoscBaterii) {
        super(nazwa, drzwi);
        this.pojemnoscBaterii = pojemnoscBaterii;
    }

    @Override
    public void drukuj() {
        System.out.println("ElektrycznySamochod: " + nazwa +
                ", drzwi=" + drzwi +
                ", bateria=" + pojemnoscBaterii + "kWh");
    }
}
