package be.nielandt

import java.time.Duration
import java.time.Instant
import java.util.*

/**
 * All the possible moves on the cube.
 */
enum class Move {
    F, F_, F2, B, B_, B2, L, L_, L2, R, R_, R2, U, U_, U2, D, D_, D2;

    /**
     * Static methods for the Move object.
     */
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

        /**
         * Use the given counter, which contains digits that correspond with the amount of Moves, to generate a list of Moves.
         */
        fun combo(counter: Counter): List<Move> {
            val res = mutableListOf<Move>()
            for (i in 0 until counter.size()) {
                res.add(Move.values()[counter.digit(i)])
            }
            return res
        }
    }

    /**
     * Use this as `Move otherFace Move`, a quick way to check if the moves manipulate the same face or not.
     * Example: `Move.F otherFace Move.U_ == false`
     */
    infix fun otherFace(otherMove: Move): Boolean {
        return this.toString().substring(0, 1) != otherMove.toString().substring(0, 1)
    }

}

/**
 * Convert the color into a single digit for print purposes.
 */
fun l(c: Color): String {
    return c.name.first().toString()
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
    allCrossMoveCount.forEach { color, moves ->
        println("cross for color: ${color} in ${moves.size}: ${moves.joinToString(" ")}")
    }
}

fun doAllCrossMoveCounts(edgeModel: EdgeModel) {
    for (c: Color in Color.values()) {
        val crossMoveCount = crossMoveCount(edgeModel, c)
        println("${c} in ${crossMoveCount?.size}: ${crossMoveCount?.joinToString()}")
    }
}

fun crossMoveCount(edgeModel: EdgeModel, color: Color): List<Move>? {
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

fun allCrossMoveCount(edgeModel: EdgeModel): Map<Color, List<Move>> {
    val start = Instant.now()
    val moveCounts = mutableMapOf<Color, List<Move>>()

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
            Color.values().forEach {  color ->
               if(!moveCounts.containsKey(color)) {
                   val crossSolved = afterMoves.crossSolved(color)
                   if (crossSolved) {
                       moveCounts[color] = moves
                   }
               }
            }

            if (moveCounts.keys.size == Color.values().size) {
                println("Execution time: ${Duration.between(start, Instant.now()).toMillis() / 1000}s")
                return@allCrossMoveCount moveCounts
            }

        } while (counter.increase())
    }
    println("Execution time: ${Duration.between(start, Instant.now()).toMillis() / 1000}s")
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

