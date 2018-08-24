package be.nielandt

import org.junit.Assert.*
import org.junit.Test

class EdgeModelTest {

    @Test
    fun singleMoveAlwaysOneSolvedCross() {
        // try each move
        Move.values().forEach { move ->
            val doMove = EdgeModel().doMove(move)
            val count = Color.values().map { color ->
                val crossSolved = doMove.crossSolved(color)
                crossSolved
            }.count { it }
            assertEquals(1, count)
        }
    }

    @Test
    fun findAtLeastOneCombo() {
        val white = Color.WHITE
        val doMoves = EdgeModel().doMoves(Move.random(20))
        val crossMoveCount = crossMoveCount(doMoves, Color.WHITE)
        assertNotNull(crossMoveCount)
        assertTrue(crossMoveCount!!.size<9)
        println(doMoves)
        println("crossMoveCount = $crossMoveCount")
    }
}