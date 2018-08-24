package be.nielandt

import org.junit.Test

import org.junit.Assert.*

class EdgeModelTest {

    @Test
    fun singleMoveAlwaysOneSolvedCross() {
        // try each move
        Move.values().forEach { move ->
            val doMove = EdgeModel().doMove(move)
            val count = (0 until 6).map { color ->
                val crossSolved = doMove.crossSolved(color.toShort())
                crossSolved
            }.count { it }
            assertEquals(1, count)
        }
    }

    @Test
    fun findAtLeastOneCombo() {
        val white = WHITE
        val doMoves = EdgeModel().doMoves(Move.random(20))
        val crossMoveCount = crossMoveCount(doMoves, WHITE)
        assertNotNull(crossMoveCount)
        assertTrue(crossMoveCount!!.size<9)
        println(doMoves)
        println("crossMoveCount = $crossMoveCount")
    }
}