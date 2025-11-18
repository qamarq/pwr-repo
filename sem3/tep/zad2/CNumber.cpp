#include "CNumber.h"
#include <sstream>

CNumber::CNumber() {
    i_length = NUMBER_DEFAULT_LENGTH;
    pi_digits = new int[i_length];
    i_size = 0;
    i_sign = 1;
}

CNumber::CNumber(const CNumber &pcOther) {
    i_length = pcOther.i_length;
    pi_digits = new int[i_length];
    for (int i = 0; i < pcOther.i_size; ++i) pi_digits[i] = pcOther.pi_digits[i];
    i_size = pcOther.i_size;
    i_sign = pcOther.i_sign;
}

CNumber::CNumber(int iVal) {
    i_length = NUMBER_DEFAULT_LENGTH;
    pi_digits = new int[i_length];
    i_size = 0;
    i_sign = 1;
    *this = iVal;
}

CNumber::~CNumber() {
    delete [] pi_digits;
}

CNumber &CNumber::operator=(const CNumber &pcOther) {
    if (this == &pcOther) return *this;
    if (i_length < pcOther.i_size) {
        delete [] pi_digits;
        i_length = pcOther.i_size;
        pi_digits = new int[i_length];
    }
    for (int i = 0; i < pcOther.i_size; ++i) pi_digits[i] = pcOther.pi_digits[i];
    i_size = pcOther.i_size;
    i_sign = pcOther.i_sign;
    return *this;
}

CNumber &CNumber::operator=(int iVal) {
    if (iVal < 0) {
        i_sign = -1;
        fromIntAbs(-iVal);
    } else {
        i_sign = 1;
        fromIntAbs(iVal);
    }
    normalize();
    return *this;
}

void CNumber::fromIntAbs(int iVal) {
    i_size = 0;
    if (iVal == 0) {
        if (i_length < 1) updateSize(1);
        pi_digits[0] = 0;
        i_size = 1;
        return;
    }
    int tmp = iVal;
    while (tmp > 0) {
        if (i_size >= i_length) updateSize(i_length * 2);
        pi_digits[i_size++] = tmp % 10;
        tmp /= 10;
    }
}

void CNumber::updateSize(int iNewCap) {
    if (iNewCap <= i_length) return;
    int *pn = new int[iNewCap];
    for (int i = 0; i < i_size; ++i) pn[i] = pi_digits[i];
    delete [] pi_digits;
    pi_digits = pn;
    i_length = iNewCap;
}

void CNumber::normalize() {
    while (i_size > 1 && pi_digits[i_size - 1] == 0) --i_size;
    if (i_size == 1 && pi_digits[0] == 0) i_sign = 1;
}

int CNumber::compareAbs(const CNumber &a, const CNumber &b) {
    if (a.i_size > b.i_size) return 1;
    if (a.i_size < b.i_size) return -1;
    for (int i = a.i_size - 1; i >= 0; --i) {
        if (a.pi_digits[i] > b.pi_digits[i]) return 1;
        if (a.pi_digits[i] < b.pi_digits[i]) return -1;
    }
    return 0;
}

CNumber CNumber::addAbs(const CNumber &a, const CNumber &b) {
    CNumber out;
    int maxu = (a.i_size > b.i_size) ? a.i_size : b.i_size;
    out.updateSize(maxu + 1);
    int carry = 0;
    out.i_size = 0;
    for (int i = 0; i < maxu || carry; ++i) {
        int av = 0;
        int bv = 0;
        if (i < a.i_size) av = a.pi_digits[i];
        if (i < b.i_size) bv = b.pi_digits[i];
        int s = av + bv + carry;
        out.pi_digits[out.i_size++] = s % 10;
        carry = s / 10;
    }
    out.normalize();
    return out;
}

CNumber CNumber::subAbs(const CNumber &a, const CNumber &b) {
    CNumber out;
    out.updateSize(a.i_size);
    out.i_size = a.i_size;
    int borrow = 0;
    for (int i = 0; i < a.i_size; ++i) {
        int av = a.pi_digits[i];
        int bv = (i < b.i_size) ? b.pi_digits[i] : 0;
        int s = av - bv - borrow;
        if (s < 0) {
            s += 10;
            borrow = 1;
        } else borrow = 0;
        out.pi_digits[i] = s;
    }
    out.normalize();
    return out;
}

CNumber CNumber::operator+(const CNumber &pcOther) const {
    CNumber res;
    if (i_sign == pcOther.i_sign) {
        res = addAbs(*this, pcOther);
        res.i_sign = i_sign;
    } else {
        int cmp = compareAbs(*this, pcOther);
        if (cmp == 0) {
            res = 0;
        } else if (cmp > 0) {
            res = subAbs(*this, pcOther);
            res.i_sign = i_sign;
        } else {
            res = subAbs(pcOther, *this);
            res.i_sign = pcOther.i_sign;
        }
    }
    return res;
}

CNumber CNumber::operator-(const CNumber &pcOther) const {
    CNumber negOther = pcOther;
    negOther.i_sign = -negOther.i_sign;
    return *this + negOther;
}

CNumber CNumber::operator*(const CNumber &pcOther) const {
    if ((i_size == 1 && pi_digits[0] == 0) || (pcOther.i_size == 1 && pcOther.pi_digits[0] == 0)) return CNumber(0);
    CNumber out;
    out.updateSize(i_size + pcOther.i_size + 2);
    for (int i = 0; i < i_size + pcOther.i_size + 1; ++i) out.pi_digits[i] = 0;
    out.i_size = i_size + pcOther.i_size + 1;
    for (int i = 0; i < i_size; ++i) {
        int carry = 0;
        for (int j = 0; j < pcOther.i_size || carry; ++j) {
            int pj = (j < pcOther.i_size) ? pcOther.pi_digits[j] : 0;
            int cur = out.pi_digits[i + j] + pi_digits[i] * pj + carry;
            out.pi_digits[i + j] = cur % 10;
            carry = cur / 10;
        }
    }
    out.i_sign = i_sign * pcOther.i_sign;
    out.normalize();
    return out;
}

bool CNumber::absGreaterOrEqual(const CNumber &a, const CNumber &b) {
    if (a.i_size > b.i_size) return true;
    if (a.i_size < b.i_size) return false;

    for (int i = a.i_size - 1; i >= 0; --i) {
        if (a.pi_digits[i] > b.pi_digits[i]) return true;
        if (a.pi_digits[i] < b.pi_digits[i]) return false;
    }
    return true;
}

CNumber CNumber::operator/(const CNumber &pcOther) const {
    CNumber result = 0;

    CNumber a = *this;
    CNumber b = pcOther;

    if (b.i_size == 1 && b.pi_digits[0] == 0) {
        // powinno sie rzucac wyjatek
        return 0;
    }

    a.i_sign = 1;
    b.i_sign = 1;

    while (absGreaterOrEqual(a, b)) {
        CNumber temp = b;
        CNumber multiple = 1;

        while (absGreaterOrEqual(a, temp * 10)) {
            temp = temp * 10;
            multiple = multiple * 10;
        }

        a = a - temp;
        result = result + multiple;
    }

    if (this->i_sign != pcOther.i_sign)
        result.i_sign = -1;

    return result;
}

CNumber CNumber::operator%(const CNumber &pcOther) const {
    if (pcOther.i_size == 1 && pcOther.pi_digits[0] == 0) {
        // powinien byc wyjatek
        return 0;
    }

    CNumber out = *this - ((*this / pcOther) * pcOther);
    return out;
}



std::string CNumber::toString() const {
    std::ostringstream oss;
    if (i_sign < 0) oss << '-';
    for (int i = i_size - 1; i >= 0; --i) oss << pi_digits[i];
    return oss.str();
}
