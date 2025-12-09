#include "Node.h"
#include <cmath>
#include <cstdlib>
#include <sstream>

Node::Node(const std::string& val, NodeType t) : value(val), type(t) {}

Node::Node(const Node& other) : value(other.value), type(other.type) {
    for (size_t i = 0; i < other.children.size(); ++i) {
        children.push_back(new Node(*other.children[i]));
    }
}

Node::~Node() {
    clearChildren();
}

Node& Node::operator=(const Node& other) {
    if (this != &other) {
        clearChildren();
        value = other.value;
        type = other.type;
        for (size_t i = 0; i < other.children.size(); ++i) {
            children.push_back(new Node(*other.children[i]));
        }
    }
    return *this;
}

void Node::clearChildren() {
    for (size_t i = 0; i < children.size(); ++i) {
        delete children[i];
    }
    children.clear();
}

void Node::addChild(Node* child) {
    children.push_back(child);
}

int Node::getChildrenCount() const {
    return children.size();
}

bool Node::isLeaf() const {
    return children.empty();
}

NodeType Node::getType() const {
    return type;
}

std::string Node::getValue() const {
    return value;
}

std::vector<Node*>& Node::getChildren() {
    return children;
}

Result<double, Error> Node::calculate(const std::map<std::string, double>& values) const {
    if (type == TYPE_CONSTANT) {
        return std::atof(value.c_str());
    }

    if (type == TYPE_VARIABLE) {
        std::map<std::string, double>::const_iterator it = values.find(value);
        if (it != values.end()) {
            return it->second;
        }
        return new Error("Variable not found: " + value);
    }

    if (type == TYPE_OPERATOR) {
        std::vector<double> childResults;
        for (size_t i = 0; i < children.size(); ++i) {
            Result<double, Error> res = children[i]->calculate(values);
            if (!res.isSuccess()) {
                return res.getErrors();
            }
            childResults.push_back(res.getValue());
        }

        if (value == STR_OPERATOR_ADD) return childResults[0] + childResults[1];
        if (value == STR_OPERATOR_SUB) return childResults[0] - childResults[1];
        if (value == STR_OPERATOR_MUL) return childResults[0] * childResults[1];

        if (value == STR_OPERATOR_DIV) {
            if (childResults[1] == 0) {
                return new Error("Division by zero");
            }
            return childResults[0] / childResults[1];
        }

        if (value == STR_OPERATOR_SIN) return std::sin(childResults[0]);
        if (value == STR_OPERATOR_COS) return std::cos(childResults[0]);
    }
    return 0.0;
}

std::string Node::toString() const {
    std::string result = value;
    for (size_t i = 0; i < children.size(); ++i) {
        result += SPACE + children[i]->toString();
    }
    return result;
}

void Node::getVars(std::vector<std::string>& accumulator) const {
    if (type == TYPE_VARIABLE) {
        bool exists = false;
        for (size_t i = 0; i < accumulator.size(); ++i) {
            if (accumulator[i] == value) {
                exists = true;
                break;
            }
        }
        if (!exists) accumulator.push_back(value);
    }
    for (size_t i = 0; i < children.size(); ++i) {
        children[i]->getVars(accumulator);
    }
}