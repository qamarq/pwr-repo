#pragma once

#include "Evaluator.hpp"
#include "Individual.hpp"
#include "SmartPointer.hpp"
#include <vector>
#include <random>

using namespace std;

namespace LcVRPContest {

    class GeneticAlgorithm {
    public:
        GeneticAlgorithm(Evaluator& evaluator, int populationSize, double crossoverProbability, double mutationProbability);

        void initialize();
        void runIteration();

        vector<int> getBestSolution() const;
        double getBestFitness() const;

    private:
        Evaluator& evaluator;
        int populationSize;
        double crossoverProbability;
        double mutationProbability;
        int numGroups;
        int genotypeSize;

        vector< SmartPointer<Individual> > population;
        SmartPointer<Individual> bestIndividual;

        mt19937 rng;

        void evaluatePopulation();
        SmartPointer<Individual> tournamentSelection();
        void updateBestIndividual();
        void createNextGeneration();
    };
}