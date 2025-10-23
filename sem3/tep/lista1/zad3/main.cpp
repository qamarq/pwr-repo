#include <iostream>
#include "zadanie3.h"

using namespace std;

// Kopiuje funkcje z zadania 2 do testowania zadania 3
bool b_alloc_table_2_dim(int ***piTable, int iSizeX, int iSizeY)
{
    if (iSizeX <= 0 || iSizeY <= 0)
    {
        return false;
    }
    
    *piTable = new int*[iSizeX];
    
    for (int i = 0; i < iSizeX; i++)
    {
        (*piTable)[i] = new int[iSizeY];
    }
    
    return true;
}

int main()
{
    cout << "=== ZADANIE 3 - Test funkcji b_dealloc_table_2_dim ===" << endl;
    
    int **pi_table;
    
    cout << "Test kompletnego cyklu alokacji i dealokacji tablicy 4x3:" << endl;
    
    // Alokacja
    if (b_alloc_table_2_dim(&pi_table, 4, 3))
    {
        cout << "Alokacja tablicy 4x3 udana!" << endl;
        
        // Wypelnienie tablicy
        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                pi_table[i][j] = (i + 1) * 10 + (j + 1);
            }
        }
        
        // Wyswietlenie zawartosci
        cout << "Zawartosc tablicy:" << endl;
        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                cout << pi_table[i][j] << "\t";
            }
            cout << endl;
        }
        
        // Dealokacja
        if (b_dealloc_table_2_dim(&pi_table, 4))
        {
            cout << "Dealokacja tablicy udana!" << endl;
            cout << "Wskaznik po dealokacji: " << pi_table << endl;
        }
        else
        {
            cout << "Blad dealokacji!" << endl;
        }
    }
    else
    {
        cout << "Blad alokacji!" << endl;
    }
    
    cout << endl << "Test dealokacji z nieprawidlowymi parametrami:" << endl;
    
    // Test z NULL pointer
    int **pi_null_table = 0;
    if (!b_dealloc_table_2_dim(&pi_null_table, 5))
    {
        cout << "Prawidlowo odrzucono NULL pointer" << endl;
    }
    
    // Test z nieprawidlowym rozmiarem
    if (b_alloc_table_2_dim(&pi_table, 2, 2))
    {
        if (!b_dealloc_table_2_dim(&pi_table, -1))
        {
            cout << "Prawidlowo odrzucono nieprawidlowy rozmiar" << endl;
        }
        // Zwolnienie pamieci normalnie
        b_dealloc_table_2_dim(&pi_table, 2);
    }
    
    cout << endl << "Test wielokrotnej dealokacji tego samego wskaznika:" << endl;
    if (b_alloc_table_2_dim(&pi_table, 3, 3))
    {
        cout << "Alokacja udana" << endl;
        
        if (b_dealloc_table_2_dim(&pi_table, 3))
        {
            cout << "Pierwsza dealokacja udana" << endl;
        }
        
        if (!b_dealloc_table_2_dim(&pi_table, 3))
        {
            cout << "Prawidlowo odrzucono probe ponownej dealokacji" << endl;
        }
    }
    
    return 0;
}