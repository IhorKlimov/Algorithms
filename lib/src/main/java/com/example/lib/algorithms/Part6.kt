package com.example.lib.algorithms

import kotlin.math.sqrt

fun main() {
    for (num in sieve(100)) {
        println("Is prime: $num = ${isPrimitiveBetter(num)}")
    }
}

private fun isPrimeNaive(num: Int): Boolean {
    if (num < 2) return false
    for (i in 2..<num) {
        if (num % i == 0) return false
    }
    return true
}

private fun isPrimitiveBetter(num: Int): Boolean {
    if (num < 2) return false
    val to = sqrt(num.toDouble()).toInt()
    for (i in 2..to) {
        if (num % i == 0) return false
    }
    return true
}

private fun sieve(max: Int): List<Int> {
    val arr = BooleanArray(max + 1) { true }
    arr[0] = false
    arr[1] = false

    var prime = 2
    val limit = sqrt(max.toDouble()).toInt()
    while (prime <= limit) {
        clear(arr, prime)
        prime = nextPrime(arr, prime)
    }
    return arr.indices.filter { arr[it] }
}

private fun clear(arr: BooleanArray, prime: Int) {
    for (i in prime * prime..<arr.size step prime) {
        arr[i] = false
    }
}

private fun nextPrime(arr: BooleanArray, prime: Int): Int {
    var next = prime + 1
    while (next < arr.size && !arr[next]) {
        next++
    }
    return next
}

