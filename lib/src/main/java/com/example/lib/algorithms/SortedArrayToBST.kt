package com.example.lib.algorithms

fun main() {
    val arr = (0..10).toList().toIntArray() // 0 1 2 3 4 5
    val root = convert(arr, 0, arr.size)
    println(root?.value)

    println(root?.left?.value)
    println(root?.right?.value)

    println(root?.left?.left?.value)
    println(root?.left?.right?.value)
    println(root?.right?.left?.value)
    println(root?.right?.right?.value)

}

private fun convert(array: IntArray, left: Int, right: Int): BSTNode? {
    /*
   *         3
   *       /   \
   *      1     5
   *    /  \   /
   *   0    2 4
   *
   *   (0 + 6) / 2 = 3
   *        (0 + 3) / 2 = 1
   *            (0 + 1) / 2 = 0
   *                (0 + 0) - break
   *            (2 + 3) / 2 = 2
   *                (3 + 3) - break
   *        (4 + 6) / 2 = 5
   *            (4 + 5) / 2 = 4
   *                (5 + 5) - break
   *            (6 + 6) - break
   *
   * 0 1 2 3 4 5
   * */

    if (array.isEmpty()) return null
    if (left >= right) return null

    val middle = (left + right) / 2

    val newNode = BSTNode(array[middle])
    newNode.left = convert(array, left, middle)
    newNode.right = convert(array, middle + 1, right)

    return newNode
}

private class BSTNode(val value: Int, var left: BSTNode? = null, var right: BSTNode? = null)