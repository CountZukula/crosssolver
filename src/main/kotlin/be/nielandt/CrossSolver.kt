package be.nielandt

import java.time.Duration
import java.time.Instant

open abstract class CrossSolver {
    /**
     * Solve all crosses, look for a minimal set of moves for each color's cross.
     */
    abstract fun solveCrosses(edgeModel: EdgeModel): Map<Int, IntArray>

    fun solveCrossesTimed(edgeModel: EdgeModel): Map<Int, IntArray> {
        val now = Instant.now()
        val solveCrosses = solveCrosses(edgeModel)
        val between = Duration.between(now, Instant.now())
        println("Solving crosses took ${between.seconds}s")
        return solveCrosses
    }

    companion object {
        fun printResults(results: Map<Int, IntArray>) {
            println("results: ")
            results.forEach { color, moveList ->
                println("> color ${colorLetter(color)}, moves (${moveList.size}) ${moveList.map { it -> decodeMove(it) }}")
            }
        }
    }
}

/**
 * Let's look for symmetries.
 *
 * - no changes: base times:                        72s, 29s, 61s
 * - changed list<int> in edgemodel to intarray:    51s, 21s, 35s
 * - new version, optimised now avoid recalc:       47s, 21s, 34s, 14s,  (401592291, 147023415, 67382002, 67381995)
 */
fun main(args: Array<String>) {

    // do a fixed scramble for testing purposes
    // noskip Move.enum 30s, skip Move.enum 32s
//    val fixedMoves = parseMoves("L_, D, U, L2, F, D, B, D, U2, D, B_, F2, D2, U_, R, D2, R_, L, B_, R")
    val moveString: String = "U2 F2 U2 D R2 F2 R2 B2 U' D2 L B L2 U2 L B' U L R B".replace('\'', '_')
    val s = "L_, D, U, L2, F, D, B, D, U2, D, B_, F2, D2, U_, R, D2, R_, L, B_, R"

    val fixedMoves = parseMoves(moveString)
    println("fixedMoves = ${fixedMoves}")
    // scramble random
    val randomMoves = randomMoves(20)

    val moves = fixedMoves
    println("Scramble: ${moves.map { decodeMove(it) }}")


    val usedModel = EdgeModel.withMoves(moves)
    println(usedModel)


    val upgradedSolve = CrossSolverUpgraded().solveCrossesTimed(usedModel)
    CrossSolver.printResults(upgradedSolve)

    val upgradedSolveSkip = CrossSolverUpgradedSkip().solveCrossesTimed(usedModel)
    CrossSolver.printResults(upgradedSolveSkip)

    val solveCrossesTimed = CrossSolverIterator().solveCrossesTimed(usedModel)
    CrossSolver.printResults(solveCrossesTimed)

    val s3 = CrossSolverIteratorUpgraded().solveCrossesTimed(usedModel)
    CrossSolver.printResults(s3)

//    val allCrossMoveCountUpgradedSkip = allCrossMoveCountUpgradedSkip(scrambledModel)
//    allCrossMoveCountUpgradedSkip.forEach { color, moves ->
//        println("skip upgrade cross for color: ${color} in ${moves.size}: ${moves.joinToString(" ")}")
//    }
//    val baseSolve = CrossSolverBase().solveCrossesTimed(usedModel)
//    CrossSolver.printResults(baseSolve)
}

