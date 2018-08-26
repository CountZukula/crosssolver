package be.nielandt

import be.nielandt.counter.CounterBasic

class CrossSolverBase : CrossSolver() {
    /**
     * Solve the minimal cross for all colors.
     */
    override fun solveCrosses(edgeModel: EdgeModel): Map<Int, List<Int>> {
        val moveCounts = mutableMapOf<Int, List<Int>>()

        for (moveCount in 1..8) {
            // build a counter of moveCount big
            println("allCrossMoveCount basic doing $moveCount")
            val counter = CounterBasic(moveCount)

            // count up, each state of the counter corresponds to a combination of moves
            do {
                // execute the moves
                val afterMoves = edgeModel.doMoves(counter.counter)
                // check crosses that have not been found yet
                (0..5).forEach { color ->
                    if (!moveCounts.containsKey(color)) {
                        val crossSolved = afterMoves.crossSolved(color)
                        if (crossSolved) {
                            moveCounts[color] = counter.counter.toList()
                        }
                    }
                }

                if (moveCounts.keys.size == 6) {
                    return@solveCrosses moveCounts
                }

            } while (counter.increase())
        }
        return moveCounts
    }

}