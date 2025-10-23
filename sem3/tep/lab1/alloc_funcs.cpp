#include "alloc_funcs.h"

using namespace std;

void alloc_table_fill_34(int size)
{
    if (size <= 0)
    {
        cout << "v_alloc_table_fill_34: niepoprawny rozmiar: " << size << endl;
        return;
    }
    int *tab = new int[size];
    for (int i = 0; i < size; ++i)
    {
        tab[i] = FILL_VALUE_34;
    }
    for (int i = 0; i < size; ++i)
    {
        cout << tab[i];
        if (i + 1 < size) cout << ", ";
    }
    cout << endl;
    delete [] tab;
}


bool alloc_table_2_dim(int ***tab, int cols, int rows)
{
    if (!tab) return false;
    if (cols <= 0 || rows <= 0) return false;
    int **p = new int*[cols];
    for (int i = 0; i < cols; ++i)
    {
        p[i] = new int[rows];
    }
    *tab = p;
    return true;
}


bool b_dealloc_table_2_dim(int ***tab, int cols)
{
    if (!tab || !*tab) return false;
    if (cols <= 0) return false;
    int **p = *tab;
    for (int i = 0; i < cols; ++i)
    {
        delete [] p[i];
    }
    delete [] p;
    *tab = 0;
    return true;
}