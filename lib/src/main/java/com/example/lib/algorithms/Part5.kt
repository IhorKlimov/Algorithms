package com.example.lib.algorithms

import kotlin.math.absoluteValue
import kotlin.math.floor
import kotlin.math.ln
import kotlin.math.pow

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

fun main() {
    println("demoInt() = ${binaryToStringFloat2(0.75)}")
}

private fun printBinary(i: Int) {
    println(Integer.toBinaryString(i).padStart(32, '0').chunked(4).joinToString(separator = " "))
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