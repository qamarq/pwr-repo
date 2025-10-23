#include "Data.h"
#include <iostream>

Data::Data() : bytes(0), length(0) {}

Data::Data(int length) : bytes(0), length(length) {
    bytes = new unsigned char[length];
    for (int i = 0; i < length; ++i)
        bytes[i] = 0;
}

Data::Data(const Data& other) : bytes(0), length(other.length) {
    if (other.bytes) {
        bytes = new unsigned char[length];
        for (int i = 0; i < length; ++i)
            bytes[i] = other.bytes[i];
    }
}

Data::~Data() {
    delete[] bytes;
}

bool Data::set(unsigned char* data, int len) {
    if (!bytes || len > length)
        return false;

    for (int i = 0; i < len; ++i)
        bytes[i] = data[i];
    for (int i = len; i < length; ++i)
        bytes[i] = 0;

    return true;
}

void Data::print() const {
    for (int i = 0; i < length; ++i)
        std::cout << (int)bytes[i] << " ";
    std::cout << std::endl;
}

void allocate_data(Data*& data, int length) {
    data = new Data(length);
}
