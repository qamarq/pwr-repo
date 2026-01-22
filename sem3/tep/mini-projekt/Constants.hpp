#pragma once

#include <string>

namespace LcVRPContest {

    const double INVALID_FITNESS_VALUE = 1.79769e+308;

    const int DEFAULT_POPULATION_SIZE = 100;
    const double DEFAULT_CROSSOVER_PROB = 0.6;
    const double DEFAULT_MUTATION_PROB = 0.01;

    const std::string HEADER_NAME = "NAME";
    const std::string HEADER_DIMENSION = "DIMENSION";
    const std::string HEADER_CAPACITY = "CAPACITY";
    const std::string HEADER_DISTANCE = "DISTANCE";
    const std::string HEADER_EDGE_WEIGHT_TYPE = "EDGE_WEIGHT_TYPE";
    const std::string SECTION_NODE_COORD = "NODE_COORD_SECTION";
    const std::string SECTION_EDGE_WEIGHT = "EDGE_WEIGHT_SECTION";
    const std::string SECTION_DEMAND = "DEMAND_SECTION";
    const std::string SECTION_DEPOT = "DEPOT_SECTION";
    const std::string SECTION_PERMUTATION = "PERMUTATION";
    const std::string EDGE_TYPE_EUC_2D = "EUC_2D";
    const std::string WHITESPACE = " \t\r\n";
}