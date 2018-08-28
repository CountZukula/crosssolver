package be.nielandt

import be.nielandt.iterator.ValidClassMoveIterator

class CrossSolverIterator : CrossSolver() {
    /**
     * Solve the minimal cross for all colors.
     */
    override fun solveCrosses(edgeModel: EdgeModel): Map<Int, IntArray> {
        val moveCounts = mutableMapOf<Int, IntArray>()
        var iteratorCount = 0

        for (moveCount in 1..8) {
            // build a counter of moveCount big
            println("crossSolverIterator doing $moveCount")
            val iterator = ValidClassMoveIterator(moveCount)

            // count up, each state of the counter corresponds to a combination of moves
            while (iterator.hasNext()) {
                val moves = iterator.next()
                iteratorCount++
                // execute the moves
                val afterMoves = edgeModel.doMoves(moves.toList())
                // check crosses that have not been found yet
                (0..5).forEach { color ->
                    if (!moveCounts.containsKey(color)) {
                        val crossSolved = afterMoves.crossSolved(color)
                        if (crossSolved) {
                            moveCounts[color] = moves
                        }
                    }
                }
                if (moveCounts.keys.size == 6) {
                    println("iteratorCount = ${iteratorCount}")
                    return@solveCrosses moveCounts
                }
            }
        }

        println("iteratorCount = ${iteratorCount}")

        return moveCounts
    }

}

fun main(args: Array<String>) {
    val start = EdgeModel.solved().doMoves(randomMoves(20))
    val solver = CrossSolverIterator()
    val solveCrossesTimed = solver.solveCrossesTimed(start)
    solveCrossesTimed.forEach { t, u ->
        println("t $t u ${u.map { decodeMove(it) }}")
    }
}