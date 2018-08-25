package be.nielandt

import java.time.Duration
import java.time.Instant

open abstract class CrossSolver {
    /**
     * Solve all crosses, look for a minimal set of moves for each color's cross.
     */
    abstract fun solveCrosses(edgeModel: EdgeModel): Map<Int, List<Int>>

    fun solveCrossesTimed(edgeModel: EdgeModel): Map<Int, List<Int>> {
        val now = Instant.now()
        val solveCrosses = solveCrosses(edgeModel)
        val between = Duration.between(now, Instant.now())
        println("Solving crosses took ${between.seconds}s")
        return solveCrosses
    }

    companion object {
        fun printResults(results: Map<Int, List<Int>>) {
            println("results: ")
            results.forEach { color, moveList ->
                println("> color ${colorLetter(color)}, moves (${moveList.size}) ${moveList.map { it -> decodeMove(it) }}")
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
    // noskip Move.enum 30s, skip Move.enum 32s
    val fixedMoves = parseMoves("L_, D, U, L2, F, D, B, D, U2, D, B_, F2, D2, U_, R, D2, R_, L, B_, R")
    println("fixedMoves = ${fixedMoves}")
    // scramble random
    val randomMoves = randomMoves(20)

    val moves = fixedMoves
    println("Scramble: ${moves.map { decodeMove(it) }}")


    val usedModel = EdgeModel(moves)
    println(usedModel)

//    val baseSolve = CrossSolverBase().solveCrossesTimed(scrambledModel)
//    CrossSolver.printResults(baseSolve)

    val upgradedSolve = CrossSolverUpgraded().solveCrossesTimed(usedModel)
    CrossSolver.printResults(upgradedSolve)

    val upgradedSolveSkip = CrossSolverUpgradedSkip().solveCrossesTimed(usedModel)
    CrossSolver.printResults(upgradedSolveSkip)

//    val allCrossMoveCountUpgradedSkip = allCrossMoveCountUpgradedSkip(scrambledModel)
//    allCrossMoveCountUpgradedSkip.forEach { color, moves ->
//        println("skip upgrade cross for color: ${color} in ${moves.size}: ${moves.joinToString(" ")}")
//    }
}

