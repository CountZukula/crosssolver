package be.nielandt

import java.time.Duration
import java.time.Instant

open abstract class CrossSolver {
    /**
     * Solve all crosses, look for a minimal set of moves for each color's cross.
     */
    abstract fun solveCrosses(edgeModel: EdgeModel): Map<Int, List<Move>>

    fun solveCrossesTimed(edgeModel: EdgeModel): Map<Int, List<Move>> {
        val now = Instant.now()
        val solveCrosses = solveCrosses(edgeModel)
        val between = Duration.between(now, Instant.now())
        println("Solving crosses took ${between.seconds}s")
        return solveCrosses
    }

    companion object {
        fun printResults(results: Map<Int, List<Move>>) {
            println("results: ")
            results.forEach { color, moveList ->
                println("> color ${colorLetter(color)}, moves (${moveList.size}) $moveList")
            }
        }
    }
}

/**
 * Let's look for symmetries.
 */
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


    // do a fixed scramble for testing purposes
    val fixedMoves = Move.parse("L L_ R R2")
    println("fixedMoves = ${fixedMoves}")


    // scramble random
    val moves = Move.random(10)
    println("Scramble: $moves")
    val scrambledModel = EdgeModel(moves)
    println(scrambledModel)

//    val baseSolve = CrossSolverBase().solveCrossesTimed(scrambledModel)
//    CrossSolver.printResults(baseSolve)

    val upgradedSolve = CrossSolverUpgraded().solveCrossesTimed(scrambledModel)
    CrossSolver.printResults(upgradedSolve)

    val upgradedSolveSkip = CrossSolverUpgradedSkip().solveCrossesTimed(scrambledModel)
    CrossSolver.printResults(upgradedSolveSkip)

//    val allCrossMoveCountUpgradedSkip = allCrossMoveCountUpgradedSkip(scrambledModel)
//    allCrossMoveCountUpgradedSkip.forEach { color, moves ->
//        println("skip upgrade cross for color: ${color} in ${moves.size}: ${moves.joinToString(" ")}")
//    }
}

