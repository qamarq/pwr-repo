#include "Interface.h"
#include "Constants.h"
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
        else if (command == STR_CMD_ENTER) handleEnter(args);
        else if (command == STR_CMD_VARS) handleVars();
        else if (command == STR_CMD_PRINT) handlePrint();
        else if (command == STR_CMD_COMP) handleComp(args);
        else if (command == STR_CMD_JOIN) handleJoin(args);
        else if (command == STR_CMD_EQUALS) handleEquals(args);
        else std::cout << "Unknown command" << std::endl;
    }
}

void Interface::handleEnter(const std::string& args) {
    currentTree.enter(args);
    std::cout << "Formula: " << currentTree.print() << std::endl;
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

    double result = currentTree.compute(values);
    std::cout << "Result: " << result << std::endl;
}

void Interface::handleJoin(const std::string& args) {
    if (!currentTree.isInitialized()) {
        std::cout << "Tree not initialized" << std::endl;
        return;
    }

    Tree otherTree;
    otherTree.enter(args);

    currentTree = currentTree + otherTree;
    
    std::cout << "Formula: " << currentTree.print() << std::endl;
}

void Interface::handleEquals(const std::string& args) {
    if (!currentTree.isInitialized()) {
        std::cout << "Tree not initialized" << std::endl;
        return;
    }

    Tree otherTree;
    otherTree.enter(args);

    const bool isEquals = currentTree == otherTree;
    const std::string result = (isEquals) ? "true" : "false";

    std::cout << result << std::endl;
}