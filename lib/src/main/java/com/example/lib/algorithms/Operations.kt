package com.example.lib.algorithms

import kotlin.math.hypot

fun main() {
    val v2 = arrayOf(-5.0, -5.0)
    val v1 = arrayOf(1.0, 1.0)

    println(hypot(v2[0] - v1[0], v2[1] - v1[1]))
}