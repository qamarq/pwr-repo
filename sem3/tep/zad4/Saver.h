#ifndef SAVER_H
#define SAVER_H

#include "Result.h"
#include "Tree.h"
#include "Error.h"
#include <fstream>
#include <string>
#include <vector>

class Saver {
public:
    template <typename T>
    static void save(const Result<T, Error>& result, const std::string& filename) {
        std::ofstream file(filename.c_str());
        if (!file.is_open()) return;

        if (!result.isSuccess()) {
            std::vector<Error*> errs = result.getErrors();
            for (size_t i = 0; i < errs.size(); ++i) {
                file << "Error: " << errs[i]->getMessage() << std::endl;
            }
        } else {
            file << "Operation successful (no output for this type)." << std::endl;
        }
        file.close();
    }

    static void save(const Result<Tree, Error>& result, const std::string& filename) {
        std::ofstream file(filename.c_str());
        if (!file.is_open()) return;

        if (result.isSuccess()) {
            file << result.getValue().print();
        } else {
            std::vector<Error*> errs = result.getErrors();
            for (size_t i = 0; i < errs.size(); ++i) {
                file << "Error: " << errs[i]->getMessage() << std::endl;
            }
        }
        file.close();
    }
};

#endif