#include "Individual.hpp"
#include "Constants.hpp"
#include <algorithm>

using namespace LcVRPContest;

Individual::Individual(int genotypeSize, int numGroups, mt19937& rng)
    : genotype(genotypeSize), fitness(INVALID_FITNESS_VALUE) {

    uniform_int_distribution<> dist(0, numGroups - 1);
    for (int & i : genotype) {
        i = dist(rng);
    }
}

Individual::Individual(int genotypeSize)
    : genotype(genotypeSize, 0), fitness(INVALID_FITNESS_VALUE) {}

double Individual::calculateFitness(const Evaluator& evaluator) {
    double result = evaluator.evaluate(genotype);

    if (result < 0.0) {
        fitness = INVALID_FITNESS_VALUE;
    } else {
        fitness = result;
    }

    return fitness;
}

void Individual::mutate(double mutationProbability, int numGroups, mt19937& rng) {
    uniform_real_distribution<> probabilityDist(0.0, 1.0);
    uniform_int_distribution<> groupDist(0, numGroups - 1);

    for (int & i : genotype) {
        if (probabilityDist(rng) < mutationProbability) {
            i = groupDist(rng);
        }
    }
}

pair<Individual, Individual> Individual::crossover(const Individual& other, double crossoverProbability, mt19937& rng) const {
    uniform_real_distribution<> probabilityDist(0.0, 1.0);

    if (probabilityDist(rng) >= crossoverProbability) {
        return make_pair(*this, other);
    }

    int size = static_cast<int>(genotype.size());
    if (size < 2) {
        return make_pair(*this, other);
    }

    uniform_int_distribution<int> cutDist(1, size - 1);
    int cutPoint = cutDist(rng);

    Individual child1(size);
    Individual child2(size);

    for (int i = 0; i < cutPoint; ++i) {
        child1.genotype[i] = genotype[i];
    }
    for (int i = cutPoint; i < size; ++i) {
        child1.genotype[i] = other.genotype[i];
    }

    for (int i = 0; i < cutPoint; ++i) {
        child2.genotype[i] = other.genotype[i];
    }
    for (int i = cutPoint; i < size; ++i) {
        child2.genotype[i] = genotype[i];
    }

    return make_pair(child1, child2);
}