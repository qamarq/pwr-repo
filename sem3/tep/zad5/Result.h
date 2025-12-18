#ifndef RESULT_H
#define RESULT_H

#include <vector>
#include <cstddef>
#include "Error.h"

template <typename T, typename E>
class Result {
public:
    Result() : value(NULL) {}

    Result(const T& val) : value(new T(val)) {}

    Result(E* err) : value(NULL) {
        errors.push_back(err);
    }

    Result(const std::vector<E*>& errs) : value(NULL) {
        copyErrors(errs);
    }

    Result(const Result& other) {
        if (other.value) {
            value = new T(*other.value);
        } else {
            value = NULL;
        }
        copyErrors(other.errors);
    }

    ~Result() {
        delete value;
        deleteErrors();
    }

    Result& operator=(const Result& other) {
        if (this != &other) {
            delete value;
            deleteErrors();
            errors.clear();

            if (other.value) {
                value = new T(*other.value);
            } else {
                value = NULL;
            }
            copyErrors(other.errors);
        }
        return *this;
    }

    static Result ok(const T& val) {
        return Result(val);
    }

    static Result fail(E* err) {
        return Result(err);
    }

    static Result fail(const std::vector<E*>& errs) {
        return Result(errs);
    }

    bool isSuccess() const {
        return value != NULL && errors.empty();
    }

    T getValue() const {
        if (value) return *value;
        return T();
    }

    std::vector<E*> getErrors() const {
        return errors;
    }

private:
    T* value;
    std::vector<E*> errors;

    void copyErrors(const std::vector<E*>& source) {
        for (size_t i = 0; i < source.size(); ++i) {
            errors.push_back(new E(*source[i]));
        }
    }

    void deleteErrors() {
        for (size_t i = 0; i < errors.size(); ++i) {
            delete errors[i];
        }
    }
};

template <typename E>
class Result<void, E> {
public:
    Result() {}

    Result(E* err) {
        errors.push_back(err);
    }

    Result(const std::vector<E*>& errs) {
        copyErrors(errs);
    }

    Result(const Result& other) {
        copyErrors(other.errors);
    }

    ~Result() {
        deleteErrors();
    }

    Result& operator=(const Result& other) {
        if (this != &other) {
            deleteErrors();
            errors.clear();
            copyErrors(other.errors);
        }
        return *this;
    }

    static Result ok() {
        return Result();
    }

    static Result fail(E* err) {
        return Result(err);
    }

    static Result fail(const std::vector<E*>& errs) {
        return Result(errs);
    }

    bool isSuccess() const {
        return errors.empty();
    }

    std::vector<E*> getErrors() const {
        return errors;
    }

private:
    std::vector<E*> errors;

    void copyErrors(const std::vector<E*>& source) {
        for (size_t i = 0; i < source.size(); ++i) {
            errors.push_back(new E(*source[i]));
        }
    }

    void deleteErrors() {
        for (size_t i = 0; i < errors.size(); ++i) {
            delete errors[i];
        }
    }
};

#endif