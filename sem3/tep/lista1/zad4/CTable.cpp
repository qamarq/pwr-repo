#include "CTable.h"
#include <iostream>
using namespace std;

CTable::CTable()
{
    s_name = S_DEFAULT_NAME;
    i_table_length = I_DEFAULT_TABLE_LENGTH;
    pi_table = new int[i_table_length];
    cout << "bezp: '" << s_name << "'" << endl;
}

CTable::CTable(string sName, int iTableLen)
{
    s_name = sName;
    if (iTableLen > 0)
    {
        i_table_length = iTableLen;
    }
    else
    {
        i_table_length = I_DEFAULT_TABLE_LENGTH;
    }
    pi_table = new int[i_table_length];
    cout << "parametr: '" << s_name << "'" << endl;
}

CTable::CTable(CTable &pcOther)
{
    s_name = pcOther.s_name + "_copy";
    i_table_length = pcOther.i_table_length;
    pi_table = new int[i_table_length];
    v_copy_table(pcOther.pi_table, pcOther.i_table_length);
    cout << "kopiuj: '" << s_name << "'" << endl;
}

CTable::~CTable()
{
    cout << "usuwam: '" << s_name << "'" << endl;
    delete[] pi_table;
}

void CTable::vSetName(string sName)
{
    s_name = sName;
}

bool CTable::bSetNewSize(int iTableLen)
{
    if (iTableLen <= 0)
    {
        return false;
    }
    
    delete[] pi_table;
    pi_table = new int[iTableLen];
    i_table_length = iTableLen;
    return true;
}

CTable *CTable::pcClone()
{
    return new CTable(*this);
}

void CTable::v_copy_table(int *piSource, int iSourceLength)
{
    for (int i = 0; i < iSourceLength && i < i_table_length; i++)
    {
        pi_table[i] = piSource[i];
    }
}

void v_mod_tab(CTable *pcTab, int iNewSize)
{
    pcTab->bSetNewSize(iNewSize);
}

void v_mod_tab(CTable cTab, int iNewSize)
{
    cTab.bSetNewSize(iNewSize);
}