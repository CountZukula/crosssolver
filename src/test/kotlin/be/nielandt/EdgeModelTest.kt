package be.nielandt

import org.junit.Assert.assertTrue
import org.junit.Test

class EdgeModelTest {

    @Test
    fun testSingleMove() {
        val edgeModel = EdgeModel.solved()
        (0..5).forEach { color ->
            assertTrue(edgeModel.crossSolved(color))
        }
    }

    @Test
    fun testSingleMoves() {
        val edgeModel = EdgeModel.solved()
        println(edgeModel)
        val final = edgeModel.doMove(F)
        println(final)
        assertTrue(final.crossSolved(BLUE))
    }
}