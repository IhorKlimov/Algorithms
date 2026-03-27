package com.example.lib.algorithms

private fun kahnTopologicalSort(input: Array<Array<Int>>): Array<Int> {
    if (input.isEmpty()) return arrayOf()

    val result = Array(input.size) { -1 }
    val dependencies = Array(input.size) { 0 }

    for (children in input) {
        for (child in children) {
            dependencies[child]++
        }
    }

    val queue = ArrayDeque<Int>()
    for (node in dependencies.indices) {
        if (dependencies[node] == 0) queue.addLast(node)
    }

    var currentIndex = 0
    while (queue.isNotEmpty()) {
        val node = queue.removeFirst()
        result[currentIndex++] = node

        for (children in input) {
            for (child in children) {
                dependencies[child]--
                if (dependencies[child] == 0) queue.addLast(child)
            }
        }
    }

    return result
}

fun main() {
    val input = arrayOf(
        arrayOf(1), arrayOf(2), arrayOf(), arrayOf(2, 4), arrayOf()
    )

    val result = kahnTopologicalSort(input)
    println(result.contentToString())
}