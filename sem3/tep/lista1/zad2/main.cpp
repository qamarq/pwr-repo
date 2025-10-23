#include <iostream>
#include "zadanie2.h"

using namespace std;

int main()
{
    cout << "=== ZADANIE 2 - Test funkcji b_alloc_table_2_dim ===" << endl;
    
    int **pi_table;
    
    cout << "Test alokacji tablicy 3x4:" << endl;
    if (b_alloc_table_2_dim(&pi_table, 3, 4))
    {
        cout << "Alokacja tablicy 3x4 udana!" << endl;
        
        // Wypelnienie tablicy testowymi wartosciami
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                pi_table[i][j] = i * 4 + j + 1;
            }
        }
        
        // Wyswietlenie zawartosci tablicy
        cout << "Zawartosc tablicy:" << endl;
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                cout << pi_table[i][j] << "\t";
            }
            cout << endl;
        }
        
        // Zwolnienie pamieci (normanie powinno byc w zadaniu 3)
        for (int i = 0; i < 3; i++)
        {
            delete[] pi_table[i];
        }
        delete[] pi_table;
        cout << "Pamiec zwolniona" << endl;
    }
    else
    {
        cout << "Blad alokacji tablicy 3x4!" << endl;
    }
    
    cout << endl << "Test z nieprawidlowymi parametrami (-1, 5):" << endl;
    if (!b_alloc_table_2_dim(&pi_table, -1, 5))
    {
        cout << "Prawidlowo odrzucono nieprawidlowe parametry" << endl;
    }
    
    cout << endl << "Test z nieprawidlowymi parametrami (5, 0):" << endl;
    if (!b_alloc_table_2_dim(&pi_table, 5, 0))
    {
        cout << "Prawidlowo odrzucono nieprawidlowe parametry" << endl;
    }
    
    cout << endl << "Test alokacji tablicy 2x6:" << endl;
    if (b_alloc_table_2_dim(&pi_table, 2, 6))
    {
        cout << "Alokacja tablicy 2x6 udana!" << endl;
        
        // Zwolnienie pamieci
        for (int i = 0; i < 2; i++)
        {
            delete[] pi_table[i];
        }
        delete[] pi_table;
        cout << "Pamiec zwolniona" << endl;
    }
    
    return 0;
}