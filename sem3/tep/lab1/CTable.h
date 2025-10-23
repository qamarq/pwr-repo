#ifndef CTABLE_H
#define CTABLE_H
#include "constants.h"
#include <string>

using namespace std;

class CTable
{
public:
    CTable();
    CTable(string name, int table_len);
    CTable(const CTable &other_table);
    ~CTable();


    void setName(string name);
    bool setNewSize(int table_len);
    CTable *tableClone();


    int *getArray() const;
    int getSize() const;
    string getName() const;


private:
    string s_name;
    int *pi_table;
    int i_table_len;
};


#endif