#include "Tree.h"
#include <sstream>
#include <iostream>
#include <cstdlib>

Tree::Tree() : root(NULL) {}

Tree::Tree(const Tree& other) : root(NULL) {
    if (other.root != NULL) {
        root = new Node(*other.root);
    }
}

Tree::~Tree() {
    delete root;
}

Tree& Tree::operator=(const Tree& other) {
    if (this != &other) {
        delete root;
        root = NULL;
        if (other.root != NULL) {
            root = new Node(*other.root);
        }
    }
    return *this;
}

Tree Tree::operator+(const Tree& other) const {
    Tree result(*this);
    result.join(other);
    return result;
}

bool Tree::operator==(const Tree& other) const {
    if (root == NULL && other.root == NULL) {
        return true;
    }

    if (root == NULL || other.root == NULL) {
        return false;
    }

    std::vector<std::string> vars1 = getVars();
    std::vector<std::string> vars2 = other.getVars();

    if (vars1.size() != vars2.size()) {
        return false;
    }

    std::map<std::string, std::string> mapping;
    return areNodesEqual(root, other.root, mapping);
}

bool Tree::areNodesEqual(const Node* node1, const Node* node2, std::map<std::string, std::string>& mapping) const {
    if (node1 == NULL && node2 == NULL) {
        return true;
    }

    if (node1 == NULL || node2 == NULL) {
        return false;
    }

    if (node1->getType() != node2->getType()) {
        return false;
    }

    if (node1->getType() == TYPE_OPERATOR) {
        if (node1->getValue() != node2->getValue()) {
            return false;
        }

        if (node1->getChildrenCount() != node2->getChildrenCount()) {
            return false;
        }

        std::vector<Node*> children1 = const_cast<Node*>(node1)->getChildren();
        std::vector<Node*> children2 = const_cast<Node*>(node2)->getChildren();

        for (size_t i = 0; i < children1.size(); ++i) {
            if (!areNodesEqual(children1[i], children2[i], mapping)) {
                return false;
            }
        }
        return true;
    }

    if (node1->getType() == TYPE_CONSTANT) {
        return node1->getValue() == node2->getValue();
    }

    if (node1->getType() == TYPE_VARIABLE) {
        return checkMapping(node1->getValue(), node2->getValue(), mapping);
    }

    return false;
}

bool Tree::checkMapping(const std::string& var1, const std::string& var2, std::map<std::string, std::string>& mapping) const {
    std::map<std::string, std::string>::iterator it = mapping.find(var1);

    if (it != mapping.end()) {
        return it->second == var2;
    }

    for (std::map<std::string, std::string>::iterator iter = mapping.begin(); iter != mapping.end(); ++iter) {
        if (iter->second == var2) {
            return false;
        }
    }

    mapping[var1] = var2;
    return true;
}

void Tree::enter(const std::string& formula) {
    delete root;
    root = NULL;

    std::stringstream ss(formula);
    std::string segment;
    std::vector<std::string> tokens;


    while (std::getline(ss, segment, ' ')) {
        if (!segment.empty()) {
            tokens.push_back(segment);
        }
    }

    if (tokens.empty()) return;

    try {
        root = parse(tokens);
    } catch (const std::invalid_argument& e) {
        std::cout << "Error: " << e.what() << std::endl;
        delete root;
        root = NULL;
    }
}

Node* Tree::parse(std::vector<std::string>& tokens) {
    if (tokens.empty()) {
        std::stringstream ss;
        ss << DEFAULT_VAL;
        return new Node(ss.str(), TYPE_CONSTANT);
    }

    std::string token = tokens[0];
    tokens.erase(tokens.begin());

    if (isOperator(token)) {
        Node* node = new Node(token, TYPE_OPERATOR);
        int arity = getArity(token);

        for (int i = 0; i < arity; ++i) {
            node->addChild(parse(tokens));
        }
        return node;
    }
    if (isNumber(token)) {
        return new Node(token, TYPE_CONSTANT);
    }
    if (!isValidVarName(token)) {
        throw std::invalid_argument("Invalid variable name or token: " + token);
    }
    return new Node(token, TYPE_VARIABLE);
}

bool Tree::isOperator(const std::string& token) {
    return token == STR_OPERATOR_ADD || token == STR_OPERATOR_SUB ||
           token == STR_OPERATOR_MUL || token == STR_OPERATOR_DIV ||
           token == STR_OPERATOR_SIN || token == STR_OPERATOR_COS;
}

int Tree::getArity(const std::string& op) {
    if (op == STR_OPERATOR_SIN || op == STR_OPERATOR_COS) return 1;
    return 2;
}

bool Tree::isNumber(const std::string& token) {
    for (size_t i = 0; i < token.length(); ++i) {
        if (!isdigit(token[i])) return false;
    }
    return true;
}

bool Tree::isValidVarName(const std::string& token) {
    if (token.empty()) return false;
    for (size_t i = 0; i < token.length(); ++i) {
        if (!isalnum(token[i])) return false;
    }
    return true;
}

std::string Tree::print() const {
    if (!root) return "";
    return root->toString();
}

std::vector<std::string> Tree::getVars() const {
    std::vector<std::string> vars;
    if (root) {
        root->getVars(vars);
    }
    return vars;
}

double Tree::compute(const std::vector<double>& values) const {
    if (!root) return 0.0;

    std::vector<std::string> vars = getVars();
    std::map<std::string, double> valueMap;

    for (size_t i = 0; i < vars.size() && i < values.size(); ++i) {
        valueMap[vars[i]] = values[i];
    }

    return root->calculate(valueMap);
}

void Tree::join(const Tree& other) {
    if (!root) {
        *this = other;
        return;
    }
    if (!other.root) return;

    bool replaced = false;
    if (root->isLeaf()) {
        delete root;
        root = new Node(*other.root);
    } else {
        std::vector<Node*>& children = root->getChildren();
        for (size_t i = 0; i < children.size(); ++i) {
            findAndReplaceLeaf(root, children[i], other.root, replaced);
            if (replaced) break;
        }
    }
}

int Tree::howManyVars() const {
    std::vector<std::string> vars;
    if (root) {
        root->getVars(vars);
    }
    return vars.size();
}

void Tree::findAndReplaceLeaf(Node* parent, Node*& current, const Node* otherRoot, bool& replaced) {
    if (replaced) return;

    if (current->isLeaf()) {
        delete current;
        current = new Node(*otherRoot);
        replaced = true;
    } else {
        std::vector<Node*>& children = current->getChildren();
        for (int i = children.size() - 1; i >= 0; --i) {
             findAndReplaceLeaf(current, children[i], otherRoot, replaced);
             if (replaced) return;
        }
    }
}

bool Tree::isInitialized() const {
    return root != NULL;
}