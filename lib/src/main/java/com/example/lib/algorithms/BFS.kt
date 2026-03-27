package com.example.lib.algorithms

import java.util.LinkedList
import kotlin.collections.isNotEmpty
import kotlin.collections.map

private fun <T : Comparable<T>> bfs(tree: ITree<T>, target: T): ITree.INode<T>? {
    val queue = ArrayDeque<ITree.INode<T>>()
    tree.root?.let { queue.add(it) }

    while (queue.isNotEmpty()) {
        val node = queue.removeFirst()
        println("Checking ${node.value}")

        if (node.value == target) return node
        node.left?.let { queue.addLast(it) }
        node.right?.let { queue.addLast(it) }
    }

    return null
}

fun main() {
    val t = BinarySearchTree<Int>().apply {
        add(10)
        add(5)
        add(20)
        add(3)
        add(7)
        add(15)
        add(30)
        add(1)
        add(4)
        add(6)
        add(8)
        add(12)
        add(35)
        add(2)
        add(9)
        add(11)
        add(13)
        add(32)
        add(37)
        add(14)
        add(31)
    }

//    depths(t).forEach { println(it) }

    println("Found: ${bfs(t, -1)?.value}")
}

private fun <T : Comparable<T>> depths(tree: ITree<T>): List<List<T>> {
    val result = mutableListOf<List<T>>()

    var currentLevel: LinkedList<ITree.INode<T>>? = null
    tree.root?.let { root ->
        currentLevel = LinkedList<ITree.INode<T>>()
        currentLevel.add(root)
    }

    while (currentLevel?.isNotEmpty() == true) {
        result.add(currentLevel.map { it.value })
        val parents = currentLevel
        currentLevel = LinkedList<ITree.INode<T>>()

        for (p in parents) {
            p.left?.let { currentLevel.add(it) }
            p.right?.let { currentLevel.add(it) }
        }
    }

    return result
}