package com.example.lib.algorithms

import kotlin.text.iterator

fun main() {
    println(task19("melon", "onwef"))
}

private fun printMatrix(matrix: Array<IntArray>) {
    for (r in 0..<matrix.size) {
        for (c in 0..<matrix[r].size) {
            val d = matrix[r][c]
            val s = d.toString().padEnd(2)
            print("$s ")
        }
        println()
    }
}


private fun task11(s: String): Boolean {
    if (s.length <= 1) return true

    val set = hashSetOf<Char>()
    for (c in s) {
        val wasNew = set.add(c)
        if (!wasNew) return false
    }

    return true
}

private fun task11Second(s: String): Boolean {
    if (s.length <= 1) return true

    val arr = s.toCharArray().apply {
        sort()
    }

    for (i in 0..<arr.size - 1) {
        if (arr[i] == arr[i + 1]) return false
    }

    return true
}

private fun task12(s1: String, s2: String): Boolean {
    if (s1.length != s2.length) return false

    val map = mutableMapOf<Char, Int>()

    for (c in s2) {
        val current = map[c]
        if (current == null) {
            map[c] = 1
        } else {
            map[c] = current + 1
        }
    }

    for (c in s1) {
        val numOfChars = map[c]
        if (numOfChars == null || numOfChars == 0) {
            return false
        }
        map[c] = numOfChars - 1
    }
    return true
}

private fun task13(chars: CharArray, l: Int) {
    var target = chars.size - 1
    for (i in l - 1 downTo 0) {
        val c = chars[i]
        if (c == ' ') {
            chars[target--] = '0'
            chars[target--] = '2'
            chars[target--] = '%'
        } else {
            chars[target--] = c
        }
    }
}

private fun task14(chars: CharArray): Boolean {
    val map = mutableMapOf<Char, Int>()
    var numOfLetters = 0
    for (c in chars) {
        if (c.isLetter()) {
            numOfLetters++
            val lc = c.lowercaseChar()
            val current = map[lc] ?: 0
            map[lc] = current + 1
        }
    }

    val isEven = numOfLetters % 2 == 0
    var foundOddCount = false
    for ((c, count) in map.entries) {
        if (count % 2 != 0) {
            if (isEven || foundOddCount) return false
            foundOddCount = true // mark found odd count
        }
    }

    return true
}

private fun task14Second(chars: CharArray): Boolean {
    val arr = IntArray(128)
    var numOfLetters = 0
    for (c in chars) {
        if (c.isLetter()) {
            numOfLetters++
            val idx = c.lowercaseChar() - 'a'
            arr[idx]++
        }
    }

    val isEven = numOfLetters % 2 == 0
    var foundOddCount = false
    for (count in arr) {
        if (count % 2 != 0) {
            if (isEven || foundOddCount) return false
            foundOddCount = true // mark found odd count
        }
    }

    return true
}

private fun task15(original: String, result: String): Boolean {
    val resultLength = result.length
    val originalLength = original.length
    if (resultLength < originalLength - 1 || resultLength > originalLength + 1) return false

    val letters = IntArray(128)
    for (c in original) {
        val index = c - 'a'
        letters[index]++
    }

    val neededNumOfCommon =
        if (resultLength == originalLength || resultLength == originalLength - 1) {
            originalLength - 1
        } else {
            originalLength
        }

    var numOfCommon = 0

    for (c in result) {
        val index = c - 'a'
        if (letters[index] > 0) {
            letters[index]--
            numOfCommon++
        }
    }

    return numOfCommon == neededNumOfCommon
}

private fun task16(input: String): String {
    val builder = StringBuilder()

    var currentChar: Char? = null
    var currentCount = 0
    for (i in 0..<input.length) {
        val c = input[i]
        if (currentChar == c) {
            currentCount++
        } else {
            currentChar = c
            if (i != 0) {
                builder.append(currentCount)
            }
            currentCount = 1
            builder.append(c)
        }

        if (i == input.length - 1) {
            builder.append(currentCount)
        }
    }

    return if (builder.length < input.length) builder.toString() else input
}

private fun task17(matrix: Array<IntArray>) {
    if (matrix.isEmpty() || matrix.size != matrix[0].size) return

    val size = matrix.size
    val result = Array(size) { IntArray(size) }
    for (r in 0..<size) {
        for (c in 0..<size) {
            val row = c
            val col = size - 1 - r
            result[row][col] = matrix[r][c]
        }
    }

    println()
    printMatrix(result)
}

private fun task17Second(matrix: Array<IntArray>) {
    if (matrix.isEmpty() || matrix.size != matrix[0].size) return

    val size = matrix.size
    for (r in 0..<size / 2) {
        val end = size - 1 - r
        for (c in r..<end) {
            var temp = matrix[r][c]
            var row = r
            var col = c
            repeat(4) {
                val rowDestination = col
                val colDestination = size - 1 - row
                val t = matrix[rowDestination][colDestination]
                matrix[rowDestination][colDestination] = temp

                row = rowDestination
                col = colDestination
                temp = t
            }
        }
    }
}

private fun task18(matrix: Array<IntArray>) {
    if (matrix.isEmpty() || matrix[0].isEmpty()) return

    val width = matrix[0].size
    val result = Array(matrix.size) { IntArray(width) }

    for (r in 0..<matrix.size) {
        for (c in 0..<width) {
            result[r][c] = matrix[r][c]
        }
    }

    for (row in 0..<matrix.size) {
        for (col in 0..<width) {
            if (matrix[row][col] == 0) {
                // row
                for (c in 0..<width) {
                    result[row][c] = 0
                }

                // col
                for (r in 0..<matrix.size) {
                    result[r][col] = 0
                }
            }
        }
    }

    println()
    printMatrix(result)
}

private fun task18Second(matrix: Array<IntArray>) {
    if (matrix.isEmpty() || matrix[0].isEmpty()) return

    val width = matrix[0].size

    for (row in 0..<matrix.size) {
        for (col in 0..<width) {
            if (matrix[row][col] == 0) {
                // row
                for (c in 0..<width) {
                    if (matrix[row][c] != 0) {
                        matrix[row][c] = Int.MIN_VALUE
                    }
                }

                // col
                for (r in 0..<matrix.size) {
                    if (matrix[r][col] != 0) {
                        matrix[r][col] = Int.MIN_VALUE
                    }
                }
            }
        }
    }

    for (row in 0..<matrix.size) {
        for (col in 0..<width) {
            if (matrix[row][col] == Int.MIN_VALUE) {
                matrix[row][col] = 0
            }
        }
    }
}

private fun task19(s1: String, s2: String): Boolean {
    if (s1.length != s2.length || s1.isEmpty()) return false

    var s1Index = 0
    val s2IndexInitial = s2.indexOf(s1[s1Index])
    var s2Index = s2IndexInitial

    while (s1Index < s1.length) {
        if (s2Index == -1) return false
        if (s2[s2Index] == s1[s1Index]) { // letters matched, go next
            s1Index++
            s2Index++
            s2Index %= s2.length
            if (s2Index == s2IndexInitial && s1Index < s1.length) return false // made a full circle
        } else {
            s2Index = s2.indexOf(s1[0], s2Index) // find the next occurrence of the first letter
            s1Index = 0
        }
    }

    return true
}