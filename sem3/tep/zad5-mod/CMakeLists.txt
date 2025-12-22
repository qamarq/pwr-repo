cmake_minimum_required(VERSION 4.1)
project(zad5_mod)

set(CMAKE_CXX_STANDARD 20)

add_executable(zad5_mod main.cpp
        RefCounter.h
        MySmartPointer.h
        MyUniquePointer.h
        Product.h)
