#include "zadanie2.h"

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