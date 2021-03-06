package be.nielandt

import be.nielandt.counter.CounterSkip

/**
 * This solver avoids redoing edgemodel manipulations. Should be equivalent to X nested for loops.
 */
class CrossSolverUpgradedSkip : CrossSolver() {

    override fun solveCrosses(edgeModel: EdgeModel): Map<Int, IntArray> {
        val moveCounts = mutableMapOf<Int, IntArray>()
        var iteratorCount = 0
        for (moveCount in 1..8) {
            println("all cross move count upgrade doing $moveCount")
            // build a counter of moveCount big
            val counter = CounterSkip(moveCount)
            val edgeModelFactory = EdgeModelFactory(edgeModel, counter)

            while (edgeModelFactory.hasNext()) {
                // get the next model, using the internal counter which simply iterates over possible combinations of moves
                val next = edgeModelFactory.getNext()
                iteratorCount++

                // check crosses that have not been found yet
                (0..5).forEach { color ->
                    if (!moveCounts.containsKey(color)) {
                        val crossSolved = next.crossSolved(color)
                        if (crossSolved) {
                            // what is the move combination we're looking at?
                            moveCounts[color] = counter.counter.copyOf()
                        }
                    }
                }
                // break if we have found hem all
                if (moveCounts.keys.size == 6) {
                    println("iteratorCount = ${iteratorCount}")
                    return moveCounts
                }
            }
        }
        println("iteratorCount = ${iteratorCount}")
        return moveCounts

    }
}