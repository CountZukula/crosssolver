package be.nielandt

import org.junit.Assert.assertEquals
import org.junit.Test

class EdgeModelTest {

    @Test
    fun singleMoveAlwaysOneSolvedCross() {
        // try each move
        Move.values().forEach { move ->
            val doMove = EdgeModel().doMove(move)
            val count = (0..5).map { color ->
                val crossSolved = doMove.crossSolved(color)
                crossSolved
            }.count { it }
            assertEquals(1, count)
        }
    }

}