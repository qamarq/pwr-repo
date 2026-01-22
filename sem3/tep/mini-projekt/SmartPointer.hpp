#ifndef SMARTPOINTER_H
#define SMARTPOINTER_H

#include "RefCounter.hpp"

namespace LcVRPContest {

    template <typename T>
    class SmartPointer {
    public:
        SmartPointer(T* ptr = 0) {
            pointer = ptr;
            if (ptr) {
                counter = new RefCounter();
                counter->add();
            } else {
                counter = 0;
            }
        }

        SmartPointer(const SmartPointer& other) {
            pointer = other.pointer;
            counter = other.counter;
            if (counter) {
                counter->add();
            }
        }

        ~SmartPointer() {
            if (counter) {
                if (counter->dec() == 0) {
                    delete pointer;
                    delete counter;
                }
            }
        }

        SmartPointer& operator=(const SmartPointer& other) {
            if (this != &other) {
                if (counter) {
                    if (counter->dec() == 0) {
                        delete pointer;
                        delete counter;
                    }
                }

                pointer = other.pointer;
                counter = other.counter;
                if (counter) {
                    counter->add();
                }
            }
            return *this;
        }

        T& operator*() { return *pointer; }
        T* operator->() { return pointer; }

        const T& operator*() const { return *pointer; }
        const T* operator->() const { return pointer; }

        bool isNull() const { return pointer == 0; }

    private:
        T* pointer;
        RefCounter* counter;
    };
}

#endif