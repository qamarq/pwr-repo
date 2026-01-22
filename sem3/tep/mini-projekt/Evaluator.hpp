#pragma once
#include "ProblemData.hpp"
#include <vector>
#include <string>

namespace LcVRPContest {

	class Evaluator {
	public:
		Evaluator() {}
		Evaluator(const ProblemData& d, int g) : data(d), groups(g) {}

		double evaluate(const std::vector<int>& solution) const;
		bool loadInstance(const std::string& path);

		int getSolutionSize() const { return data.GetNumCustomers(); }
		int getNumGroups() const { return groups; }
		const ProblemData& getProblemData() const { return data; }

	private:
		ProblemData data;
		int groups = 0;

		void trim(std::string& s);
	};

}
