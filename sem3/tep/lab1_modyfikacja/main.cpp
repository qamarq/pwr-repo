#include "Data.h"
#include <iostream>

using namespace std;

int main() {
    Data* data = 0;
    allocate_data(data, 10);

    unsigned char arr[5] = {1, 2, 3, 4, 5};
    if (data->set(arr, 5)) {
        data->print();
    } else {
        cout << "dane sie nie zmiesily" << endl;
    }

    delete data;
    return 0;
}
