#include "Base256Number.h"
#include <iostream>
#include <sstream>
#include <iomanip>
#include <algorithm>
#include <cstring>

void Base256Number::removeLeadingZeros() {
    while (i_size > 1 && i_digits[0] == 0) {
        unsigned char* newi_digits = new unsigned char[i_size - 1];
        std::memcpy(newi_digits, i_digits + 1, i_size - 1);
        delete[] i_digits;
        i_digits = newi_digits;
        i_size--;
    }
}

Base256Number::Base256Number() : i_digits(0), i_size(1) {
    i_digits = new unsigned char[1];
    i_digits[0] = 0;
}

Base256Number::Base256Number(const Base256Number& other) : i_digits(0), i_size(other.i_size) {
    i_digits = new unsigned char[i_size];
    std::memcpy(i_digits, other.i_digits, i_size);
}

Base256Number::~Base256Number() {
    delete[] i_digits;
}

Base256Number& Base256Number::operator=(const Base256Number& other) {
    if (this != &other) {
        delete[] i_digits;
        i_size = other.i_size;
        i_digits = new unsigned char[i_size];
        std::memcpy(i_digits, other.i_digits, i_size);
    }
    return *this;
}

Base256Number& Base256Number::operator=(int value) {
    if (value < 0) value = 0;

    delete[] i_digits;

    if (value == 0) {
        i_size = 1;
        i_digits = new unsigned char[1];
        i_digits[0] = 0;
        return *this;
    }

    unsigned int temp = value;
    size_t count = 0;
    while (temp > 0) {
        count++;
        temp /= 256;
    }

    i_size = count;
    i_digits = new unsigned char[i_size];

    temp = value;
    for (int i = i_size - 1; i >= 0; i--) {
        i_digits[i] = temp % 256;
        temp /= 256;
    }

    return *this;
}

std::string Base256Number::toHexString() const {
    std::stringstream ss;
    ss << "0x";

    bool firstDigit = true;
    for (size_t i = 0; i < i_size; i++) {
        if (firstDigit && i_digits[i] < 16) {
            ss << std::hex << (int)i_digits[i];
        } else {
            ss << std::hex << std::setw(2) << std::setfill('0') << (int)i_digits[i];
        }
        firstDigit = false;
    }

    return ss.str();
}

Base256Number Base256Number::operator&(const Base256Number& other) const {
    Base256Number result;
    size_t maxSize = std::max(i_size, other.i_size);

    delete[] result.i_digits;
    result.i_digits = new unsigned char[maxSize];
    result.i_size = maxSize;

    std::memset(result.i_digits, 0, maxSize);

    size_t offset1 = maxSize - i_size;
    size_t offset2 = maxSize - other.i_size;

    for (size_t i = 0; i < maxSize; i++) {
        unsigned char d1 = (i >= offset1) ? i_digits[i - offset1] : 0;
        unsigned char d2 = (i >= offset2) ? other.i_digits[i - offset2] : 0;
        result.i_digits[i] = d1 & d2;
    }

    result.removeLeadingZeros();
    return result;
}

Base256Number Base256Number::operator|(const Base256Number& other) const {
    Base256Number result;
    size_t maxSize = std::max(i_size, other.i_size);

    delete[] result.i_digits;
    result.i_digits = new unsigned char[maxSize];
    result.i_size = maxSize;

    size_t offset1 = maxSize - i_size;
    size_t offset2 = maxSize - other.i_size;

    for (size_t i = 0; i < maxSize; i++) {
        unsigned char d1 = (i >= offset1) ? i_digits[i - offset1] : 0;
        unsigned char d2 = (i >= offset2) ? other.i_digits[i - offset2] : 0;
        result.i_digits[i] = d1 | d2;
    }

    result.removeLeadingZeros();
    return result;
}

Base256Number Base256Number::operator^(const Base256Number& other) const {
    Base256Number result;
    size_t maxSize = std::max(i_size, other.i_size);

    delete[] result.i_digits;
    result.i_digits = new unsigned char[maxSize];
    result.i_size = maxSize;

    size_t offset1 = maxSize - i_size;
    size_t offset2 = maxSize - other.i_size;

    for (size_t i = 0; i < maxSize; i++) {
        unsigned char d1 = (i >= offset1) ? i_digits[i - offset1] : 0;
        unsigned char d2 = (i >= offset2) ? other.i_digits[i - offset2] : 0;
        result.i_digits[i] = d1 ^ d2;
    }

    result.removeLeadingZeros();
    return result;
}

Base256Number Base256Number::operator~() const {
    int msb_byte = 0;

    int msb_bit = 7;
    const unsigned char val = i_digits[msb_byte];
    for (int bit = 7; bit >= 0; bit--) {
        if (val & (1 << bit)) {
            msb_bit = bit;
            break;
        }
    }

    const int highest_bit = (i_size - 1 - msb_byte) * 8 + msb_bit;

    Base256Number mask;
    mask = 1;
    for (int i = 0; i < highest_bit + 1; i++) {
        mask = mask * 2;
    }
    Base256Number temp;
    temp = 1;
    mask = mask - temp;

    return mask - *this;
}

Base256Number Base256Number::operator+(const Base256Number& other) const {
    Base256Number result;
    size_t maxSize = std::max(i_size, other.i_size);

    delete[] result.i_digits;
    result.i_digits = new unsigned char[maxSize + 1];
    result.i_size = maxSize + 1;
    std::memset(result.i_digits, 0, result.i_size);

    unsigned int carry = 0;
    for (size_t i = 0; i < maxSize || carry; ++i) {
        unsigned int a_val = 0;
        unsigned int b_val = 0;

        if (i < i_size) a_val = i_digits[i_size - 1 - i];
        if (i < other.i_size) b_val = other.i_digits[other.i_size - 1 - i];

        unsigned int sum = a_val + b_val + carry;
        result.i_digits[result.i_size - 1 - i] = sum % 256;
        carry = sum / 256;
    }

    result.removeLeadingZeros();
    return result;
}

Base256Number Base256Number::operator-(const Base256Number& other) const {
    Base256Number result;

    delete[] result.i_digits;
    result.i_digits = new unsigned char[i_size];
    result.i_size = i_size;
    std::memset(result.i_digits, 0, result.i_size);

    int borrow = 0;
    for (size_t i = 0; i < i_size; ++i) {
        int a_val = i_digits[i_size - 1 - i];
        int b_val = (i < other.i_size) ? other.i_digits[other.i_size - 1 - i] : 0;

        int diff = a_val - b_val - borrow;
        if (diff < 0) {
            diff += 256;
            borrow = 1;
        } else {
            borrow = 0;
        }
        result.i_digits[result.i_size - 1 - i] = diff;
    }

    result.removeLeadingZeros();
    return result;
}

Base256Number Base256Number::operator*(int value) const {
    if (value < 0) value = 0;
    if (value == 0) {
        Base256Number result;
        return result;
    }
    if (value == 1) {
        return *this;
    }

    Base256Number result;
    delete[] result.i_digits;

    result.i_size = i_size + 2;
    result.i_digits = new unsigned char[result.i_size];
    std::memset(result.i_digits, 0, result.i_size);

    unsigned int carry = 0;
    for (int i = i_size - 1; i >= 0; i--) {
        unsigned int temp = (unsigned int)i_digits[i] * value + carry;
        result.i_digits[i + 2] = temp % 256;
        carry = temp / 256;
    }

    result.i_digits[1] = carry % 256;
    result.i_digits[0] = carry / 256;

    result.removeLeadingZeros();
    return result;
}

Base256Number Base256Number::operator/(int value) const {
    if (value <= 0) {
        Base256Number result;
        return result;
    }
    if (value == 1) {
        return *this;
    }

    Base256Number result;
    delete[] result.i_digits;

    result.i_size = i_size;
    result.i_digits = new unsigned char[result.i_size];

    unsigned int remainder = 0;
    for (size_t i = 0; i < i_size; i++) {
        unsigned int current = remainder * 256 + i_digits[i];
        result.i_digits[i] = current / value;
        remainder = current % value;
    }

    result.removeLeadingZeros();
    return result;
}

Base256Number Base256Number::operator<<(int shift) const {
    Base256Number result(*this);
    for (int i = 0; i < shift; i++) {
        result = result * 2;
    }
    return result;
}

Base256Number Base256Number::operator>>(int shift) const {
    Base256Number result(*this);
    for (int i = 0; i < shift; i++) {
        result = result / 2;
    }
    return result;
}

void Base256Number::print() const {
    std::cout << "Base256: [";
    for (size_t i = 0; i < i_size; i++) {
        std::cout << (int)i_digits[i];
        if (i < i_size - 1) std::cout << ", ";
    }
    std::cout << "]" << std::endl;
}