#include "GeneticAlgorithm.hpp"
#include "Constants.hpp"
#include <iostream>
#include <iomanip>

using namespace LcVRPContest;

GeneticAlgorithm::GeneticAlgorithm(Evaluator& evaluator, int populationSize, double crossoverProbability, double mutationProbability)
    : evaluator(evaluator),
      populationSize(populationSize),
      crossoverProbability(crossoverProbability),
      mutationProbability(mutationProbability),
      numGroups(evaluator.getNumGroups()),
      genotypeSize(evaluator.getSolutionSize()),
      rng(random_device{}()) {
}

void GeneticAlgorithm::initialize() {
    population.clear();
    population.reserve(populationSize);

    for (int i = 0; i < populationSize; ++i) {
        population.push_back(SmartPointer<Individual>(new Individual(genotypeSize, numGroups, rng)));
    }

    evaluatePopulation();

    if (!population.empty()) {
        bestIndividual = population[0];
        updateBestIndividual();
    }
}

void GeneticAlgorithm::runIteration() {
    createNextGeneration();
    evaluatePopulation();
    updateBestIndividual();

    if (!bestIndividual.isNull()) {
        cout << "Current best fitness: " << fixed << setprecision(2) << bestIndividual->getFitness() << endl;
    }
}

void GeneticAlgorithm::evaluatePopulation() {
    for (auto & i : population) {
        if (!i.isNull()) {
            i->calculateFitness(evaluator);
        }
    }
}

SmartPointer<Individual> GeneticAlgorithm::tournamentSelection() {
    uniform_int_distribution<> dist(0, static_cast<int>(population.size()) - 1);

    int idx1 = dist(rng);
    int idx2 = dist(rng);

    SmartPointer<Individual> ind1 = population[idx1];
    SmartPointer<Individual> ind2 = population[idx2];

    if (ind1->getFitness() < ind2->getFitness()) {
        return ind1;
    }
    return ind2;
}

void GeneticAlgorithm::createNextGeneration() {
    vector< SmartPointer<Individual> > newPopulation;
    newPopulation.reserve(populationSize);

    while (newPopulation.size() < populationSize) {
        SmartPointer<Individual> parent1 = tournamentSelection();
        SmartPointer<Individual> parent2 = tournamentSelection();

        pair<Individual, Individual> childrenValues = parent1->crossover(*parent2, crossoverProbability, rng);

        childrenValues.first.mutate(mutationProbability, numGroups, rng);
        childrenValues.second.mutate(mutationProbability, numGroups, rng);

        newPopulation.push_back(SmartPointer<Individual>(new Individual(childrenValues.first)));

        if (newPopulation.size() < populationSize) {
            newPopulation.push_back(SmartPointer<Individual>(new Individual(childrenValues.second)));
        }
    }

    population = newPopulation;
}

void GeneticAlgorithm::updateBestIndividual() {
    for (auto & i : population) {
        if (bestIndividual.isNull() || i->getFitness() < bestIndividual->getFitness()) {
            bestIndividual = i;
        }
    }
}

vector<int> GeneticAlgorithm::getBestSolution() const {
    if (!bestIndividual.isNull()) {
        return bestIndividual->getGenotype();
    }
    return {};
}

double GeneticAlgorithm::getBestFitness() const {
    if (!bestIndividual.isNull()) {
        return bestIndividual->getFitness();
    }
    return INVALID_FITNESS_VALUE;
}