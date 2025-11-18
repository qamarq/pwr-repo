#ifndef CNUMBER_H
#define CNUMBER_H

#include <string>

#define NUMBER_DEFAULT_LENGTH 10

class CNumber {
public:
    CNumber();
    CNumber(const CNumber &pcOther);
    CNumber(int iVal);
    ~CNumber();

    CNumber &operator=(const CNumber &pcOther);
    CNumber &operator=(int iVal);

    CNumber operator+(const CNumber &pcOther) const;
    CNumber operator-(const CNumber &pcOther) const;
    CNumber operator*(const CNumber &pcOther) const;
    CNumber operator/(const CNumber &pcOther) const;
    CNumber operator%(const CNumber &pcOther) const;

    std::string toString() const;

private:
    int *pi_digits;
    int i_length;
    int i_size;
    int i_sign;

    void updateSize(int iNewCap);
    void normalize();
    void fromIntAbs(int iVal);
    static int compareAbs(const CNumber &a, const CNumber &b);
    static CNumber addAbs(const CNumber &a, const CNumber &b);
    static CNumber subAbs(const CNumber &a, const CNumber &b);
    static bool absGreaterOrEqual(const CNumber &a, const CNumber &b);
};

#endif
