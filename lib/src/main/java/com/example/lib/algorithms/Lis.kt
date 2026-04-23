package com.example.lib.algorithms

// Longest increasing subsequence

private fun lis(array: IntArray): Int {
    var max = 0
    val cache = IntArray(array.size)
    for (i in array.lastIndex downTo 0) {
        max = max.coerceAtLeast(lis(array, cache, i))
    }
    return max
}

private fun lis(array: IntArray, cache: IntArray, index: Int): Int {
    if (cache[index] != 0) return cache[index]
    else if (index == 0) {
        cache[index] = 1
        return cache[index]
    }

    val value = array[index]
    var maxLength = 0
    for (i in index - 1 downTo 0) {
        if (array[i] < value) {
            maxLength = maxLength.coerceAtLeast(lis(array, cache, i))
        }
    }

    cache[index] = maxLength + 1
    return cache[index]
}

fun main() {
    val input = intArrayOf(3, 1, 8, 2, 5)
    val result = lis(input)
    println(result)
}