package be.nielandt

import java.util.*

// constant values for all the moves
// these are purposely put in this order: Move.index % 6 will result in the same value for the same face
const val F = 0
const val B = 1
const val U = 2
const val D = 3
const val L = 4
const val R = 5

const val F_ = 6
const val B_ = 7
const val U_ = 8
const val D_ = 9
const val L_ = 10
const val R_ = 11

const val F2 = 12
const val B2 = 13
const val U2 = 14
const val D2 = 15
const val L2 = 16
const val R2 = 17

fun decodeMove(i: Int): String {
    return when (i) {
        F -> "F"
        B -> "B"
        U -> "U"
        D -> "D"
        L -> "L"
        R -> "R"

        F_ -> "F_"
        B_ -> "B_"
        U_ -> "U_"
        D_ -> "D_"
        L_ -> "L_"
        R_ -> "R_"

        F2 -> "F2"
        B2 -> "B2"
        U2 -> "U2"
        D2 -> "D2"
        L2 -> "L2"
        R2 -> "R2"
        else -> {
            println("i = $i")
            throw IllegalArgumentException()
        }
    }
}

fun classOf(move: Int): Int {
    return (move % 6) / 2
}

fun parseMoves(s: String): IntArray {
    return s.split(" ", ",", ";").filter { it?.length > 0 }.map { parseMove(it) }.toIntArray()
}

fun parseMove(s: String): Int {
    return when (s) {
        "F" -> F
        "B" -> B
        "U" -> U
        "D" -> D
        "L" -> L
        "R" -> R

        "F_" -> F_
        "B_" -> B_
        "U_" -> U_
        "D_" -> D_
        "L_" -> L_
        "R_" -> R_

        "F2" -> F2
        "B2" -> B2
        "U2" -> U2
        "D2" -> D2
        "L2" -> L2
        "R2" -> R2
        else -> {
            println("s = ${s}")
            throw IllegalArgumentException()
        }
    }
}

/**
 * Get a proper scramble set of moves. Disallow the obvious symmetries.
 */
fun randomMoves(amount: Int): IntArray {
    val rgen = Random()
    val result = IntArray(amount)
    for (i in 0 until result.size) {
        // create valid options for the next one
        val options = mutableListOf<Int>()
        if (i == 0) {
            // all options
            options.addAll(0..17)
        }
        // if there's two before us, we can't add a third of the same class
        else if (i > 1 && classOf(result[i - 1]) == classOf(result[i - 2])) {
            options.addAll(
                    (0..17).filter { classOf(it) != classOf(result[i - 1]) }
            )
        } else {
            // all options except the previous one
            // also disallow same-face solutions
            options.addAll(
                    (0..17)
                            // can't be the same face move
                            .filter {
                                it % 6 != result[i - 1] % 6
                            }
            )
        }
        result[i] = options[rgen.nextInt(options.size)]
    }
    return result
}

fun main(args: Array<String>) {
    printMoves(randomMoves(50))
}

fun printMoves(moves: IntArray) {
    println(moves.map { decodeMove(it) }.joinToString(","))
}


