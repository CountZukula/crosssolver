package be.nielandt

import java.util.*

/**
 * All the possible moves on the cube.
 */
enum class Move {
    F, F_, F2, B, B_, B2, L, L_, L2, R, R_, R2, U, U_, U2, D, D_, D2;

    companion object {
        fun random(amount: Int): List<Move> {
            val rgen = Random()
            val result = mutableListOf<Move>()
            for(i in 0 until amount) {
                result.add(Move.values()[rgen.nextInt(Move.values().size)])
            }
            return result
        }
    }
}

/**
 * Six colors
 */
const val WHITE: Short = 0
const val YELLOW: Short = 1
const val ORANGE: Short = 2
const val RED: Short = 3
const val GREEN: Short = 4
const val BLUE: Short = 5
val COLOR_LETTER = arrayOf("W", "Y", "O", "R", "G", "B")

fun l(colorIndex: Short): String {
    return COLOR_LETTER[colorIndex.toInt()]
}

fun main(args: Array<String>) {
    val edgeModel = EdgeModel()
    println(edgeModel)
    println("edgeModel.whiteCrossSolved() = ${edgeModel.whiteCrossSolved()}")

    val doMove = edgeModel.doMove(Move.D)
    println(doMove)
    println("doMove.whiteCrossSolved() = ${doMove.whiteCrossSolved()}")

    val message = EdgeModel().doMoves(Move.R, Move.U, Move.R_)
    println(message)
    println("message.whiteCrossSolved() = ${message.whiteCrossSolved()}")

    val doMoves = EdgeModel().doMoves(Move.random(15))
    println("random 15 moves = ${doMoves}")
    println("doMoves.whiteCrossSolved() = ${doMoves.whiteCrossSolved()}")
    
}

const val GO = 3
const val GW = 0
const val GR = 1
const val GY = 2

const val WG = 18
const val WO = 19
const val WB = 16
const val WR = 17

const val YG = 20
const val YO = 23
const val YB = 22
const val YR = 21

const val RG = 7
const val RW = 4
const val RB = 5
const val RY = 6

const val BR = 11
const val BW = 8
const val BO = 9
const val BY = 10

const val OB = 15
const val OW = 12
const val OG = 13
const val OY = 14
