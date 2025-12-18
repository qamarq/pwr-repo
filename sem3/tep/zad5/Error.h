#ifndef ERROR_H
#define ERROR_H

#include <string>

class Error {
public:
    Error( const std::string& msg) : message(msg) {}
    std::string getMessage() const { return message ;}

private:
    std::string message;
};

#endif