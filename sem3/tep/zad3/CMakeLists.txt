cmake_minimum_required(VERSION 4.0)
project(zad3)

set(CMAKE_CXX_STANDARD 98)

add_executable(zad3 main.cpp
        constants.h
        Node.h
        Node.cpp
        Tree.h
        Tree.cpp
        Interface.h
        Interface.cpp)
