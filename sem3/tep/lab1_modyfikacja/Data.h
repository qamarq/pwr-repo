#ifndef DATA_H
#define DATA_H

class Data {
    unsigned char* bytes;
    int length;

public:
    Data();
    Data(int length);
    Data(const Data& other);
    ~Data();

    bool set(unsigned char* data, int len);
    void print() const;
};

void allocate_data(Data*& data, int length);

#endif
