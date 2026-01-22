#include "Evaluator.hpp"
#include "Constants.hpp"
#include <fstream>
#include <sstream>

using namespace std;
using namespace LcVRPContest;

void Evaluator::trim(string& s) {
    const int a = s.find_first_not_of(WHITESPACE);
    const int b = s.find_last_not_of(WHITESPACE);
    if (a == -1) s = "";
    else s = s.substr(a, b - a + 1);
}

double Evaluator::evaluate(const vector<int>& sol) const {
    if (sol.size() != data.GetNumCustomers())
        return INVALID_FITNESS_VALUE;

    vector<vector<int>> routes(groups);
    vector<int> perm = data.GetPermutation();

    // rozdzielamy klientow do grup wg permutacji
    for (int customerId : perm) {
        int idx = customerId - 2; // bo klienci od 2
        if (idx >= 0 && idx < sol.size()) {
            int g = sol[idx];
            if (g >= 0 && g < groups)
                routes[g].push_back(customerId);
        }
    }

    int depot = data.GetDepot() - 1;
    int capacity = data.GetCapacity();
    double maxDist = data.HasDistanceConstraint() ? data.GetDistance() : -1;
    vector<int> demands = data.GetDemands();

    double totalCost = 0.0;

    for (int g = 0; g < groups; g++) {
        int load = 0;
        int last = depot;
        double dist = 0.0;

        for (int i : routes[g]) {
            int cust = i - 1;
            int dem = demands[cust];

            if (load + dem > capacity) {
                double back = data.CalculateDistance(last, depot);
                if (back < 0) return INVALID_FITNESS_VALUE;
                dist += back;
                totalCost += dist;

                dist = 0;
                load = 0;
                last = depot;
            }

            double step = data.CalculateDistance(last, cust);
            if (step < 0) return INVALID_FITNESS_VALUE;

            if (maxDist > 0) {
                double back = data.CalculateDistance(cust, depot);
                if (back < 0) return INVALID_FITNESS_VALUE;

                if (dist + step + back > maxDist) {
                    double toDepot = data.CalculateDistance(last, depot);
                    if (toDepot < 0) return INVALID_FITNESS_VALUE;
                    dist += toDepot;
                    totalCost += dist;

                    dist = 0;
                    load = 0;
                    last = depot;

                    step = data.CalculateDistance(depot, cust);
                    if (step < 0) return INVALID_FITNESS_VALUE;
                }
            }

            dist += step;
            load += dem;
            last = cust;
        }

        // #powrut
        double back = data.CalculateDistance(last, depot);
        if (back < 0) return INVALID_FITNESS_VALUE;
        dist += back;
        totalCost += dist;
    }

    return totalCost;
}

bool Evaluator::loadInstance(const string& path) {
    ifstream f(path);
    if (!f) return false;

    string line;
    while (getline(f, line)) {
        trim(line);
        if (line == "") continue;

        if (line.find(HEADER_NAME) != string::npos) {
            data.SetName(line.substr(line.find(':') + 1));
        }
        else if (line.find(HEADER_DIMENSION) != string::npos) {
            data.SetDimension(stoi(line.substr(line.find(':') + 1)));
        }
        else if (line.find(HEADER_CAPACITY) != string::npos) {
            data.SetCapacity(stoi(line.substr(line.find(':') + 1)));
        }
        else if (line.find(HEADER_DISTANCE) != string::npos) {
            data.SetDistance(stod(line.substr(line.find(':') + 1)));
        }
        else if (line.find(HEADER_EDGE_WEIGHT_TYPE) != string::npos) {
            data.SetEdgeWeightType(line.substr(line.find(':') + 1));
        }

        else if (line.find(SECTION_NODE_COORD) != string::npos) {
            vector<Coordinate> c(data.GetDimension());
            for (int i = 0, id; i < data.GetDimension(); i++) {
                double x, y;
                f >> id >> x >> y;
                c[id - 1] = Coordinate(x, y);
            }
            data.SetCoordinates(c);
        }

        else if (line.find(SECTION_DEMAND) != string::npos) {
            vector<int> d(data.GetDimension());
            for (int i = 0, id, v; i < data.GetDimension(); i++) {
                f >> id >> v;
                d[id - 1] = v;
            }
            data.SetDemands(d);
        }

        else if (line.find(SECTION_DEPOT) != string::npos) {
            int d;
            f >> d;
            data.SetDepot(d);
        }

        else if (line.find(SECTION_PERMUTATION) != string::npos) {
            vector<int> p;
            string s = line.substr(line.find(':') + 1);
            stringstream ss(s);
            int x;
            while (ss >> x) p.push_back(x);
            data.SetPermutation(p);
        }
    }

    if (data.GetEdgeWeightType() == EDGE_TYPE_EUC_2D)
        data.BuildEdgeWeightMatrix();

    return true;
}
