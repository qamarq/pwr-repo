#include <iostream>
#include "zadanie1.h"

using namespace std;

int main()
{
    cout << "=== ZADANIE 1 - Test funkcji v_alloc_table_fill_34 ===" << endl;
    
    cout << "Test z prawidlowym rozmiarem (5):" << endl;
    v_alloc_table_fill_34(5);
    
    cout << endl << "Test z prawidlowym rozmiarem (3):" << endl;
    v_alloc_table_fill_34(3);
    
    cout << endl << "Test z nieprawidlowym rozmiarem (-1):" << endl;
    v_alloc_table_fill_34(-1);
    
    cout << endl << "Test z rozmiarem 0:" << endl;
    v_alloc_table_fill_34(0);
    
    cout << endl << "Test z wiekszym rozmiarem (10):" << endl;
    v_alloc_table_fill_34(10);
    
    return 0;
}