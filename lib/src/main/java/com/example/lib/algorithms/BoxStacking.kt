package com.example.lib.algorithms

data class Box(val length: Int, val width: Int, val height: Int) {
    fun canBeStackedOn(box: Box): Boolean {
        return length < box.length && width < box.width
    }
}

private fun boxStacking(boxes: List<Box>): Int {
    val heights = IntArray(boxes.size) { -1 }
    val graph = Array<MutableList<Int>>(boxes.size) { mutableListOf() }

    for (base in graph.indices) {
        for (top in graph.indices) {
            val t = boxes[top]
            if (base != top && t.canBeStackedOn(boxes[base])) {
                graph[base].add(top)
            }
        }
    }

    var maxHeight = 0
    for (i in heights.indices) {
        maxHeight = getHeight(boxes, heights, graph, i).coerceAtLeast(maxHeight)
    }

    return maxHeight
}

private fun getHeight(
    boxes: List<Box>,
    heights: IntArray,
    graph: Array<MutableList<Int>>,
    index: Int
): Int {
    if (heights[index] != -1) return heights[index]

    var maxHeight = 0

    for (child in graph[index]) {
        maxHeight = maxHeight.coerceAtLeast(
            getHeight(boxes, heights, graph, child)
        )
    }

    heights[index] = boxes[index].height + maxHeight
    return heights[index]
}

fun main() {
//    val input = listOf(
//        Box(2, 3, 3),
//        Box(2, 2, 4),
//        Box(4, 4, 2),
//    ) // -> 6
    val input = listOf(
        Box(4, 5, 3),
        Box(2, 3, 2),
        Box(3, 6, 2),
        Box(1, 5, 4),
        Box(2, 4, 1),
        Box(1, 2, 2),
    ) //-> 7
    val result = boxStacking(input)
    println(result)
}
