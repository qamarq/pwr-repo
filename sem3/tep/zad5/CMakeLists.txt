cmake_minimum_required(VERSION 4.0)
project(zad5)

set(CMAKE_CXX_STANDARD 23)

add_executable(zad5 main.cpp
        Interface.cpp
        Interface.h
        Tree.cpp
        Tree.h
        Node.cpp
        Node.h
        RefCounter.h
        SmartPointer.h
        Error.h
        Result.h
        Constants.h
        Saver.h)
