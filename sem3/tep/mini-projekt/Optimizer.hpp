#pragma once

#include "Evaluator.hpp"
#include "GeneticAlgorithm.hpp"
#include <vector>

using namespace std;

namespace LcVRPContest {
	class Optimizer {
	public:
		Optimizer(Evaluator& evaluator);

		void Initialize();
		void RunIteration();

		vector<int>* GetCurrentBest();

	private:
		GeneticAlgorithm ga;
		vector<int> currentBestSolutionCache;
	};
}