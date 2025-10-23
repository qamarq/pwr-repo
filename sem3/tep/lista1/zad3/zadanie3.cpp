#include "zadanie3.h"

bool b_dealloc_table_2_dim(int ***piTable, int iSizeX)
{
    if (*piTable == 0 || iSizeX <= 0)
    {
        return false;
    }
    
    for (int i = 0; i < iSizeX; i++)
    {
        delete[] (*piTable)[i];
    }
    
    delete[] *piTable;
    *piTable = 0;
    
    return true;
}