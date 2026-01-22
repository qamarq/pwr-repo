cmake_minimum_required(VERSION 4.1)
project(mini_projekt)

set(CMAKE_CXX_STANDARD 14)

include_directories(.)

add_executable(mini_projekt
        Evaluator.cpp
        Evaluator.hpp
        LcVRPContest.cpp
        Optimizer.cpp
        Optimizer.hpp
        ProblemData.cpp
        ProblemData.hpp
        ProblemLoader.cpp
        ProblemLoader.hpp
        Individual.hpp
        Individual.cpp
        GeneticAlgorithm.hpp
        GeneticAlgorithm.cpp
        RefCounter.cpp
        SmartPointer.hpp
        RefCounter.hpp
        Constants.hpp
)
