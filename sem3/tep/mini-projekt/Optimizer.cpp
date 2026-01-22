#include "Optimizer.hpp"
#include "Constants.hpp"

using namespace LcVRPContest;

Optimizer::Optimizer(Evaluator& evaluator)
	: ga(evaluator, DEFAULT_POPULATION_SIZE, DEFAULT_CROSSOVER_PROB, DEFAULT_MUTATION_PROB) {
}

void Optimizer::Initialize() {
	ga.initialize();
}

void Optimizer::RunIteration() {
	ga.runIteration();
}

vector<int>* Optimizer::GetCurrentBest() {
	currentBestSolutionCache = ga.getBestSolution();
	return &currentBestSolutionCache;
}