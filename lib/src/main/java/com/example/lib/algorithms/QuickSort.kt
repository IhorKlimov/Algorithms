package com.example.lib.algorithms

fun main() {
    val arr = intArrayOf(5, 2, 1, 0, 3, 4)
    quickSort(arr, 0, arr.size - 1)
    println(arr.contentToString())
}

private fun quickSort(arr: IntArray, from: Int, to: Int) {
    if (from >= to) return
    var i = from - 1
    val p = (from + to) / 2
    val pivot = arr[p]

    val t = arr[p]
    arr[p] = arr[to]
    arr[to] = t

    for (j in from..<to) {
        if (arr[j] < pivot) {
            val temp = arr[j]
            arr[j] = arr[++i]
            arr[i] = temp
        }
    }
    val temp = arr[++i]
    arr[i] = arr[to]
    arr[to] = temp

    quickSort(arr, from, i - 1)
    quickSort(arr, i + 1, to)
}