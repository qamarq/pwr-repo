#ifndef ALTLINKLIST_H
#define ALTLINKLIST_H

template <typename T1, typename T2>
class AltLinkList;

template <typename T1, typename T2>
AltLinkList<T2, T1> operator+(const T2& lhs, const AltLinkList<T1, T2>& rhs);

template <typename T1, typename T2>
class AltLinkList {
private:
    T1 head_;
    AltLinkList<T2, T1>* tail_;

public:
    explicit AltLinkList(const T1& val) : head_(val), tail_(NULL) {}

    AltLinkList(const AltLinkList& other) : head_(other.head_), tail_(NULL) {
        if (other.tail_) {
            tail_ = new AltLinkList<T2, T1>(*other.tail_);
        }
    }

    AltLinkList(const T1& val, const AltLinkList<T2, T1>& tailList) : head_(val) {
        tail_ = new AltLinkList<T2, T1>(tailList);
    }

    ~AltLinkList() {
        delete tail_;
    }

    AltLinkList& operator=(const AltLinkList& other) {
        if (this == &other) {
            return *this;
        }

        head_ = other.head_;

        delete tail_;
        tail_ = NULL;

        if (other.tail_) {
            tail_ = new AltLinkList<T2, T1>(*other.tail_);
        }

        return *this;
    }

    bool isSingleElem() const {
        return tail_ == NULL;
    }

    const T1& getHead() const {
        return head_;
    }

    const AltLinkList<T2, T1>& getTail() const {
        return *tail_;
    }

    bool operator==(const AltLinkList<T1, T2>& other) const {
        if (head_ != other.head_) return false;
        
        if (tail_ == NULL && other.tail_ != NULL) return false;
        if (tail_ != NULL && other.tail_ == NULL) return false;
        if (tail_ == NULL && other.tail_ == NULL) return true;

        return *tail_ == *other.tail_;
    }

    friend AltLinkList<T2, T1> operator+ <>(const T2& lhs, const AltLinkList<T1, T2>& rhs);
};

template <typename T1, typename T2>
AltLinkList<T2, T1> operator+(const T2& lhs, const AltLinkList<T1, T2>& rhs) {
    return AltLinkList<T2, T1>(lhs, rhs);
}

#endif