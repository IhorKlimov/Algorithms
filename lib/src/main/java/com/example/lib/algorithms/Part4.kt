package com.example.lib.algorithms

import com.example.lib.algorithms.TreeNode
import java.util.LinkedList
import kotlin.math.abs
import kotlin.math.max
import kotlin.random.Random

interface ITree<T : Comparable<T>> {
    val root: INode<T>?

    interface INode<T : Comparable<T>> {
        val value: T
        var left: INode<T>?
        var right: INode<T>?
        var parent: INode<T>?
        var size: Int
    }

    fun add(value: T)

    fun printInOrder() {
        recPrintInOrder(root)
    }

    private fun recPrintInOrder(node: INode<T>?) {
        if (node == null) return

        recPrintInOrder(node.left)
        println(node.value)
        recPrintInOrder(node.right)
    }

    fun printPreOrder() {
        recPrintPreOrder(root)
    }

    private fun recPrintPreOrder(node: INode<T>?) {
        if (node == null) return

        println(node.value)
        recPrintPreOrder(node.left)
        recPrintPreOrder(node.right)
    }

    fun printPostOrder() {
        recPrintPostOrder(root)
    }

    private fun recPrintPostOrder(node: INode<T>?) {
        if (node == null) return

        recPrintPostOrder(node.left)
        recPrintPostOrder(node.right)
        println(node.value)
    }

    fun printDecreasing() {
        recPrintDecreasing(root)
    }

    private fun recPrintDecreasing(node: INode<T>?) {
        if (node == null) return

        recPrintDecreasing(node.right)
        println(node.value)
        recPrintDecreasing(node.left)
    }
}

class BinarySearchTree<T : Comparable<T>> : ITree<T> {
    override var root: ITree.INode<T>? = null
        private set

    override fun add(value: T) {
        val node = TreeNode(value)

        var n = root
        while (true) {
            if (n == null) {
                root = node
                break
            } else {
                n.size++
                if (value <= n.value) { // go left
                    if (n.left == null) {
                        n.left = node
                        node.parent = n
                        break
                    } else {
                        n = n.left
                    }
                } else { // go right
                    if (n.right == null) {
                        n.right = node
                        node.parent = n
                        break
                    } else {
                        n = n.right
                    }
                }
            }
        }
    }

    fun find(value: T): ITree.INode<T>? {
        var currentNode = root
        var result: ITree.INode<T>? = null

        while (currentNode != null && result?.value != value) {
            if (currentNode.value == value) result = currentNode
            else if (value <= currentNode.value) currentNode = currentNode.left
            else if (value > currentNode.value) currentNode = currentNode.right
        }

        return result
    }

    /**
     * Remove a node with the given [value].
     *
     * @return true, if the value was present in the list and removed. False otherwise.
     * */
    fun remove(value: T): Boolean {
        var currentNode: ITree.INode<T>? = root
        var parentNode: ITree.INode<T>? = null

        find(value) ?: return false

        while (currentNode != null && currentNode.value != value) {
            currentNode.size--
            parentNode = currentNode
            currentNode = if (value <= currentNode.value) currentNode.left else currentNode.right
        }

        if (currentNode == null || currentNode.value != value) return false

        if (currentNode.left != null && currentNode.right != null) { // two children
            val previousInOrderChild = getPreviousInOrderNode(currentNode)
            (previousInOrderChild.second?.left
                ?: previousInOrderChild.second?.right)?.let { child ->
                val node = previousInOrderChild.second
                if (node != null) {
                    replace(previousInOrderChild.first, node, child)
                }
            }

            val node = previousInOrderChild.second
            if (node != null) replace(parentNode, currentNode, node)
            if (currentNode === root) root = node

            return true
        } else if (currentNode.left == null && currentNode.right == null) { // no children
            if (currentNode == root) root = null
            else if (parentNode?.left == currentNode) parentNode.left = null
            else if (parentNode?.right == currentNode) parentNode.right = null
            return true
        } else { // just one child
            val child = currentNode.left ?: currentNode.right ?: return false
            replace(parentNode, currentNode, child)
            if (currentNode === root) root = child
            return true
        }
    }

    /**
     * Returns a node that would appear in in-order traversal just before the given node and its
     * parent.
     *
     * @return a pair of nodes, where the first item is parent of the found node and the second is
     * the found node.
     * */
    private fun <T : Comparable<T>> getPreviousInOrderNode(node: ITree.INode<T>): Pair<ITree.INode<T>, ITree.INode<T>?> {
        var parent = node
        var child = node.left

        while (child?.right != null) {
            parent = child
            child = child.right
        }

        return Pair(parent, child)
    }

    /**
     * Replaces [node] with [otherNode] and adjusts all the links.
     *
     * @param parent parent of the [node]
     * @param node a node to be replaced
     * @param otherNode a node that will replace [node]
     * */
    private fun <T : Comparable<T>> replace(
        parent: ITree.INode<T>?,
        node: ITree.INode<T>,
        otherNode: ITree.INode<T>
    ) {
        if (parent != null) {
            val isLeftChild = parent.left === node
            if (isLeftChild) parent.left = otherNode
            else parent.right = otherNode
        }
        otherNode.left = if (node.left != otherNode) node.left else null
        otherNode.right = if (node.right != otherNode) node.right else null
        // Update sizes
        otherNode.size = (otherNode.left?.size ?: 0) + (otherNode.right?.size ?: 0) + 1
        parent?.size = (parent.left?.size ?: 0) + (parent.right?.size ?: 0) + 1
    }
}


class TreeNode<T : Comparable<T>>(
    override val value: T,
    override var left: ITree.INode<T>? = null,
    override var right: ITree.INode<T>? = null
) : ITree.INode<T> {
    override var parent: ITree.INode<T>? = null
    override var size = 1
}

class CompleteTree<T : Comparable<T>> : ITree<T> {
    override var root: TreeNode<T>? = null

    override fun add(value: T) {
        val node = TreeNode(value)
        val r = root

        if (r == null) {
            root = node
        } else {
            addNode(listOf(r), node)
        }
    }

    private fun addNode(level: List<ITree.INode<T>>, node: TreeNode<T>) {
        for (n in level) {
            if (n.left == null) {
                n.left = node
                return
            } else if (n.right == null) {
                n.right = node
                return
            }
        }

        addNode(level.flatMap {
            sequence {
                yield(it.left)
                yield(it.right)
            }
        }.filterNotNull(), node)
    }

    data class TreeNode<T : Comparable<T>>(
        override val value: T,
        override var left: ITree.INode<T>? = null,
        override var right: ITree.INode<T>? = null
    ) : ITree.INode<T> {
        override var parent: ITree.INode<T>? = null
        override var size: Int = 0
    }
}

class SimpleTree<T : Comparable<T>> : ITree<T> {
    override var root: ITree.INode<T>? = null

    override fun add(value: T) {
        val node = TreeNode(value)

        var n = root
        while (true) {
            if (n == null) {
                root = node
                break
            } else {
                n.size++
                if (Random.nextBoolean()) { // go left
                    if (n.left == null) {
                        n.left = node
                        node.parent = n
                        break
                    } else {
                        n = n.left
                    }
                } else { // go right
                    if (n.right == null) {
                        n.right = node
                        node.parent = n
                        break
                    } else {
                        n = n.right
                    }
                }
            }
        }
    }

    data class TreeNode<T : Comparable<T>>(
        override val value: T,
        override var left: ITree.INode<T>? = null,
        override var right: ITree.INode<T>? = null
    ) : ITree.INode<T> {
        override var parent: ITree.INode<T>? = null
        override var size: Int = 1
    }
}

data class Graph(val nodes: List<Node>)
data class Node(val name: Int, var children: List<Node> = listOf()) {
    var visited = false

    override fun toString(): String {
        return "Name: $name children: ${children.map { it.name }}"
    }
}

private fun findShortestPath(graph: Graph, from: Node, to: Node) {
    val paths = mutableMapOf<Int, List<Node>>()

    val queue = ArrayDeque<Node>()
    queue.add(from)

    while (queue.isNotEmpty()) {
        val node = queue.removeFirst()
        println("Checking ${node.name}")

        node.visited = true
        if (node == to) {
            println(
                "Found path! ${
                    (paths[node.name]?.plus(node))
                        ?.map { it.name }
                        ?.joinToString(separator = "->")
                }"
            )
            graph.nodes.forEach { it.visited = false }
            return
        }
        for (n in node.children) {
            if (!n.visited) {
                paths[n.name] = (paths[node.name] ?: listOf()) + node
                n.visited = true
                queue.add(n)
            }
        }
    }

    graph.nodes.forEach { it.visited = false }
    println("No path found!")
}

class DependencyGraph<T : Comparable<T>> {
    private val _nodes = mutableMapOf<T, Node<T>>()
    val nodes: Map<T, Node<T>> = _nodes

    fun add(value: T) {
        _nodes[value] = Node(value)
    }

    fun getNode(value: T): Node<T>? {
        return nodes[value]
    }

    data class Node<T : Comparable<T>>(
        val value: T,
        var isVisited: Boolean = false,
        var height: Int? = null
    ) {
        private val _children = mutableListOf<Node<T>>()
        val children: List<Node<T>> = _children

        fun addChild(node: Node<T>) {
            _children.add(node)
        }
    }
}

private fun task2(list: List<Int>, tree: ITree<Int>) {
    task2Rec(list, tree, 0, list.size - 1)
}

private fun task2Rec(list: List<Int>, tree: ITree<Int>, from: Int, to: Int) {
    if (from > to) return

    val middle = (to + from) / 2

    tree.add(list[middle])
    task2Rec(list, tree, from, middle - 1)
    task2Rec(list, tree, middle + 1, to)
}

private fun <T : Comparable<T>> task3(tree: ITree<T>): List<List<T>> {
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

// task 4
private fun <T : Comparable<T>> isBalanced(node: ITree.INode<T>?): Boolean {
    return treeHeight2(node) != -1
}

private fun <T : Comparable<T>> treeHeight2(node: ITree.INode<T>?): Int {
    if (node == null) return 0

    val leftHeight = treeHeight2(node.left)
    if (leftHeight == -1) return -1

    val rightHeight = treeHeight2(node.right)
    if (rightHeight == -1) return -1

    val difference = abs(leftHeight - rightHeight)
    if (difference > 1) return -1
    return 1 + max(leftHeight, rightHeight)
}

private fun <T : Comparable<T>> treeHeight(node: ITree.INode<T>?): Int {
    if (node == null) return 0
    val leftHeight = treeHeight(node.left)
    val rightHeight = treeHeight(node.right)
    return 1 + max(leftHeight, rightHeight)
}

// task 5
private fun isBinarySearchTree(tree: ITree<Int>): Boolean {
    return isBinaryOrderCheck(tree.root)
}

private fun isBinaryOrderCheck(
    node: ITree.INode<Int>?,
    leftBound: Int? = null,
    rightBound: Int? = null,
): Boolean {
    if (node == null) return true

    val leftOrdered = isBinaryOrderCheck(node.left, leftBound, node.value)
    if (!leftOrdered ||
        !isWithinBounds(
            node.left?.value,
            leftBound,
            node.value
        )
    ) return false

    val rightOrdered = isBinaryOrderCheck(node.right, node.value + 1, rightBound)
    if (!rightOrdered ||
        !isWithinBounds(
            node.right?.value,
            node.value + 1,
            rightBound
        )
    ) return false

    return true
}

private fun <T : Comparable<T>> isWithinBounds(
    value: T?,
    leftBound: T? = null,
    rightBound: T? = null
): Boolean {
    if (value == null) return true

    return if (leftBound != null && value < leftBound) false
    else if (rightBound != null && value > rightBound) false
    else true
}

// task 6
var foundTarget = false

private fun <T : Comparable<T>> getSuccessor(node: ITree.INode<T>?): ITree.INode<T>? {
    foundTarget = false
    if (node == null) return null

    val parent = node.parent ?: node.right
    if (node.parent == null) foundTarget = true

    return successorInOrderTraversal(parent, node)
}


private fun <T : Comparable<T>> successorInOrderTraversal(
    node: ITree.INode<T>?,
    target: ITree.INode<T>
): ITree.INode<T>? {
    if (node == null) return null

    val leftResult = successorInOrderTraversal(node.left, target)
    if (leftResult != null) return leftResult

    if (foundTarget) return node
    else if (node === target) foundTarget = true

    val rightResult = successorInOrderTraversal(node.right, target)
    if (rightResult != null) return rightResult

    return null
}

private fun <T : Comparable<T>> getSuccessor2(node: ITree.INode<T>?): ITree.INode<T>? {
    if (node == null) return null

    // If righ node exists, return left-most node of the right sub-tree
    val rightNode = getSuccessor2Rec(node.right)
    if (rightNode != null) return rightNode

    // else, go up the tree to get the tree
    // If the target is left child node, then return parent
    var currentNode = node
    while (currentNode != null) {
        if (currentNode.parent?.left === currentNode) return currentNode.parent
        currentNode = currentNode.parent
    }

    // No successor is present
    return null
}

private fun <T : Comparable<T>> getSuccessor2Rec(node: ITree.INode<T>?): ITree.INode<T>? {
    if (node == null) return null
    return getSuccessor2Rec(node.left) ?: node
}

// task 7
private fun <T : Comparable<T>> getDependencyBuildList(
    items: List<T>,
    dependencies: List<Pair<T, T>>
): List<T>? {
    // Step 1. Build a graph
    val graph = DependencyGraph<T>()
    items.forEach(graph::add)
    for (dependency in dependencies) {
        val parent = graph.getNode(dependency.first) ?: continue
        graph.getNode(dependency.second)?.addChild(parent)
    }

    val cells = mutableMapOf<Int, MutableList<DependencyGraph.Node<T>>>()

    for (node in graph.nodes.values) {
        // Step 2. Calculate heights
        val height = getNodeHeight(node)
        if (height == -1) return null

        // Step 3. Populate cells
        val cell = cells[height] ?: mutableListOf<DependencyGraph.Node<T>>().also {
            cells[height] = it
        }
        cell.add(node)
    }

    // Step 4. Build dependency list
    val result = mutableListOf<T>()
    for (height in 0..items.size) {
        val cell = cells[height]
        if (cell != null) {
            for (node in cell) {
                result.add(node.value)
            }
        }
    }

    return result
}

private fun <T : Comparable<T>> getNodeHeight(node: DependencyGraph.Node<T>?): Int {
    if (node == null || node.children.isEmpty()) return 0
    else if (node.isVisited && node.height == null) return -1
    node.height?.let { return it }

    node.isVisited = true
    var maxChildrenHeight = 0
    for (child in node.children) {
        val h = getNodeHeight(child)
        if (h == -1) return -1
        if (h > maxChildrenHeight) maxChildrenHeight = h
    }
    val height = 1 + maxChildrenHeight
    node.height = height

    return height
}

// task 8
private fun <T : Comparable<T>> findFirstCommonAncestor(tree: ITree<T>, first: T, second: T): T? {
    return findFirstCommonAncestor(tree.root, first, second)
}

private fun <T : Comparable<T>> findFirstCommonAncestor(
    node: ITree.INode<T>?,
    first: T,
    second: T
): T? {
    val firstNode =
        hasNodeInSubtree(node?.left, first) ?: hasNodeInSubtree(node?.right, first)
    val secondNode =
        hasNodeInSubtree(node?.left, second) ?: hasNodeInSubtree(node?.right, second)

    if (firstNode == secondNode) { // same subtree
        if (node?.left?.value == first ||
            node?.left?.value == second ||
            node?.right?.value == first ||
            node?.right?.value == second
        ) {
            return node.value
        }
        return when (firstNode) {
            node?.left -> findFirstCommonAncestor(node?.left, first, second)
            node?.right -> findFirstCommonAncestor(node?.right, first, second)
            else -> null
        }
    } else if (firstNode != null && secondNode != null) { // different subtrees
        return node?.value
    } else {
        return null
    }
}

private fun <T : Comparable<T>> hasNodeInSubtree(node: ITree.INode<T>?, value: T): ITree.INode<T>? {
    return if (node == null) {
        null
    } else if (node.value == value) {
        node
    } else {
        return if (hasNodeInSubtree(node.left, value) != null ||
            hasNodeInSubtree(node.right, value) != null
        ) node
        else null
    }
}

// Task 9
var branchIndex = 0

private fun <T : Comparable<T>> getAllArrayCombinations(tree: ITree<T>): List<List<T>> {
    val result = mutableListOf<MutableList<T>>()

    branchIndex = 0
    tree.root?.let {
        populateCombinations(it, mutableListOf(), result, 0)
    }

    return result
}

private fun <T : Comparable<T>> populateCombinations(
    node: ITree.INode<T>,
    availableOptions: MutableList<ITree.INode<T>>,
    result: MutableList<MutableList<T>>,
    index: Int
) {
    // Get or create an output for the current branch
    val output = result.getOrNull(index) ?: mutableListOf<T>().also {
        if (result.size > index) {
            result[index] = it
        } else {
            result.add(it)
        }
    }
    output.add(node.value)

    node.left?.let { availableOptions.add(it) }
    node.right?.let { availableOptions.add(it) }

    val copy = mutableListOf<T>().apply { addAll(output) }

    availableOptions.forEachIndexed { i, node ->
        if (i > 0) {
            // Create a new branch and copy the nodes from the origin branch to the new branch
            result.add(mutableListOf<T>().apply { addAll(copy) })
            branchIndex++
        }
        populateCombinations(
            node,
            availableOptions.filter { it != node }.toMutableList(),
            result,
            branchIndex
        )
    }
}

// Recursive, preserves the original order of both lists
private fun weaveRec(
    first: LinkedList<Int>,
    second: LinkedList<Int>,
    prefix: LinkedList<Int>
): List<List<Int>> {
    val result = mutableListOf<List<Int>>()

    if (first.isEmpty() || second.isEmpty()) {
        val output = mutableListOf<Int>().apply {
            addAll(prefix)
            addAll(first)
            addAll(second)
        }
        result.add(output)
        return result
    }

    prefix.addLast(first.removeFirst())
    val weavedLeft = weaveRec(first, second, prefix)
    first.addFirst(prefix.removeLast())

    prefix.addLast(second.removeFirst())
    val weavedRight = weaveRec(first, second, prefix)
    second.addFirst(prefix.removeLast())

    result.addAll(weavedLeft)
    result.addAll(weavedRight)

    return result
}

// Task 10

/**
 * Returns whether the [second] tree is a subtree of [first] tree
 * */
private fun <T : Comparable<T>> isSubTree(first: ITree<T>, second: ITree<T>): Boolean {
    return isSubTree(first.root, second.root)
}

private fun <T : Comparable<T>> isSubTree(
    first: ITree.INode<T>?,
    second: ITree.INode<T>?
): Boolean {
    // Pre-order traversal
    if (first == null) return false
    if (first.value == second?.value && areTreesEqual(first, second)) return true
    return isSubTree(first.left, second) || isSubTree(first.right, second)
}

private fun <T : Comparable<T>> areTreesEqual(
    first: ITree.INode<T>?,
    second: ITree.INode<T>?
): Boolean {
    if (first == null && second == null) return true
    if (first?.value != second?.value) return false
    return areTreesEqual(first?.left, second?.left) && areTreesEqual(first?.right, second?.right)
}

// Task 11
private fun <T : Comparable<T>> getRandom(tree: ITree<T>): ITree.INode<T>? {
    if (tree.root == null) return null

    val index = Random.nextInt(1, (tree.root?.size ?: return null) + 1)
    println("Random index: $index")

    var node = tree.root
    var memory = 0
    var result: ITree.INode<T>? = null
    while (node != null) {
        val leftSize = node.left?.size ?: 0
        val currentIndex = memory + leftSize + 1

        if (currentIndex == index) { // found the node
            result = node
            break
        } else if (index < currentIndex) { // go left
            node = node.left
        } else { // go right
            memory += leftSize + 1
            node = node.right
        }
    }

    return result
}

// Task 12
private fun <T : Comparable<T>> pathsWithSum(tree: ITree<T>, targetSum: Int): Int {
    val result = mutableListOf<MutableList<ITree.INode<T>>>()

    pathsWithSumInternal(
        tree.root,
        mutableListOf(),
        result
    )
    for (nodes in result) {
        println(nodes.joinToString { "${it.value}" })
    }

//    slidingWindowWithNegativeNums()
    return -1
}

private fun <T : Comparable<T>> pathsWithSumInternal(
    node: ITree.INode<T>?,
    branch: MutableList<ITree.INode<T>>,
    result: MutableList<MutableList<ITree.INode<T>>>
) {
    if (node == null) return

    branch.add(node)

    val rightBranch = if (node.left != null && node.right != null) {
        mutableListOf<ITree.INode<T>>().apply { addAll(branch) }
    } else {
        branch
    }
    pathsWithSumInternal(node.left, branch, result)
    pathsWithSumInternal(node.right, rightBranch, result)
    if (node.left == null && node.right == null) result.add(branch)
}


/**
 * Finds a sub-arrays of [arr], where a sum of elements results in [targetSum]. O(N)
 *
 * @param arr an array input
 * @param targetSum a desired sum of elements
 * @return list of sub-arrays, where a sum of elements results in [targetSum]
 * */
private fun slidingWindowWithNegativeNums(arr: List<Int>, targetSum: Int): List<List<Int>> {
    val result = mutableListOf<List<Int>>()
    if (arr.isEmpty()) return result

    val previous = mutableMapOf<Int, Int>()
    var sum = 0

    for (i in arr.indices) {
        sum += arr[i]
        if (previous.contains(sum - targetSum)) {
            val start = previous.getValue(sum - targetSum) + 1
            result.add(arr.copyOfRange(start, i + 1))
            previous.clear()
        }
        previous[sum] = i
    }

    return result
}

private fun <T> List<T>.copyOfRange(start: Int, end: Int): List<T> {
    val result = ArrayList<T>(end - start)
    for (i in start..<end) {
        result.add(get(i))
    }
    return result
}

/**
 * Finds a sub-array of [arr], where a sum of elements results in [targetSum]. O(n^2)
 *
 * @param arr an array input
 * @param targetSum a desired sum of elements
 * @return a sub-array, where a sum of elements results in [targetSum]
 * */
private fun slidingWindowWithNegativeNumsN2(arr: Array<Int>, targetSum: Int): Array<Int>? {
    for (i in 0..<arr.size) {
        var sum = 0

        for (j in i..<arr.size) {
            sum += arr[j]
            if (sum == targetSum) {
                val result = Array(j - i + 1) { 0 }
                for (k in i..j) {
                    result[k - i] = arr[k]
                }
                return result
            }
        }
    }

    return null
}


fun main() {
//    val list = (5..25).toList()
//    val tree = BinarySearchTree<Int>()
//
//    task2(list, tree)
//    tree.add(1)
//    tree.add(2)
//
//    tree.printInOrder()
//    println("Tree size: ${tree.size}")
//    val depths = task3(tree)
//    println("Is balanced ${isBalanced(tree.root)}")
//
//    for (d in 0..<depths.size) {
//        println(depths[d].joinToString(separator = " "))
//    }

//        val tree = SimpleTree<Int>()
//        tree.root = SimpleTree.TreeNode(5)
//
//        tree.root?.left = SimpleTree.TreeNode(3)
//        tree.root?.right = SimpleTree.TreeNode(7)
//
//        tree.root?.left?.left = SimpleTree.TreeNode(1)
//        tree.root?.left?.right = SimpleTree.TreeNode(5)
//
//        tree.root?.right?.left = SimpleTree.TreeNode(4)
//        tree.root?.right?.right = SimpleTree.TreeNode(9)
//
//
//        val depths = task3(tree)
//        for (d in 0..<depths.size) {
//            println(depths[d].joinToString(separator = " "))
//        }
//        println("Is BST ${isBinarySearchTree(tree)}")
//
//    val n = tree.root?.right?.right?.right?.right

//    println("Successor of ${n?.value} is ${getSuccessor2(n)?.value}")


//    val items = listOf("a", "b", "c", "d", "e", "f")
//    val dependencies = listOf(
//        Pair("a", "d"),
//        Pair("f", "b"),
//        Pair("b", "d"),
//        Pair("f", "a"),
//        Pair("d", "c"),
//    )
//    val list = getDependencyBuildList(items, dependencies)
//    println(list)

//    val tree = BinarySearchTree<Int>()
//    tree.add(10)
//    tree.add(5)
//    tree.add(15)
//
//    val tree2 = BinarySearchTree<Int>()
//    tree2.add(10)
//    tree2.add(5)
//    tree2.add(15)
//    tree.add(3)
//    tree.add(8)
//    tree.add(20)
//    tree.add(9)
//    tree.add(1)
//    tree.add(2)
//    tree.add(26)
//    tree.add(23)

//    val result = getAllArrayCombinations(tree)
//
//    result.forEach(::println)
//
//    var hasDuplicates = false
//    for (i in 0..<result.size) {
//        for (j in 0..<result.size) {
//            if (i != j && result[i] == result[j]) {
//                hasDuplicates = true
//            }
//        }
//    }
//
//    println("Has duplicates: $hasDuplicates")

//    val first = LinkedList<Int>()
//    first.add(1)
//    first.add(2)
//    val second = LinkedList<Int>()
//    second.add(3)
//    second.add(4)
//    val prefix = LinkedList<Int>()
//    println(weaveRec(first, second, prefix))

    val tree = BinarySearchTree<Int>().apply {
        add(10)
        add(5)
        add(16)
        add(12)
        add(11)
        add(1)
        add(2)
        add(13)
        add(15)
//        add(14)
//        add(20)
    }

//    val depths = task3(tree)
//    for (d in 0..<depths.size) {
//        println(depths[d].joinToString(separator = " "))
//    }

//    pathsWithSum(tree)
//    val tree2 = BinarySearchTree<Int>()
//    tree2.add(10)
//    tree2.add(5)
//    tree2.add(15)
//    tree2.add(25)

//    println(isSubTree(tree, tree2))
//    println(areTreesEqual(tree.root, tree2.root))

//    println("Find node: ${tree.find(4)?.value}")
//    tree.remove(20)
//    println(getRandom(tree)?.value)

//    val input = arrayOf(10, 8, -3, -5, 2, -2, 4, 3, 1, -1)
//    val result = slidingWindowWithNegativeNums(input, 0)
//    result.forEach {
//        println(it.contentToString())
//    }

}

