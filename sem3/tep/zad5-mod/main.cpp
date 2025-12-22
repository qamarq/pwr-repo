#include <iostream>
#include <map>
#include <vector>

#include "MySmartPointer.h"
#include "MyUniquePointer.h"
#include "Product.h"

std::vector<MyUniquePointer<Product>> konwersja(std::vector<MySmartPointer<Product>>& list) {
    std::vector<MyUniquePointer<Product>> result;
    std::map<Product*, std::vector<MySmartPointer<Product>*>> mapa;

    for (size_t i = 0; i < list.size(); i++) {
        Product* rawPtr = list[i].operator->();
        mapa[rawPtr].push_back(&list[i]);
    }

    for (auto it = mapa.begin(); it != mapa.end(); ++it) {
        Product* prod = it->first;
        std::vector<MySmartPointer<Product>*>& secondProduct = it->second;

        if (!secondProduct.empty()) {
            int totalRefCount = secondProduct[0]->getCounter();
            int inVector = secondProduct.size();

            if (totalRefCount == inVector) {
                result.push_back(MyUniquePointer<Product>(prod));
                for (auto & k : secondProduct) {
                    k->releasePayload();
                }
            } else {
                std::cout << "Skip '" << prod->getName() << "' due to external reference." << std::endl;
            }
        }
    }

    list.clear();
    return result;
}

int main() {
    std::vector<MySmartPointer<Product>> lista;

    Product* p1 = new Product("Rower");
    Product* p2 = new Product("Pilka");
    Product* p3 = new Product("Mistyczne szachy");
    Product* p4 = new Product("Klocki");
    Product* p5 = new Product("Zabugione mandarynki");

    lista.emplace_back(p1);

    {
        MySmartPointer temp(p2);
        lista.push_back(temp);
        lista.push_back(temp);
    }

    MySmartPointer sp3(p3);
    lista.push_back(sp3);

    lista.emplace_back(p4);

    MySmartPointer sp5(p5);
    lista.push_back(sp5);

    std::vector<MyUniquePointer<Product>> packed = konwersja(lista);

    std::cout << "size konwersja: " << packed.size() << std::endl;
    for (const auto & i : packed) {
        if (!i.isEmpty()) {
            std::cout << "- " << i->getName() << std::endl;
        }
    }

    std::cout << "\nclean:" << std::endl;
    return 0;
}