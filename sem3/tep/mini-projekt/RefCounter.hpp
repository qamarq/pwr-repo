#pragma once

namespace LcVRPContest {
    class RefCounter {
    public:
        RefCounter();
        void add();
        int dec();
        int get() const;

    private:
        int count;
    };
}