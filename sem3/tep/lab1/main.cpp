#include "alloc_funcs.h"
#include "CTable.h"
#include "constants.h"
#include <iostream>

using namespace std;

void v_mod_tab(CTable *pc_tab, int new_size)
{
    if (!pc_tab) return;
    pc_tab->setNewSize(new_size);
}


void v_mod_tab(CTable c_tab, int new_size)
{
    c_tab.setNewSize(new_size);
}


int main()
{
    cout << "ZAD1 ============" << endl;
    alloc_table_fill_34(10);
    alloc_table_fill_34(-1);


    cout << "ZAD2 & ZAD3 =============" << endl;
    int **pi_table = 0;
    if (alloc_table_2_dim(&pi_table, TABLE_COLS, TABLE_ROWS))
    {
        for (int x = 0; x < TABLE_COLS; ++x)
        {
            for (int y = 0; y < TABLE_ROWS; ++y)
            {
                pi_table[x][y] = x * 10 + y;
            }
        }
        for (int x = 0; x < TABLE_COLS; ++x)
        {
            for (int y = 0; y < TABLE_ROWS; ++y)
            {
                cout << pi_table[x][y];
                if (y + 1 < TABLE_ROWS) cout << ", ";
            }
            cout << endl;
        }
        b_dealloc_table_2_dim(&pi_table, TABLE_COLS);
    }


    cout << "ZAD4 ===============" << endl;
    CTable c1;
    CTable c2("myTable", 3);
    CTable *pc3 = c2.tableClone();

    cout << "v_mod 1 ========" << endl;
    v_mod_tab(&c2, 6);
    cout << c2.getSize() << endl;
    cout << "v_mod 2 ========" << endl;
    v_mod_tab(c2, 2);
    cout << c2.getSize() << endl;
    cout << "v_mod end ===========" << endl;

    CTable *pc_dyn = new CTable("dyn", 4);
    delete pc_dyn;
    CTable *pc_arr = new CTable[2];
    delete [] pc_arr;
    delete pc3;

    return 0;
}