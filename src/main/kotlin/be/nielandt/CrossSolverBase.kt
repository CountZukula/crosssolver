package be.nielandt

class CrossSolverBase : CrossSolver() {
    /**
     * Solve the minimal cross for all colors.
     */
    override fun solveCrosses(edgeModel: EdgeModel): Map<Int, List<Move>> {
        val moveCounts = mutableMapOf<Int, List<Move>>()

        for (moveCount in 1..8) {
            // build a counter of moveCount big
            println("allCrossMoveCount basic doing $moveCount")
            val counter = Counter(moveCount, Move.values().size)

            // count up, each state of the counter corresponds to a combination of moves
            do {
                // what is the move combination we're looking at?
                val moves = Move.combo(counter)
                // execute the moves
                val afterMoves = edgeModel.doMoves(moves)
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
                    return@solveCrosses moveCounts
                }

            } while (counter.increase())
        }
        return moveCounts
    }

}