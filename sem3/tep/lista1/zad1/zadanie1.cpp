#include "zadanie1.h"
#include <iostream>
using namespace std;

void v_alloc_table_fill_34(int iSize)
{
    if (iSize <= 0)
    {
        cout << "Blad: rozmiar tablicy musi byc wiekszy od 0" << endl;
        return;
    }

    int *pi_table = new int[iSize];
    
    for (int i = 0; i < iSize; i++)
    {
        pi_table[i] = I_DEFAULT_FILL_VALUE;
    }
    
    cout << "Zawartosc tablicy o rozmiarze " << iSize << ":" << endl;
    for (int i = 0; i < iSize; i++)
    {
        cout << "pi_table[" << i << "] = " << pi_table[i] << endl;
    }
    
    delete[] pi_table;
}