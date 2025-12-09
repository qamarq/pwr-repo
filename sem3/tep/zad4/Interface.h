#ifndef CINTERFACE_H
#define CINTERFACE_H

#include "Tree.h"

class Interface {
public:
    Interface();
    void run();

private:
    Tree currentTree;
    
    void handleEnter(const std::string& args);
    void handleVars();
    void handlePrint();
    void handleComp(const std::string& args);
    void handleJoin(const std::string& args);
    void handleEquals(const std::string& args);
    void handleSave(const std::string& args);
};

#endif