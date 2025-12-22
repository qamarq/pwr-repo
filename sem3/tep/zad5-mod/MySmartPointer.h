#ifndef MYSMARTPOINTER_H
#define MYSMARTPOINTER_H

#include "RefCounter.h"
#include <cstddef>

template <typename T>
class MySmartPointer {
public:
    MySmartPointer(T* ptr) {
        pointer = ptr;
        counter = new RefCounter();
        counter->add();
    }

    MySmartPointer(const MySmartPointer& other) {
        pointer = other.pointer;
        counter = other.counter;
        counter->add();
    }

    ~MySmartPointer() {
        if (counter->dec() == 0) {
            delete pointer;
            delete counter;
        }
    }

    MySmartPointer& operator=(const MySmartPointer& other) {
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

    int getCounter() const {
        return counter->get();
    }

    void releasePayload() {
        pointer = NULL;
    }

private:
    T* pointer;
    RefCounter* counter;
};

#endif