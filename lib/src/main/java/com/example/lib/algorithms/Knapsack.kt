package com.example.lib.algorithms

import kotlin.math.max

var i = 0

internal fun knapsackRec(
    values: IntArray,
    weights: IntArray,
    index: Int,
    maxWeight: Int,
    memo: Array<IntArray>
): Int {
    if (maxWeight == 0 || index < 0) return 0
    if (memo[index][maxWeight] != -1) return memo[index][maxWeight]
    i++

    var max = 0
    if (weights[index] <= maxWeight) {
        max = values[index] + knapsackRec(
            values,
            weights,
            index - 1,
            maxWeight - weights[index],
            memo
        )
    }
    max = max(max, knapsackRec(values, weights, index - 1, maxWeight, memo))
    memo[index][maxWeight] = max
    return max
}

private fun knapsackIterative(
    values: IntArray,
    weights: IntArray,
    maxWeight: Int
): Int {
    val memo = Array(values.size + 1) { IntArray(maxWeight + 1) { 0 } }

    for (v in 0..values.size) {
        for (w in 0..maxWeight) {
            if (v == 0 || w == 0) {
                memo[v][w] = 0
            } else {
                var pick = 0
                i++

                if (weights[v - 1] <= w) {
                    pick = values[v - 1] + memo[v - 1][w - weights[v - 1]]
                }
                val notPick = memo[v - 1][w]

                memo[v][w] = max(pick, notPick)
            }
        }
    }

    return memo[values.size][maxWeight]
}

private fun knapsackCanonical(
    values: IntArray,
    weights: IntArray,
    maxWeight: Int
): Int {
    val memo = IntArray(maxWeight + 1) { 0 }

    for (v in values.indices) {
        for (w in maxWeight downTo weights[v]) {
            memo[w] = max(
                memo[w],
                memo[w - weights[v]] + values[v]
            )
            println("saved ${memo[w]} for v $v and w $w using prev: ${w - weights[v]} -> ${memo.contentToString()}")
        }
    }

    return memo[maxWeight]
}

private fun unboundedKnapsack(
    values: IntArray,
    weights: IntArray,
    index: Int,
    capacity: Int,
    memo: Array<IntArray>
): Int {
    if (index == values.size) return 0
    if (memo[index][capacity] != -1) return memo[index][capacity]

    i++
    val take = if (weights[index] <= capacity) {
        values[index] + unboundedKnapsack(values, weights, index, capacity - weights[index], memo)
    } else {
        0
    }
    val notTake = unboundedKnapsack(values, weights, index + 1, capacity, memo)

    memo[index][capacity] = max(take, notTake)
    return memo[index][capacity]
}

private fun unboundedKnapsackCanonical(
    values: IntArray,
    weights: IntArray,
    capacity: Int
): Int {
    val memo = IntArray(capacity + 1) { 0 }

    for (v in values.indices) {
        for (c in 1..capacity) {
            i++
            val take = if (weights[v] <= c) values[v] + memo[c - weights[v]] else 0
            memo[c] = max(memo[c], take)
        }
    }

    return memo[capacity]
}