package com.example.lib.algorithms

import kotlin.math.absoluteValue
import kotlin.math.floor
import kotlin.math.ln
import kotlin.math.pow
import kotlin.random.Random

// Task 1
private fun insertion(n: Int, m: Int, i: Int, j: Int): Int {
    val maskLeftSize = (-1) shl (j + 1)
    val maskRightSide = (-1) ushr (32 - i)
    val mask = maskLeftSize or maskRightSide

    val cleared = n and mask
    val target = m shl i
    return cleared or target
}

// Task 2 IEEE-754
/*  Float:
    1 bit for sign
    23 bits for significant
    8 bits for exponent

    Double:
    1 bit for sign
    52 bits for significant
    11 bits for exponent
    Exponent has a 127 bias (need to subtract 127 to get a real value)
*/
private fun binaryToStringFloat(value: Double): String {
    val numOfBits = 32
    val numOfExponentBits = 8
    val numOfSignificantBits = numOfBits - 1 - numOfExponentBits

    val n = value.absoluteValue
    val log = log2(n)
    println("log = $log")

    val exponent = if (n == 0.0) {
        -127
    } else if (log < 0.0) {
        floor(log).toInt()
    } else {
        log.toInt()
    }
    val adjustedExponent = exponent + 127
    println("exponent = $exponent")
    println("adjustedExponent = $adjustedExponent")

    val significantRemainder = if (n == 0.0) 0.0 else n - 2.toDouble().pow(exponent.toDouble())
    println("significantRemainder = $significantRemainder")

    val result = StringBuilder(numOfBits)

    // sign bit
    result.append(if (value < 0) "1" else "0")
    result.append(" ")

    // exponent bits
    var exponentRemainder = adjustedExponent
    for (p in (numOfExponentBits - 1) downTo 0) {
        if (exponentRemainder == 0) {
            result.append("0")
            continue
        }

        val pow = 2.0.pow(p).toInt()
        val d = exponentRemainder / pow
        if (d != 0) {
            exponentRemainder -= pow
        }
        result.append(if (d != 0) "1" else "0")
    }

    result.append(" ")

    // significant bits
    var significantRemainderDivided = significantRemainder / 2.toDouble().pow(exponent.toDouble())
    for (p in 1..numOfSignificantBits) {
        if (significantRemainderDivided == 0.0) {
            result.append("0")
            continue
        }

        val pow = 2.0.pow(-p)
        val rem = (significantRemainderDivided / pow).toInt()
        if (rem != 0) {
            significantRemainderDivided -= pow
        }
        result.append(if (rem != 0) "1" else "0")
    }

    if (exponentRemainder != 0 || significantRemainderDivided != 0.0) return "ERROR"

    return result.toString()
}

// Task 2 version 2
private fun binaryToStringFloat2(value: Double): String {
    if (value !in 0.0..1.0) return "ERROR"

    val numOfBits = 32
    val result = StringBuilder(numOfBits)

    result.append("0.")

    var remainder = value
    for (p in 1..numOfBits - 2) {
        val pow = 2.0.pow(-p)
        if (remainder >= pow) {
            result.append("1")
            remainder -= pow
        } else {
            result.append("0")
        }
    }

    if (remainder != 0.0) return "ERROR"

    return result.toString()
}

// Task 3
private fun flipBit(number: Int): Int {
    var maxLength = 0
    var currLength = 0
    var prevLength = 0
    var hasPrecedingZero = false

    var remainder = number
    for (p in 30 downTo 0) {
        val pow = 2.0.pow(p).toInt()

        if (remainder >= pow) {
            remainder -= pow
            currLength++
            val extra = if (hasPrecedingZero) 1 else 0
            maxLength = maxLength.coerceAtLeast(currLength + prevLength + extra)
        } else {
            hasPrecedingZero = true
            prevLength = currLength
            currLength = 0
            maxLength = maxLength.coerceAtLeast(prevLength + 1)
        }
    }

    return maxLength
}

// Task 4
fun nextNumber(number: Int): Pair<Int, Int> {
    if (number == 0) return Pair(0, 0)

    return Pair(getPrev(number), getNext(number))
}

private fun getNext(number: Int): Int {
    for (bitIndex in 0..29) {
        if (!isZero(bitIndex, number) && isZero(bitIndex + 1, number)) {
            var result = setBit(bitIndex + 1, number)
            var copy = number
            var mask = -1 shl bitIndex + 1
            result = result and mask
            mask = mask.inv()
            mask = clearBit(bitIndex, mask)
            copy = copy and mask

            var new = 0
            while (copy != 0) {
                new = new shl 1 or 1
                while (isZero(0, copy)) {
                    copy = copy shr 1
                }
                copy = copy shr 1
            }
            return result or new
        }
    }
    return number
}

private fun getPrev(number: Int): Int {
    for (bitIndex in 1..30) {
        if (!isZero(bitIndex, number) && isZero(bitIndex - 1, number)) {
            var result = setBit(bitIndex - 1, number)
            result = clearBit(bitIndex, result)

            if (bitIndex - 2 < 0) return result

            var mask = -1 shl bitIndex - 1
            result = result and mask
            mask = mask.inv()
            var remainder = number and mask

            var new = 0
            while (remainder != 0) {
                new = new ushr 1 or (1 shl bitIndex - 2)
                while (isZero(0, remainder)) {
                    remainder = remainder ushr 1
                }
                remainder = remainder ushr 1
            }

            return result or new
        }
    }
    return number
}

private fun isZero(bitIndex: Int, number: Int): Boolean {
    val mask = 1 shl bitIndex
    return number and mask == 0
}

private fun clearBit(bitIndex: Int, number: Int): Int {
    val mask = (1 shl bitIndex).inv()
    return number and mask
}

private fun setBit(bitIndex: Int, number: Int): Int {
    val mask = 1 shl bitIndex
    return number or mask
}

// Task 6
private fun conversion(from: Int, to: Int): Int {
    if (from == to) return 0

    var c = from xor to
    var result = 0

    while (c != 0) {
        result += 1 and c
        c = c ushr 1
    }

    return result
}

private fun conversion2(from: Int, to: Int): Int {
    if (from == to) return 0

    var c = from xor to
    var result = 0

    while (c != 0) {
        c = c and (c - 1)
        result++
    }

    return result
}

// Task 7
private fun pairwiseSwap(number: Int): Int {
    var result = number

    for (bitIndex in 0..31 step 2) {
        val isRightZero = isZero(bitIndex, number)
        val isLeftZero = isZero(bitIndex + 1, number)
        result = if (isRightZero) clearBit(bitIndex + 1, result) else setBit(bitIndex + 1, result)
        result = if (isLeftZero) clearBit(bitIndex, result) else setBit(bitIndex, result)
    }

    return result
}

private fun pairwiseSwap2(number: Int): Int {
    var result = 0

    val first = number ushr 1
    val second = number shl 1
    var bit = 1

    var current = first

    while (bit > 0) {
        result = result or (bit and current)
        current = if (current == first) second else first
        bit *= 2
    }

    return result
}

// Task 8
private fun drawLine(screen: ByteArray, width: Int, x1: Int, x2: Int, y: Int) {
    val offset = width * y

    for (bitIndex in x1..x2) {
        val position = offset + bitIndex
        var byte = screen[position / 8].toInt()
        val indexInByte = position % 8
        val b = 8 - indexInByte - 1
        byte = (1 shl b) or byte
        screen[position / 8] = byte.toByte()
    }
}

fun main() {
    val input = byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0)
    drawLine(input, 16, 2, 10, 1)
    input.toList().chunked(2).reversed().forEach {
        println("${getBinary(it.first())} ${getBinary(it[1])}")
    }
}

private fun countNumOfOnes(number: Int): Int {
    var result = 0
    var n = number

    while (n != 0) {
        if (n and 1 != 0) result++
        n = n ushr 1
    }

    return result
}

private fun printBinary(i: Int) {
    println(Integer.toBinaryString(i).padStart(32, '0').chunked(4).joinToString(separator = " "))
}

private fun getBinary(i: Byte): String {
    val result = StringBuilder()
    val input = i.toInt()

    for (shift in 7 downTo 0) {
        val b = input shr shift and 1
        result.append(b)
    }

    return result.toString()
}

private fun printBinary(i: Float) {
    println(
        Integer.toBinaryString(java.lang.Float.floatToIntBits(i)).padStart(32, '0').chunked(4)
            .joinToString(separator = " ")
    )
}

private fun log2(n: Double): Double {
    return ln(n) / ln(2.0)
}