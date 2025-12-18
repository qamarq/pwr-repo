#ifndef SMARTPOINTER_H
#define SMARTPOINTER_H

#include "RefCounter.h"
#include <cstddef> // dla NULL

template <typename T>
class SmartPointer {
public:
    SmartPointer(T* ptr) {
        pointer = ptr;
        counter = new RefCounter();
        counter->add();
    }

    SmartPointer(const SmartPointer& other) {
        pointer = other.pointer;
        counter = other.counter;
        counter->add();
    }

    ~SmartPointer() {
        if (counter->dec() == 0) {
            delete pointer;
            delete counter;
        }
    }

    SmartPointer& operator=(const SmartPointer& other) {
        if (this != &other) {
            if (counter->dec() == 0) {
                delete pointer;
                delete counter;
            }

            pointer = other.pointer;
            counter = other.counter;
            counter->add();
        }
        return *this;
    }

    T& operator*() { return *pointer; }
    T* operator->() { return pointer; }

private:
    T* pointer;
    RefCounter* counter;
};

#endif