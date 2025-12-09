#include "Interface.h"
#include "Constants.h"
#include "Result.h"
#include "Saver.h"
#include <iostream>
#include <sstream>
#include <vector>
#include <cstdlib>

Interface::Interface() {}

void Interface::run() {
    std::string line;
    while (true) {
        std::cout << "> ";
        if (!std::getline(std::cin, line)) break;

        std::stringstream ss(line);
        std::string command;
        ss >> command;

        std::string args;
        std::string temp;
        while (ss >> temp) {
            if (!args.empty()) args += SPACE;
            args += temp;
        }

        if (command == STR_CMD_EXIT) break;
        if (command == STR_CMD_ENTER) handleEnter(args);
        else if (command == STR_CMD_VARS) handleVars();
        else if (command == STR_CMD_PRINT) handlePrint();
        else if (command == STR_CMD_COMP) handleComp(args);
        else if (command == STR_CMD_JOIN) handleJoin(args);
        else if (command == STR_CMD_EQUALS) handleEquals(args);
        else if (command == STR_CMD_SAVE) handleSave(args);
        else std::cout << "Unknown command" << std::endl;
    }
}

void Interface::handleEnter(const std::string& args) {
    Result<Tree, Error> res = Tree::build(args);

    if (res.isSuccess()) {
        currentTree = res.getValue();
        std::cout << "Formula: " << currentTree.print() << std::endl;
    } else {
        std::vector<Error*> errors = res.getErrors();
        std::cout << "Errors found:" << std::endl;
        for (size_t i = 0; i < errors.size(); ++i) {
            std::cout << errors[i]->getMessage() << std::endl;
        }
    }
}

void Interface::handleVars() {
    std::vector<std::string> vars = currentTree.getVars();
    for (size_t i = 0; i < vars.size(); ++i) {
        std::cout << vars[i] << " ";
    }
    std::cout << std::endl;
}

void Interface::handlePrint() {
    std::cout << currentTree.print() << std::endl;
}

void Interface::handleComp(const std::string& args) {
    if (!currentTree.isInitialized()) {
        std::cout << "Tree not initialized" << std::endl;
        return;
    }

    std::vector<std::string> vars = currentTree.getVars();
    std::vector<double> values;

    std::stringstream ss(args);
    double val;
    while (ss >> val) {
        values.push_back(val);
    }

    if (values.size() != vars.size()) {
        std::cout << "Invalid number of values. Expected: " << vars.size() << std::endl;
        return;
    }

    Result<double, Error> result = currentTree.compute(values);

    if (result.isSuccess()) {
        std::cout << "Result: " << result.getValue() << std::endl;
    } else {
        std::cout << "Computation error:" << std::endl;
        std::vector<Error*> errors = result.getErrors();
        for (size_t i = 0; i < errors.size(); ++i) {
            std::cout << errors[i]->getMessage() << std::endl;
        }
    }
}

void Interface::handleJoin(const std::string& args) {
    if (!currentTree.isInitialized()) {
        std::cout << "Tree not initialized" << std::endl;
        return;
    }

    Result<Tree, Error> res = Tree::build(args);

    if (res.isSuccess()) {
        currentTree = currentTree + res.getValue();
        std::cout << "Formula: " << currentTree.print() << std::endl;
    } else {
        std::cout << "Error parsing formula to join:" << std::endl;
        std::vector<Error*> errors = res.getErrors();
        for (size_t i = 0; i < errors.size(); ++i) {
            std::cout << errors[i]->getMessage() << std::endl;
        }
    }
}

void Interface::handleEquals(const std::string& args) {
    if (!currentTree.isInitialized()) {
        std::cout << "Tree not initialized" << std::endl;
        return;
    }

    Result<Tree, Error> res = Tree::build(args);

    if (res.isSuccess()) {
        const bool isEquals = currentTree == res.getValue();
        const std::string result = (isEquals) ? "true" : "false";
        std::cout << result << std::endl;
    } else {
        std::cout << "Error parsing formula for comparison." << std::endl;
    }
}

void Interface::handleSave(const std::string& args) {
    if (args.empty()) {
        std::cout << "Usage: save <filename>" << std::endl;
        return;
    }

    if (currentTree.isInitialized()) {
        Result<Tree, Error> res = Result<Tree, Error>::ok(currentTree);
        Saver::save(res, args);
        std::cout << "Tree saved to " << args << std::endl;
    } else {
        Result<Tree, Error> res = Result<Tree, Error>::fail(new Error("Tree not initialized"));
        Saver::save(res, args);
        std::cout << "Error state saved to " << args << std::endl;
    }
}