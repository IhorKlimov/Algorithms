package com.example.lib.algorithms

import kotlin.math.abs

private class LinkedList<T : Int> {
    private var root: Node<T>? = null
    var size = 0
        private set

    fun add(value: T) {
        if (root == null) {
            root = Node(value)
        } else {
            var n: Node<T>? = root
            while (n?.next != null) n = n.next
            n?.next = Node(value)
        }
        size++
    }

    fun add(node: Node<T>) {
        if (root == null) {
            root = node
        } else {
            var n: Node<T>? = root
            while (n?.next != null) n = n.next
            n?.next = node
        }

        var s = 0
        var n = root
        while (n != null) {
            s++
            n = n.next
        }
        size = s
    }

    fun addToStart(value: T) {
        val node = Node(value)
        node.next = root
        root = node
    }

    fun get(index: Int): Node<T>? {
        var node = root
        for (i in 0..index) {
            if (i == index) {
                return node
            }
            node = node?.next
        }
        return null
    }

    private fun indexToLast(node: Node<T>): Int {
        val next = node.next
        return if (next == null) {
            1
        } else {
            1 + indexToLast(next)
        }
    }

    override fun toString(): String {
        val builder = StringBuilder(size + 2)
        var n = root
        builder.append("[")
        while (n != null) {
            builder.append("${n.value}")
            n = n.next
            if (n != null) {
                builder.append(", ")
            }
        }
        builder.append("]")

        return builder.toString()
    }

    fun task1() {
        val occurrences = mutableSetOf<T>()
        var current = root
        var previous: Node<T>? = null

        while (current != null) {
            if (occurrences.contains(current.value)) {
                previous?.next = current.next
                size--
            } else {
                occurrences.add(current.value)
                previous = current
            }
            current = current.next
        }
    }

    fun task1N2() {
        var n = root
        var previous: Node<T>? = null

        while (n != null) {
            val value = n.value
            var comparing = n.next
            var isDuplicate = false
            while (comparing != null) {
                if (comparing.value == value) {
                    if (n === root) {
                        root = n.next
                    } else {
                        previous?.next = n.next
                    }
                    isDuplicate = true
                    size--
                    break
                } else {
                    comparing = comparing.next
                }
            }

            if (!isDuplicate) previous = n
            n = n.next
        }
    }

    fun task2(k: Int): Node<T>? {
        var s = 0
        var current = root
        while (current != null) {
            s++
            current = current.next
        }
        if (k > s) return null

        current = root
        var i = 0
        while (current != null) {
            if (i == s - k) return current

            current = current.next
            i++
        }

        return null
    }

    fun task2Recursion(k: Int): Node<T>? {
        var current = root
        while (current != null) {
            if (indexToLast(current) == k) return current
            current = current.next
        }
        return null
    }

    fun task2Runner(k: Int): Node<T>? {
        var current = root
        var runner = current
        repeat(k - 1) {
            runner = runner?.next
        }
        if (runner == null) return null

        while (runner?.next != null) {
            current = current?.next
            runner = runner.next
        }
        return current
    }

    fun task3(node: Node<T>) {
        node.next?.let { next ->
            node.value = next.value
            node.next = next.next
            size--
        }
    }

    fun task4(x: T) {
        var left: Node<T>? = null
        var right: Node<T>? = null

        var n = root
        var l = left
        var r = right
        while (n != null) {
            if (n.value >= x) {
                if (right == null) {
                    right = n
                } else {
                    r?.next = n
                }
                r = n
            } else {
                if (left == null) {
                    left = n
                } else {
                    l?.next = n
                }
                l = n
            }

            n = n.next
        }

        root = left
        l?.next = right
        r?.next = null

        println("Left: $left")
        println("Right: $right")
    }

    fun task5(list2: LinkedList<T>): LinkedList<Int> {
        val result = LinkedList<Int>()

        var n1 = root
        var n2 = list2.root
        var rollOver = 0

        while (n1 != null || n2 != null) {
            val d1 = n1?.value ?: 0
            val d2 = n2?.value ?: 0

            val sum = d1 + d2 + rollOver
            result.add(sum % 10)
            rollOver = sum / 10

            n1 = n1?.next
            n2 = n2?.next
        }

        return result
    }

    fun task5Forward(list: LinkedList<T>): LinkedList<Int> {
        val result = LinkedList<Int>()

        val sizeDiff = abs(size - list.size)
        if (sizeDiff != 0) {
            addZerosToBeginning(
                if (size > list.size) list else this,
                sizeDiff
            )
        }

        val n1 = root
        val n2 = list.root

        task5Rec(n1, n2, result, 0)

        return result
    }

    private fun addZerosToBeginning(list: LinkedList<T>, count: Int) {
        repeat(count) {
            list.addToStart(0 as T)
        }
    }

    private fun task5Rec(n1: Node<T>?, n2: Node<T>?, result: LinkedList<Int>, depth: Int): Int {
        if (n1 == null && n2 == null) return 0

        val rollOver = task5Rec(n1?.next, n2?.next, result, depth + 1)
        val d1 = n1?.value ?: 0
        val d2 = n2?.value ?: 0
        val sum = d1 + d2 + rollOver

        result.addToStart(sum % 10)

        val rem = sum / 10

        if (depth == 0 && rem != 0) result.addToStart(rem)

        return rem
    }

    fun task6(): Boolean {
        if (size <= 1) return false
        var n = root

        for (i in 0..size / 2 - 1) {
            val anotherValue = kFromLast(i + 1)
            if (n?.value != anotherValue?.value) return false

            n = n?.next
        }

        return true
    }

    private fun kFromLast(k: Int): Node<T>? {
        var runner = root
        repeat(k) {
            runner = runner?.next
        }
        var n = root
        while (runner != null) {
            n = n?.next
            runner = runner.next
        }

        return n
    }

    fun task6Stack(): Boolean {
        if (size <= 1) return false

        val values = ArrayDeque<T>()

        var n = root
        while (n != null) {
            values.add(n.value)
            n = n.next
        }
        for (i in 0..<size / 2) {
            val left = values.removeFirst()
            val right = values.removeLast()
            if (left != right) return false
        }

        return true
    }

    var leftNode: Node<T>? = null
    fun task6Rec(): Boolean {
        leftNode = root
        return recursiveCall(root)
    }

    private fun recursiveCall(right: Node<T>?): Boolean {
        if (right == null) return true

        val result = recursiveCall(right.next)
        if (!result || leftNode?.value != right.value) return false

        leftNode = leftNode?.next

        return true
    }

    fun task7(list: LinkedList<T>): Node<T>? {
        var n1 = root
        var n2 = list.root

        // Align the nodes to the same index from last
        val sizeDiff = abs(size - list.size)
        if (size > list.size) {
            n1 = runFor(n1, sizeDiff)
        } else if (list.size > size) {
            n2 = runFor(n2, sizeDiff)
        }

        while (n1 != null && n2 != null) {
            if (n1 === n2) return n1

            n1 = n1.next
            n2 = n2.next
        }


        return null
    }

    private fun runFor(node: Node<T>?, count: Int): Node<T>? {
        var n = node
        repeat(count) {
            n = n?.next
        }

        return n
    }

    fun task8(): Boolean {
        val nodes = mutableSetOf<Node<T>>()

        var n = root
        while (n != null) {
            if (nodes.contains(n)) return true
            nodes.add(n)
            n = n.next
        }

        return false
    }

    fun task8Runner(): Node<T>? {
        var runner = root?.next?.next
        var n = root?.next

        while (runner != null) {
            if (runner === n) {
                var start = root
                while (start !== n) {
                    start = start?.next
                    n = n?.next
                }
                return start
            }

            runner = runner.next?.next
            n = n?.next
        }

        return null
    }

    data class Node<T>(var value: T, var next: Node<T>? = null) {
        override fun equals(other: Any?): Boolean {
            return this === other
        }

        override fun hashCode(): Int {
            return super.hashCode()
        }
    }
}


fun main() {
    val list1 = LinkedList<Int>()
    list1.add(1)
    list1.add(2)
    list1.add(3)
    list1.add(4)
    list1.add(5)
    val last = list1.get(4)
    val first = list1.get(0)

    last?.next = first

    val result = list1.task8Runner()
    println(result?.value)

}
