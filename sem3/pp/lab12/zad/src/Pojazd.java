class Pojazd {
    protected String nazwa;

    public Pojazd(String nazwa) {
        this.nazwa = nazwa;
    }

    public void drukuj() {
        System.out.println("Pojazd: " + nazwa);
    }
}
