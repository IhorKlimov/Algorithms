package com.example.lib.algorithms

import kotlin.test.Test
import kotlin.test.assertEquals

class Part5Test {

    @Test
    fun nextNumber_shouldWorkCorrectly() {
        for (input in 0..100_000) {
            val result = nextNumber(input)
            val correctNext = findCorrectNextNumber(input)
            val correctPrev = findCorrectPrevNumber(input)

            assertEquals(correctPrev, result.first, "Failed for $input")
            assertEquals(correctNext, result.second, "Failed for $input")
        }
    }

    private fun findCorrectNextNumber(from: Int): Int {
        if (from == Int.MAX_VALUE) return from

        val numOfBits = countNumOfOnes(from)
        if (numOfBits == 0) return from

        var n = from + 1
        while (true) {
            if (countNumOfOnes(n) == numOfBits) return n
            n++
            if (n > Int.MAX_VALUE) return from
        }
    }

    private fun findCorrectPrevNumber(from: Int): Int {
        if (from <= 1) return from

        val numOfBits = countNumOfOnes(from)
        if (numOfBits == 0) return from

        var n = from - 1
        while (true) {
            if (countNumOfOnes(n) == numOfBits) return n
            n--
            if (n <= 0) return from
        }
    }
}

private fun countNumOfOnes(number: Int): Int {
    var result = 0

    var n = number

    while (n != 0) {
        n = n and (n - 1)
        result++
    }

    return result
}