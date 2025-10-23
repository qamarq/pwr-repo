#include "CTable.h"
#include <iostream>

using namespace std;

CTable::CTable()
{
    s_name = DEFAULT_NAME;
    i_table_len = DEFAULT_TABLE_LEN;
    pi_table = new int[i_table_len];

    for (int i = 0; i < i_table_len; ++i) pi_table[i] = 0;
    cout << "bezp: '" << s_name << "'" << endl;
}


CTable::CTable(string name, int table_len)
{
    s_name = name;
    if (table_len <= 0) i_table_len = DEFAULT_TABLE_LEN; else i_table_len = table_len;
    pi_table = new int[i_table_len];

    for (int i = 0; i < i_table_len; ++i) pi_table[i] = 0;
    cout << "parametr: '" << s_name << "'" << endl;
}


CTable::CTable(const CTable &other_table)
{
    s_name = other_table.s_name + "_copy";
    i_table_len = other_table.i_table_len;
    pi_table = new int[i_table_len];

    if (other_table.pi_table)
    {
        for (int i = 0; i < i_table_len; ++i) pi_table[i] = other_table.pi_table[i];
    }
    cout << "kopiuj: '" << s_name << "'" << endl;
}


CTable::~CTable()
{
    cout << "usuwam: '" << s_name << "'" << endl;
    delete [] pi_table;
}


void CTable::setName(string name)
{
    s_name = name;
}


bool CTable::setNewSize(int table_len)
{
    if (table_len <= 0) return false;
    int *new_pointer = new int[table_len];
    int to_copy = (table_len < i_table_len) ? table_len : i_table_len;

    for (int i = 0; i < to_copy; ++i) new_pointer[i] = pi_table[i];
    for (int i = to_copy; i < table_len; ++i) new_pointer[i] = 0;

    delete [] pi_table;
    pi_table = new_pointer;
    i_table_len = table_len;
    return true;
}


CTable *CTable::tableClone()
{
    CTable *new_pointer = new CTable(*this);
    return new_pointer;
}


int *CTable::getArray() const { return pi_table; }
int CTable::getSize() const { return i_table_len; }
string CTable::getName() const { return s_name; }