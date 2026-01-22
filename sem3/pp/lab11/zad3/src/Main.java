void main() {
    System.out.println("==========");
    Samochod s1 = new Samochod("Toyota", "Corolla", 2020, 75000);
    Debug.fields(s1);
    System.out.println("==========");

    System.out.println("==========");
    Samochod s2 = new Samochod("BMW", "X5", 2018, 150000);
    Debug.fields(s2);
    System.out.println("==========");

    System.out.println("==========");
    Samochod s3 = new Samochod("Audi", "A4", 2019, 120000);
    Debug.fields(s3);
    System.out.println("==========");

    System.out.println("==========");
    Samochod s4 = new Samochod("Skoda", "Octavia", 2015, 45000);
    Debug.fields(s4);
    System.out.println("==========");
}