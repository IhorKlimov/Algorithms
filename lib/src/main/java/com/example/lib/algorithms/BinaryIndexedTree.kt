package com.example.lib.algorithms

class BinaryIndexedTree(private val n: Int) {
    private val bit = IntArray(n + 1)

    fun update(index: Int, delta: Int) {
        var i = index
        while (i <= n) {
            bit[i] += delta
            i += i and -i
        }
    }

    fun query(index: Int): Int {
        var sum = 0
        var i = index

        while (i > 0) {
            sum += bit[i]
            i -= i and -i
        }

        return sum
    }
}