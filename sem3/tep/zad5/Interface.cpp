#include "Interface.h"
#include "Constants.h"
#include "Result.h"
#include "Saver.h"
#include "SmartPointer.h" // Include dla testu
#include <iostream>
#include <sstream>
#include <vector>
#include <cstdlib>
#include <utility> // std::move

Interface::Interface() {}

void Interface::run() {
    std::string line;
    std::cout << "Commands: enter <formula>, vars, print, comp <vals>, join <formula>, equals <formula>, save <file>, test, exit" << std::endl;
    
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
        else if (command == STR_CMD_TEST) handleTest();
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

void Interface::handleTest() {
    std::cout << "--- SmartPointer Test ---" << std::endl;
    int* val = new int(10);
    SmartPointer<int> sp1(val);
    std::cout << "SP1 value: " << *sp1 << std::endl;
    
    {
        SmartPointer<int> sp2 = sp1; // Copy constructor, ref count increases
        std::cout << "SP2 created from SP1. Value: " << *sp2 << std::endl;
        *sp2 = 20;
        std::cout << "Changed SP2 value to 20. SP1 value: " << *sp1 << " (should be 20)" << std::endl;
    } // SP2 destroyed, ref count decreases, memory NOT deleted

    std::cout << "SP2 out of scope. SP1 value: " << *sp1 << " (still valid)" << std::endl;

    std::cout << "\n--- Move Semantics Test (Tree) ---" << std::endl;
    Tree::resetCounters();
    
    Result<Tree, Error> r1 = Tree::build("+ + x 1 2");
    Result<Tree, Error> r2 = Tree::build("- y 5");
    
    if (r1.isSuccess() && r2.isSuccess()) {
        Tree t1 = r1.getValue();
        Tree t2 = r2.getValue();
        
        std::cout << "Resetting counters..." << std::endl;
        Tree::resetCounters();
        
        std::cout << "Executing: t3 = t1 + t2" << std::endl;
        Tree t3 = t1 + t2;
        
        std::cout << "Copies: " << Tree::getCopyCount() << std::endl;
        std::cout << "Moves: " << Tree::getMoveCount() << std::endl;
        
        std::cout << "Executing: t3 = std::move(t1)" << std::endl;
        t3 = std::move(t1);
        
        std::cout << "Copies: " << Tree::getCopyCount() << std::endl;
        std::cout << "Moves: " << Tree::getMoveCount() << std::endl;
    }
}