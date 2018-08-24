package be.nielandt

import java.time.Duration
import java.time.Instant
import java.util.*

/**
 * All the possible moves on the cube.
 */
enum class Move {
    F, F_, F2, B, B_, B2, L, L_, L2, R, R_, R2, U, U_, U2, D, D_, D2;

    companion object {
        /**
         * Make a random set of moves that makes sense.
         */
        fun random(amount: Int): List<Move> {
            val rgen = Random()
            val result = mutableListOf<Move>()
            while (result.size < amount) {
                val randomMove = Move.values()[rgen.nextInt(Move.values().size)]
                // check if it makes sense?
                if (result.isNotEmpty() && result.last() otherFace randomMove)
                    result.add(randomMove)
                else if (result.isEmpty())
                    result.add(randomMove)
            }
            return result
        }

        fun combo(counter: Counter): List<Move> {
            val res = mutableListOf<Move>()
            for (i in 0 until counter.size()) {
                res.add(Move.values()[counter.digit(i)])
            }
            return res
        }
    }

    infix fun otherFace(otherMove: Move): Boolean {
        return this.toString().substring(0, 1) != otherMove.toString().substring(0, 1)
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

//    val u2 = Move.U2
//    val doMove = edgeModel.doMove(Move.U2)
//    println(u2)
//    println(doMove)
//    println("doMove.whiteCrossSolved() = ${doMove.whiteCrossSolved()}")

//    val message = EdgeModel().doMoves(Move.R, Move.U, Move.R_)
//    println(message)
//    println("message.whiteCrossSolved() = ${message.whiteCrossSolved()}")
//
//    val doMoves = EdgeModel().doMoves(Move.random(15))
//    println("random 15 moves = ${doMoves}")
//    println("doMoves.whiteCrossSolved() = ${doMoves.whiteCrossSolved()}")


//    val begin = Instant.now()
//    var i: Long = 0
//    while (Duration.between(begin, Instant.now()) < Duration.ofMinutes(1)) {
//        EdgeModel().doMoves(Move.random(15))
//        i++
//    }
//    println("i = ${i}")
//
//    val counter = Counter(4, 3)
//    while (counter.increase()) {
//        println("counter = ${counter}")
//    }


    // make a beginning model, then start doing crazy shit
    val moves = Move.random(20)
    println("Scramble: $moves")
    val scrambledModel = EdgeModel(moves)
    println(scrambledModel)

//    doAllCrossMoveCounts(scrambledModel)
    val allCrossMoveCount = allCrossMoveCount(scrambledModel)
    allCrossMoveCount.forEachIndexed { index, list ->
        println("cross for color: ${colorName(index.toShort())} in ${list?.size}: ${list?.joinToString(" ")}")
    }
}

fun colorName(color: Short): String {
    return when (color) {
        WHITE -> "white"
        YELLOW -> "yellow"
        RED -> "red"
        BLUE -> "blue"
        GREEN -> "green"
        ORANGE -> "orange"
        else -> {
            "?"
        }
    }
}

fun doAllCrossMoveCounts(edgeModel: EdgeModel) {
    for (i in 0 until 6) {
        val crossMoveCount = crossMoveCount(edgeModel, i.toShort())
        println("${colorName(i.toShort())} in ${crossMoveCount?.size}: ${crossMoveCount?.joinToString()}")
    }
}

fun crossMoveCount(edgeModel: EdgeModel, color: Short): List<Move>? {
    val moveCounts = Array<List<Move>?>(6) { null }

    for (moveCount in 1..8) {
        // build a counter of moveCount big
        val counter = Counter(moveCount, Move.values().size)

        // count up, each state of the counter corresponds to a combination of moves
        do {
            // what is the move combination we're looking at?
            val moves = Move.combo(counter)
            // execute the moves
            val afterMoves = edgeModel.doMoves(moves)
            val crossSolved = afterMoves.crossSolved(color)
            if (crossSolved) {
                return@crossMoveCount moves
            }

        } while (counter.increase())
    }
    return null
}

fun allCrossMoveCount(edgeModel: EdgeModel): Array<List<Move>?> {
    val start = Instant.now()
    val moveCounts = Array<List<Move>?>(6) { null }

    for (moveCount in 1..8) {
        // build a counter of moveCount big
        val counter = Counter(moveCount, Move.values().size)

        // count up, each state of the counter corresponds to a combination of moves
        do {
            // what is the move combination we're looking at?
            val moves = Move.combo(counter)
            // execute the moves
            val afterMoves = edgeModel.doMoves(moves)
            // check crosses that have not been found yet
            moveCounts.forEachIndexed { index, list ->
                if (list == null) {
                    val crossSolved = afterMoves.crossSolved(index.toShort())
                    if (crossSolved) {
                        moveCounts[index] = moves
                    }
                }
            }

            if (moveCounts.all { it != null }) {
                println("Execution time: ${Duration.between(start, Instant.now()).toSeconds()}s")
                return@allCrossMoveCount moveCounts
            }

        } while (counter.increase())
    }
    println("Execution time: ${Duration.between(start, Instant.now()).toSeconds()}s")
    return moveCounts
}

/**
 * Counter for X digits of a given base.
 */
class Counter(size: Int, val base: Int = 10) {

    // start the counter at [0,0,...0]
    private var counter: Array<Int> = Array(size) { 0 }

    fun increase(): Boolean {
        if (atMax()) {
            return false
        }
        for (i in this.counter.size - 1 downTo 0) {
            this.counter[i]++
            if (this.counter[i] == base)
                this.counter[i] = 0
            else
                break
        }
        return true
    }

    fun size(): Int {
        return this.counter.size
    }

    fun digit(index: Int): Int {
        return counter[index]
    }

    override fun toString(): String = this.counter.joinToString(".")

    fun atMax(): Boolean {
        return counter.all {
            it == this.base - 1
        }
    }
}

