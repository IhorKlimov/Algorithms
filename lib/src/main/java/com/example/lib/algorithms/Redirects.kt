package com.example.lib.algorithms

import kotlin.math.hypot


private fun findLast(locations: Array<Array<Int>>, redirects: Array<Int>): Array<Int> {
    if (locations.isEmpty() || redirects.isEmpty()) return arrayOf()

    val visited = Array(locations.size) { false }
    var currentPosition = 0

    for (redirect in redirects) {
        var nextPosition = -1
        var distance = Double.MAX_VALUE
        val currentServer = locations[currentPosition]
        visited[currentPosition] = true

        for (locationIndex in locations.indices) {
            if (locationIndex == currentPosition || visited[locationIndex]) continue

            val potentialPosition = locations[locationIndex]
            if (isInRightDirection(currentServer, potentialPosition, redirect)) {
                val d = hypot(
                    (potentialPosition[0] - currentServer[0]).toDouble(),
                    (potentialPosition[1] - currentServer[1]).toDouble()
                )
                if (d < distance) {
                    distance = d
                    nextPosition = locationIndex
                }
            }
        }

        currentPosition = nextPosition
    }

    return arrayOf(locations[currentPosition][0], locations[currentPosition][1])
}

private fun isInRightDirection(server1: Array<Int>, server2: Array<Int>, direction: Int): Boolean {
    return when (direction) {
        1 -> server2[0] >= server1[0] && server2[1] >= server1[1]
        2 -> server2[0] >= server1[0] && server2[1] < server1[1]
        3 -> server2[0] < server1[0] && server2[1] < server1[1]
        4 -> server2[0] < server1[0] && server2[1] >= server1[1]
        else -> false
    }
}

fun main() {
    val locations = arrayOf(
        arrayOf(3, 2),
        arrayOf(4, 5),
        arrayOf(6, 8),
        arrayOf(2, 10),
        arrayOf(1, 11),
        arrayOf(1, 2),
        arrayOf(9, 3),
    )
    val redirects = arrayOf(1, 4)

    println(findLast(locations, redirects).contentToString())
}