package com.example.lib.algorithms

import java.util.TreeMap


fun main() {
    println(Solution().hIndex(intArrayOf(1, 4, 4, 4)))
}
class Solution {
    fun hIndex(citations: IntArray): Int {
        val votes = IntArray(citations.size + 1) // include an extra element for a 0 value

        val possibleValues = mutableSetOf<Int>()

        for (i in 0..<citations.size) {
            var c = citations[i]
            possibleValues.add(c)
            // trim to a max possible value as the value can't be larger
            // than the size of the citations array
            if (c > citations.size) {
                c = citations.size
            }
            votes[c]++
        }

        var maxPossible = 0
        for (h in votes.size - 1 downTo 0) {
            val v = votes[h]
            if (v >= h && possibleValues.contains(h)) { // check if this h value has enough votes
                maxPossible = h
                break
            } else { // else, pass down its votes to a smaller h
                if (h == 0) break
                votes[h - 1]+= v
            }
        }

        return maxPossible
    }
}