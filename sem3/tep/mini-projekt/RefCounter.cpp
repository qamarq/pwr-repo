#include "RefCounter.hpp"

using namespace LcVRPContest;

RefCounter::RefCounter() : count(0) {}

void RefCounter::add() {
    count++;
}

int RefCounter::dec() {
    return --count;
}

int RefCounter::get() const {
    return count;
}