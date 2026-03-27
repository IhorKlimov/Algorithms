package com.example.lib.algorithms



private const val BRACKET_1_OPEN = '('
private const val BRACKET_1_CLOSE = ')'
private const val BRACKET_2_OPEN = '['
private const val BRACKET_2_CLOSE = ']'
private const val BRACKET_3_OPEN = '{'
private const val BRACKET_3_CLOSE = '}'

private fun areBracketsBalanced(input: String): Boolean {
    val queue = ArrayDeque<Char>()

    for (ch in input) {
        if (isOpenBracket(ch)) queue.addLast(ch)
        if (isCloseBracket(ch)) {
            val last = queue.lastOrNull() ?: return false
            if (isCloseBracketFor(last, ch)) {
                queue.removeLast()
            } else {
                return false
            }
        }
    }
    return queue.isEmpty()
}

private fun isOpenBracket(char: Char): Boolean {
    return when (char) {
        BRACKET_1_OPEN, BRACKET_2_OPEN, BRACKET_3_OPEN -> true
        else -> false
    }
}

private fun isCloseBracket(char: Char): Boolean {
    return when (char) {
        BRACKET_1_CLOSE, BRACKET_2_CLOSE, BRACKET_3_CLOSE -> true
        else -> false
    }
}

private fun isCloseBracketFor(openBracket: Char, closeBracket: Char): Boolean {
    return when (openBracket) {
        BRACKET_1_OPEN -> closeBracket == BRACKET_1_CLOSE
        BRACKET_2_OPEN -> closeBracket == BRACKET_2_CLOSE
        BRACKET_3_OPEN -> closeBracket == BRACKET_3_CLOSE
        else -> false
    }
}

fun main() {
    val m = mapOf<Int, Int>()
   val r =  m.entries.sortedWith { e1, e2 ->
       val valueCompare = e1.value.compareTo(e2.value)
       if (valueCompare != 0) valueCompare
       else e1.key.compareTo(e2.key)
   }

    val l = listOf(1, 2, 3).sortedWith { i, i1 -> i1.compareTo(i)  }
    println(l)

    val result = arrayOf<Array<Int>>()
    println(areBracketsBalanced("{[()]}"))
    println(areBracketsBalanced("{"))
    println(areBracketsBalanced("}"))
    println(areBracketsBalanced("{[(])}"))
    println(areBracketsBalanced("{{[[(())]]}}"))
}