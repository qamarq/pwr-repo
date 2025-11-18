cmake_minimum_required(VERSION 4.0)
project(zad2)

set(CMAKE_CXX_STANDARD 98)

add_executable(zad2 main.cpp
        CNumber.h
        CNumber.cpp
        Base256Number.h
        Base256Number.cpp
)
