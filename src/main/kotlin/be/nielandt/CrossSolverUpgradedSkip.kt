package be.nielandt

/**
 * This solver avoids redoing edgemodel manipulations. Should be equivalent to X nested for loops.
 */
class CrossSolverUpgradedSkip : CrossSolver() {

    override fun solveCrosses(edgeModel: EdgeModel): Map<Int, List<Move>> {
        val moveCounts = mutableMapOf<Int, List<Move>>()
        for (moveCount in 1..8) {
            println("all cross move count upgrade doing $moveCount")
            // build a counter of moveCount big
            val counter = CounterSkipSameFaces(moveCount, Move.values().size)
            val edgeModelFactory = EdgeModelFactory(edgeModel, counter)

            while (edgeModelFactory.hasNext()) {
                // get the next model, using the internal counter which simply iterates over possible combinations of moves
                val next = edgeModelFactory.getNext()

                // check crosses that have not been found yet
                (0..5).forEach { color ->
                    if (!moveCounts.containsKey(color)) {
                        val crossSolved = next.crossSolved(color)
                        if (crossSolved) {
                            // what is the move combination we're looking at?
                            val moves = Move.combo(counter)
                            moveCounts[color] = moves
                        }
                    }
                }
                // break if we have found hem all
                if (moveCounts.keys.size == 6) {
                    return moveCounts
                }
            }
        }
        return moveCounts

    }
}