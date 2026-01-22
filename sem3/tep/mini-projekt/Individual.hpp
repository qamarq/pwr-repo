#pragma once

#include "Evaluator.hpp"
#include <vector>
#include <random>
#include <utility>

using namespace std;

namespace LcVRPContest {

    class Individual {
    public:
        Individual(int genotypeSize, int numGroups, mt19937& rng);
        Individual(int genotypeSize);

        double calculateFitness(const Evaluator& evaluator);
        void mutate(double mutationProbability, int numGroups, mt19937& rng);
        pair<Individual, Individual> crossover(const Individual& other, double crossoverProbability, mt19937& rng) const;

        const vector<int>& getGenotype() const { return genotype; }
        double getFitness() const { return fitness; }

    private:
        vector<int> genotype;
        double fitness;
    };
}