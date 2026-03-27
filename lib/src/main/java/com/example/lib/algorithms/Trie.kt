package com.example.lib.algorithms

import kotlin.text.iterator


class Trie {
    val root = Node(isRoot = true)

    fun addWord(word: String) {
        var currentNode = root

        root@ for (ch in word.lowercase()) {
            for (n in currentNode.children) {
                if (!n.isEnd && !n.isRoot && n.value == ch) {
                    currentNode = n
                    continue@root
                }
            }
            val node = Node(value = ch)
            currentNode.children.add(node)
            currentNode = node
        }
        currentNode.children.add(Node(isEnd = true))
    }

    fun contains(word: String): Boolean {
        var currentNode = root

        root@ for (ch in word.lowercase()) {
            for (n in currentNode.children) {
                if (!n.isEnd && !n.isRoot && n.value == ch) {
                    currentNode = n
                    continue@root
                }
            }
            return false
        }
        return currentNode.children.any { it.isEnd }
    }

    fun getAutocomplete(query: String): List<String> {
        val result = mutableListOf<String>()
        val builder = StringBuilder()
        var currentNode = root

        root@ for (ch in query.lowercase()) {
            for (n in currentNode.children) {
                if (!n.isEnd && !n.isRoot && n.value == ch) {
                    builder.append(ch)
                    currentNode = n
                    continue@root
                }
            }

            return listOf()
        }

        getAllStrings(currentNode, builder.toString(), result)

        return result
    }

    private fun getAllStrings(node: Node, prefix: String, output: MutableList<String>) {
        for (child in node.children) {
            if (child.isEnd) {
                output.add(prefix)
            } else {
                getAllStrings(child, "${prefix}${child.value ?: ""}", output)
            }
        }
    }

    class Node(
        val isRoot: Boolean = false,
        val isEnd: Boolean = false,
        val value: Char? = null,
        val children: MutableSet<Node> = mutableSetOf()
    )
}

fun main() {
    val t = Trie()
    println(t.contains("hey"))
    t.addWord("hey")
    t.addWord("hey")
    t.addWord("heresy")
    t.addWord("he")
    t.addWord("hero")

    val suggestions = t.getAutocomplete("her")
    suggestions.forEach {
        println(it)
    }
}