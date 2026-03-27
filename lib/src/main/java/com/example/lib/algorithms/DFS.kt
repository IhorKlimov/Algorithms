package com.example.lib.algorithms

private fun <T : Comparable<T>> dfs1(tree: ITree<T>, target: T): ITree.INode<T>? {
    return dfsRec(tree.root, target)
}

private fun <T : Comparable<T>> dfs2(tree: ITree<T>, target: T): ITree.INode<T>? {
    val queue = ArrayDeque<ITree.INode<T>>()
    val processed = mutableSetOf<ITree.INode<T>>()
    tree.root?.let { queue.addFirst(it) }

    while (queue.isNotEmpty()) {
        val node = queue.first()
        println("Checking ${node.value}")
        if (node.value == target) return node

        node.left?.let {
            if (!processed.contains(it)) {
                queue.addFirst(it)
                continue
            }
        }
        node.right?.let {
            if (!processed.contains(it)) {
                queue.addFirst(it)
                continue
            }
        }
        processed.add(node)
        queue.removeFirst()
    }

    return null
}

private fun <T : Comparable<T>> dfsRec(node: ITree.INode<T>?, target: T): ITree.INode<T>? {
    if (node == null) return null
    println("Checking ${node.value}")
    if (node.value == target) return node
    return dfsRec(node.left, target) ?: dfsRec(node.right, target)
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

    println("Found: ${dfs1(t, 31)?.value}")
}