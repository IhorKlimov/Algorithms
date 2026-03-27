package com.example.lib.algorithms

import java.util.LinkedList

private interface IStack<T : Comparable<T>> {
    fun push(value: T)
    fun pop(): T?
    fun isEmpty(): Boolean
    fun peek(): T?
}

private class MultiStack<T : Comparable<T>>(private val capacity: Int) : IStack<T> {
    val stacks = mutableListOf<Stack<T>>()

    override fun push(value: T) {
        if (stacks.isEmpty() || stacks.last().size == capacity) stacks.add(Stack())

        stacks.last().push(value)
    }

    override fun pop(): T? {
        val stack = stacks.lastOrNull()
        val value = stack?.pop()
        if (stack?.isEmpty() == true) {
            stacks.removeAt(stack.size - 1)
        }

        return value
    }

    override fun isEmpty(): Boolean = stacks.lastOrNull()?.isEmpty() == false

    override fun peek(): T? = stacks.lastOrNull()?.peek()

    fun popAt(index: Int): T? {
        val stack = stacks[index]
        val value = stack.pop()
        if (stack.isEmpty()) stacks.removeAt(index)

        return value
    }

}

class Stack<T : Comparable<T>>(trackMinValue: Boolean = true) : IStack<T> {
    private var head: Node<T>? = null
    private val minValues: Stack<T>?
    var size = 0
        private set

    init {
        if (trackMinValue) {
            minValues = Stack<T>(false)
        } else {
            minValues = null
        }
    }

    override fun push(value: T) {
        val node = Node(value)
        node.next = head
        head = node

        if (minValues != null) {
            if (minValues.isEmpty()) {
                minValues.push(value)
            } else {
                minValue()?.let {
                    if (value < it) minValues.push(value)
                }
            }
        }
        size++
    }

    override fun pop(): T? {
        val top = head
        head = top?.next

        size--

        if (minValues != null && top?.value == minValue()) minValues.pop()

        return top?.value
    }

    override fun isEmpty(): Boolean = head == null

    override fun peek(): T? = head?.value

    fun minValue(): T? = minValues?.peek()

    private data class Node<T>(val value: T, var next: Node<T>? = null)
}

class Queue<T> {
    private var head: Node<T>? = null
    var size = 0
        private set

    fun add(value: T) {
        var n = head
        val node = Node(value)

        if (head == null) {
            head = node
        } else {
            while (n?.next != null) {
                n = n.next
            }
            n?.next = node
        }
        size++
    }

    fun remove(): T? {
        val node = head
        head = node?.next
        size--
        return node?.value
    }

    fun peek(): T? = head?.value

    fun isEmpty(): Boolean = head == null

    private data class Node<T>(val value: T, var next: Node<T>? = null)
}

class MyQueue<T : Comparable<T>> : IStack<T> {
    private val leftStack = Stack<T>()
    private val rightStack = Stack<T>()

    override fun push(value: T) {
        while (!leftStack.isEmpty()) {
            val value = leftStack.pop()
            if (value != null) rightStack.push(value)
        }

        rightStack.push(value)

        while (!rightStack.isEmpty()) {
            val value = rightStack.pop()
            if (value != null) leftStack.push(value)
        }
    }

    override fun pop(): T? = leftStack.pop()

    override fun isEmpty(): Boolean = leftStack.isEmpty()

    override fun peek(): T? = leftStack.peek()

}

private class SortedStack<T : Comparable<T>> : IStack<T> {
    private val values = Stack<T>()
    private val temp = Stack<T>()

    override fun push(value: T) {
        while (values.peek().isSmaller(value)) {
            val v = values.pop()
            if (v != null) temp.push(v)
        }

        values.push(value)

        while (!temp.isEmpty()) {
            val v = temp.pop()
            if (v != null) values.push(v)
        }
    }

    override fun pop(): T? = values.pop()

    override fun isEmpty(): Boolean = values.isEmpty()

    override fun peek(): T? = values.peek()

    fun T?.isSmaller(other: T): Boolean {
        if (this == null) return false
        return this < other
    }
}

private class AnimalShelter {
    private val queue = LinkedList<Animal>()

    fun enqueue(animal: Animal) {
        queue.add(animal)
    }

    fun dequeueAny(): Animal? {
        return queue.removeFirst()
    }

    fun dequeueDog(): Dog? {
        for (i in 0..<queue.size) {
            val a = queue[i]
            if (a is Dog) {
                queue.removeAt(i)
                return a
            }
        }
        return null
    }
    fun dequeueCat(): Cat? {
        for (i in 0..<queue.size) {
            val a = queue[i]
            if (a is Cat) {
                queue.removeAt(i)
                return a
            }
        }
        return null
    }

}

private sealed  class Animal(val id: Long){
    override fun toString(): String {
        return id.toString()
    }
}
private class Cat(id: Long) : Animal(id){
    override fun toString(): String {
        return "Cat $id"
    }
}
private class Dog(id: Long) : Animal(id) {
    override fun toString(): String {
        return "Dog $id"
    }
}

fun main() {
    val list = AnimalShelter()

    list.enqueue(Cat(1))
    list.enqueue(Dog(1))
    list.enqueue(Cat(2))
    list.enqueue(Cat(3))
    list.enqueue(Dog(2))

    println(list.dequeueAny())
    println(list.dequeueCat())
    println(list.dequeueDog())
//    println(list.dequeueAny())
//    println(list.dequeueAny())
//    println(list.dequeueAny())


}