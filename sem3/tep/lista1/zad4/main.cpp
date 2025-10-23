#include <iostream>
#include "CTable.h"

using namespace std;

int main()
{
    cout << "=== ZADANIE 4 - Test klasy CTable ===" << endl;
    
    cout << "\n=== Test konstruktorow ===" << endl;
    
    cout << "\n1. Konstruktor bezparametrowy:" << endl;
    CTable c_table1;
    
    cout << "\n2. Konstruktor z parametrami:" << endl;
    CTable c_table2("testowa_tabela", 15);
    
    cout << "\n3. Konstruktor kopiujacy:" << endl;
    CTable c_table3(c_table2);
    
    cout << "\n=== Test metod ===" << endl;
    
    cout << "\n4. Test metody vSetName:" << endl;
    c_table1.vSetName("nowa_nazwa");
    cout << "Nazwa zmieniona na 'nowa_nazwa'" << endl;
    
    cout << "\n5. Test metody bSetNewSize - prawidlowy rozmiar:" << endl;
    if (c_table1.bSetNewSize(20))
    {
        cout << "Zmiana rozmiaru na 20 udana" << endl;
    }
    else
    {
        cout << "Blad zmiany rozmiaru" << endl;
    }
    
    cout << "\n6. Test metody bSetNewSize - nieprawidlowy rozmiar:" << endl;
    if (!c_table1.bSetNewSize(-5))
    {
        cout << "Prawidlowo odrzucono nieprawidlowy rozmiar (-5)" << endl;
    }
    
    if (!c_table1.bSetNewSize(0))
    {
        cout << "Prawidlowo odrzucono nieprawidlowy rozmiar (0)" << endl;
    }
    
    cout << "\n=== Test metody pcClone ===" << endl;
    cout << "\n7. Klonowanie obiektu:" << endl;
    CTable *pc_cloned = c_table2.pcClone();
    cout << "Obiekt sklonowany" << endl;
    
    cout << "\n=== Test funkcji v_mod_tab ===" << endl;
    
    cout << "\n8. Modyfikacja przez wskaznik (powinna zmienic oryginal):" << endl;
    cout << "Przed modyfikacja - wywolanie bSetNewSize(25) przez wskaznik" << endl;
    v_mod_tab(&c_table1, 25);
    cout << "Modyfikacja przez wskaznik zakonczona" << endl;
    
    cout << "\n9. Modyfikacja przez wartosc (nie powinna zmienic oryginalu):" << endl;
    cout << "Przed modyfikacja - wywolanie bSetNewSize(30) przez wartosc" << endl;
    v_mod_tab(c_table1, 30);
    cout << "Modyfikacja przez wartosc zakonczona" << endl;
    
    cout << "\n=== Test alokacji dynamicznej ===" << endl;
    
    cout << "\n10. Alokacja dynamiczna pojedynczego obiektu:" << endl;
    CTable *pc_dynamic = new CTable("dynamiczny", 8);
    
    cout << "\n11. Test klonowania dynamicznego obiektu:" << endl;
    CTable *pc_dynamic_clone = pc_dynamic->pcClone();
    
    cout << "\n=== Test tablicy obiektow ===" << endl;
    
    cout << "\n12. Alokacja tablicy obiektow:" << endl;
    CTable *pc_array = new CTable[3];
    cout << "Tablica 3 obiektow utworzona" << endl;
    
    cout << "\n=== Test usuwania obiektow ===" << endl;
    
    cout << "\n13. Usuwanie obiektow dynamicznych:" << endl;
    cout << "Usuwanie pc_cloned:" << endl;
    delete pc_cloned;
    
    cout << "Usuwanie pc_dynamic_clone:" << endl;
    delete pc_dynamic_clone;
    
    cout << "Usuwanie pc_dynamic:" << endl;
    delete pc_dynamic;
    
    cout << "Usuwanie tablicy obiektow:" << endl;
    delete[] pc_array;
    
    cout << "\n=== Dodatkowe testy ===" << endl;
    
    cout << "\n14. Test konstruktora z nieprawidlowym rozmiarem:" << endl;
    CTable c_table_invalid("invalid", -10);
    cout << "Konstruktor z nieprawidlowym rozmiarem wywolany" << endl;
    
    cout << "\n15. Test lancucha klonowania:" << endl;
    CTable c_original("original", 5);
    CTable *pc_clone1 = c_original.pcClone();
    CTable *pc_clone2 = pc_clone1->pcClone();
    
    cout << "Usuwanie klonow:" << endl;
    delete pc_clone2;
    delete pc_clone1;
    
    cout << "\n=== Koniec testow ===" << endl;
    cout << "Obiekty lokalne zostana automatycznie usuniete:" << endl;
    
    return 0;
}