#include <iostream>

#include "AltLinkList.h"

int main() {
    AltLinkList<int, double> list(1);
    AltLinkList<int, double> list2(2.0);
    std::cout << list.isSingleElem() << std::endl; // true
    std::cout << list.getHead() << std::endl; // 1
    std::cout << (2.0 + list).isSingleElem() << std::endl; // false
    std::cout << (2.0 + list).getHead() << std::endl; // 2.0
    bool result = (2.0 + list).getTail() == list;
    bool result2 = (2.0 + list).getTail() == list2;
    std::cout << result << std::endl;
    std::cout << result2 << std::endl;
    std::cout << (2.0 + list).getTail().getHead() << std::endl; // 1
    std::cout << (3 + (2.0 + list)).getHead() << std::endl; // 3
    std::cout << (2.0 + list).getHead() << std::endl; // błąd kompilacji (powinien być dodany double zamiast int)
    list = 3 + (2.0 + list);
    list = (4.0 + (3 + (2.0 + list))).getTail();
    std::cout << list.isSingleElem() << std::endl;
    std::cout << list.getHead() << std::endl;
}
