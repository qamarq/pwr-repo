#ifndef PRODUCT_H
#define PRODUCT_H

#include <string>
#include <iostream>

class Product {
public:
    Product(std::string n) : name(n) {
        std::cout << "product created: " << name << std::endl;
    }

    ~Product() {
        std::cout << "product deleted: " << name << std::endl;
    }

    std::string getName() const { return name; }

private:
    std::string name;
};

#endif