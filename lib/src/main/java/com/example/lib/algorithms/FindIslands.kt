package com.example.lib.algorithms

private fun findIslands(matrix: Array<Array<Char>>): Int {
    if (matrix.isEmpty()) return 0

    var numOfIslands = 0
    val visited = Array(matrix.size) { Array(matrix[0].size) { false } }

    for (row in 0..<matrix.size) {
        for (col in 0..<matrix[0].size) {
            if (!visited[row][col]) {
                if (bfs(matrix, visited, row, col)) numOfIslands++
            }
        }
    }

    return numOfIslands
}

private fun bfs(
    matrix: Array<Array<Char>>,
    visited: Array<Array<Boolean>>,
    row: Int,
    col: Int
): Boolean {
    var foundIsland = false

    val queue = ArrayDeque<Pair<Int, Int>>()
    queue.addFirst(Pair(row, col))

    val matrixWidth = matrix[0].size
    val matrixHeight = matrix.size

    while (queue.isNotEmpty()) {
        val cell = queue.removeFirst()
        val isLand = matrix[cell.first][cell.second] == 'L'

        if (isLand) foundIsland = true
        visited[cell.first][cell.second] = true

        if (isLand) {
            for (r in -1..1) {
                for (c in -1..1) {
                    val r = (cell.first + r).coerceIn(0, matrixHeight - 1)
                    val c = (cell.second + c).coerceIn(0, matrixWidth - 1)
                    if (!visited[r][c]) queue.addLast(Pair(r, c))
                }
            }
        }
    }

    return foundIsland
}

fun main() {

    val input = arrayOf(
        arrayOf('L', 'L', 'W', 'W', 'W'),
        arrayOf('W', 'L', 'W', 'W', 'L'),
        arrayOf('L', 'W', 'W', 'L', 'L'),
        arrayOf('W', 'W', 'W', 'W', 'W'),
        arrayOf('L', 'W', 'L', 'L', 'W'),
    )

    println(findIslands(input))
}

class Graph2<T : Comparable<T>> {
    val nodes: MutableList<Node<T>>? = null

    class Node<T>(val value: T, children: MutableList<Node<T>>)
}