package com.example.lib.algorithms

import kotlin.collections.copyOfRange

private fun maxSumFixed(array: IntArray, k: Int): Int {
    var currentSum = 0
    var maxSum = 0
    var accumulated = 0

    for (i in array.indices) {
        currentSum += array[i]

        if (accumulated < k) {
            accumulated++
            if (accumulated == k) maxSum = currentSum
        } else {
            currentSum -= array[i - k]
            maxSum = currentSum.coerceAtLeast(maxSum)
        }
    }

    return maxSum
}

private fun sumDynamic(array: IntArray, target: Int): IntArray {
    if (array.isEmpty()) return intArrayOf()

    var sum = 0
    var start = 0
    var end = 0

    sum += array[end]

    while (end <= array.lastIndex && start <= end) {
        if (sum == target) return array.copyOfRange(start, end + 1)
        if (sum > target && start < end) sum -= array[start++]
        else sum += array[++end]
    }

    return intArrayOf()
}

fun main() {
//    val input = intArrayOf(5, 2, -1, 0, 3)
    val input = intArrayOf(5, 2, 1, 0, 3)
    val k = 3

//    println(maxSumFixed(input, k))
    println(sumDynamic(input, 4).contentToString())
}