cmake_minimum_required(VERSION 4.0)
project(lab1)

set(CMAKE_CXX_STANDARD 98)

add_executable(lab1 main.cpp
        constants.h
        alloc_funcs.h
        alloc_funcs.cpp
        CTable.h
        CTable.cpp
)
