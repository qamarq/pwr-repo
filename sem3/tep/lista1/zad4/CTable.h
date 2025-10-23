#ifndef CTABLE_H
#define CTABLE_H

#include <string>
using namespace std;

const string S_DEFAULT_NAME = "def";
const int I_DEFAULT_TABLE_LENGTH = 10;

class CTable
{
public:
    CTable();
    CTable(string sName, int iTableLen);
    CTable(CTable &pcOther);
    ~CTable();
    
    void vSetName(string sName);
    bool bSetNewSize(int iTableLen);
    CTable *pcClone();

private:
    string s_name;
    int *pi_table;
    int i_table_length;
    
    void v_copy_table(int *piSource, int iSourceLength);
};

void v_mod_tab(CTable *pcTab, int iNewSize);
void v_mod_tab(CTable cTab, int iNewSize);

#endif // CTABLE_H