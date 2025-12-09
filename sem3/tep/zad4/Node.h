#ifndef CNODE_H
#define CNODE_H

#include <string>
#include <vector>
#include <map>
#include "Constants.h"
#include "Result.h"
#include "Error.h"

enum NodeType {
    TYPE_OPERATOR,
    TYPE_CONSTANT,
    TYPE_VARIABLE
};

class Node {
public:
    Node(const std::string& value, NodeType type);
    Node(const Node& other);
    ~Node();

    Node& operator=(const Node& other);

    Result<double, Error> calculate(const std::map<std::string, double>& values) const;

    std::string toString() const;
    void getVars(std::vector<std::string>& accumulator) const;

    void addChild(Node* child);
    int getChildrenCount() const;
    bool isLeaf() const;

    NodeType getType() const;
    std::string getValue() const;
    std::vector<Node*>& getChildren();

private:
    void clearChildren();

    std::string value;
    NodeType type;
    std::vector<Node*> children;
};

#endif