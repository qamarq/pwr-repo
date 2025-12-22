#ifndef MYUNIQUEPOINTER_H
#define MYUNIQUEPOINTER_H

#include <cstddef>

template <typename T>
class MyUniquePointer {
public:
    MyUniquePointer() : pointer(NULL) {}
    MyUniquePointer(T* ptr) : pointer(ptr) {}

    ~MyUniquePointer() {
        delete pointer;
    }

    MyUniquePointer(const MyUniquePointer&) = delete;
    MyUniquePointer& operator=(const MyUniquePointer&) = delete;

    MyUniquePointer(MyUniquePointer&& other) : pointer(other.pointer) {
        other.pointer = NULL;
    }

    MyUniquePointer& operator=(MyUniquePointer&& other) {
        if (this != &other) {
            delete pointer;
            pointer = other.pointer;
            other.pointer = NULL;
        }
        return *this;
    }

    bool isEmpty() const {
        return pointer == NULL;
    }

    T* get() const { return pointer; }
    T& operator*() const { return *pointer; }
    T* operator->() const { return pointer; }

private:
    T* pointer;
};

#endif