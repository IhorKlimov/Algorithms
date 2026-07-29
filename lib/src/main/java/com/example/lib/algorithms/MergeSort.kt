package com.example.lib.algorithms

fun main() {
    val n1 = MyNode(
        4,
        MyNode(
            2,
            MyNode(
                1,
                MyNode(3)
            )
        )
    )
    val n2 = MyNode(
        2,
        MyNode(
            5,
            MyNode(8)
        )
    )
    val sorted = mergeSort(n1)
    var n = sorted
    while (n != null) {
        println(n.value)
        n = n.next
    }
}

private fun mergeSort(node: MyNode?): MyNode? {
    if (node?.next == null) return node

    var n = node
    var runner = n.next?.next

    while (runner != null) {
        n = n?.next
        runner = runner.next?.next
    }
    val rightHalf = n?.next
    n?.next = null

    return merge(mergeSort(node), mergeSort(rightHalf))
}

private fun merge(node1: MyNode?, node2: MyNode?): MyNode? {
    println("Merge ${node1?.value} ${node2?.value}")

    var n1 = node1
    var n2 = node2
    val result = MyNode(-1)
    var curr: MyNode? = result

    while (n1 != null && n2 != null) {
        if (n1.value <= n2.value) {
            curr?.next = n1
            n1 = n1.next
        } else {
            curr?.next = n2
            n2 = n2.next
        }
        curr = curr?.next
    }
    curr?.next = n1 ?: n2

    return result.next
}

private class MyNode(val value: Int, var next: MyNode? = null)


