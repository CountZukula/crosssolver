package be.nielandt

import java.time.Duration
import java.time.Instant

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

/**
 * For a single color, go 8 deep and try and find the minimal amount of moves to solve that cross.
 */
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

/**
 * Solve the minimal cross for all colors.
 */
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

