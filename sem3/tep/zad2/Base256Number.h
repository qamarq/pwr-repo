#ifndef BASE256NUMBER_H
#define BASE256NUMBER_H

#include <string>
#include <cstddef>

class Base256Number {
private:
    unsigned char* i_digits;
    size_t i_size;

    void removeLeadingZeros();

public:
    Base256Number();
    Base256Number(const Base256Number& other);
    ~Base256Number();

    Base256Number& operator=(const Base256Number& other);
    Base256Number& operator=(int value);

    std::string toHexString() const;

    Base256Number operator&(const Base256Number& other) const;
    Base256Number operator|(const Base256Number& other) const;
    Base256Number operator^(const Base256Number& other) const;
    Base256Number operator~() const;
    Base256Number operator<<(int shift) const;
    Base256Number operator>>(int shift) const;

    Base256Number operator+(const Base256Number& other) const;
    Base256Number operator-(const Base256Number& other) const;
    Base256Number operator*(int value) const;
    Base256Number operator/(int value) const;

    void print() const;
};

#endif