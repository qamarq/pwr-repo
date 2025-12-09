#ifndef CTREE_H
#define CTREE_H

#include "Node.h"
#include "Result.h"
#include "Error.h"
#include <string>
#include <vector>
#include <map>

class Tree {
public:
    Tree();
    Tree(const Tree& other);
    ~Tree();

    Tree& operator=(const Tree& other);
    Tree operator+(const Tree& other) const;
    bool operator==(const Tree& other) const;

    static Result<Tree, Error> build(const std::string& formula);

    std::string print() const;
    std::vector<std::string> getVars() const;

    Result<double, Error> compute(const std::vector<double>& values) const;

    void join(const Tree& other);
    int howManyVars() const;
    int howManyChildren() const;
    bool isInitialized() const;

private:
    Node* root;

    Tree(Node* rootNode);

    static Result<Node*, Error> parse(std::vector<std::string>& tokens);

    static bool isOperator(const std::string& token);
    static int getArity(const std::string& op);
    static bool isNumber(const std::string& token);
    static bool isValidVarName(const std::string& token);

    bool areNodesEqual(const Node* node1, const Node* node2, std::map<std::string, std::string>& mapping) const;
    bool checkMapping(const std::string& var1, const std::string& var2, std::map<std::string, std::string>& mapping) const;

    void findAndReplaceLeaf(Node* parent, Node*& current, const Node* otherRoot, bool& replaced);
};

#endif