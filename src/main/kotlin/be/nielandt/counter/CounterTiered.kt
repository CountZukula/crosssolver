package be.nielandt.counter

import be.nielandt.decodeMove

/**
 * Try a multi-tier counter.
 * 1) tier1 : axes / move classes (FB/UD/RL) -> three classes, but multiple sizes (1 or 2) possible
 * 2) tier2: each block of class (22, 11, 2, 11, 0, ...) needs to be expanded into the full range of related moves
 */
class CounterTiered : Counter(8) {

    private val moveIterator: Iterator<IntArray> = listOf<IntArray>().iterator()

    override fun increase(): Boolean {
        if (!this.moveIterator.hasNext())
            return false
        this.counter = this.moveIterator.next()
        return true
    }

    override fun atMax(): Boolean {
        return !this.moveIterator.hasNext()
    }

    private class MoveIterator(val classes: List<IntArray>) : Iterator<IntArray> {

        var currentClassIndex = 0
        lateinit var currentMoves: Iterator<IntArray>


        init {
            // set up the first class
            doClass(currentClassIndex)
        }

        private fun doClass(index: Int) {
            val fillIn = fillIn(0, classes[index], IntArray(classes.size) { 0 })
            currentMoves = fillIn.toList().iterator()
        }

        override fun next(): IntArray {
            val next = currentMoves.next()
            if (!currentMoves.hasNext()) {
                nextClass()
            }
            return next
        }

        private fun nextClass() {
            if (currentClassIndex < classes.size) {
                currentClassIndex++
                doClass(currentClassIndex)
            }
        }

        override fun hasNext(): Boolean {
            return currentClassIndex < classes.size && currentMoves.hasNext()
        }

    }


}

class CounterTieredFactory {
    companion object {
        fun create(size: Int): CounterTiered {
            return CounterTiered()
        }
    }
}

/**
 * Expand the class ints into the full range of moves.
 *
 * class 0: 0,1,6,7,12,13
 * class 1: 2,3,8,9,14,15
 *
const val F = 0
const val B = 1
const val U = 2
const val D = 3
const val L = 4
const val R = 5

const val F_ = 6
const val B_ = 7
const val U_ = 8
const val D_ = 9
const val L_ = 10
const val R_ = 11

const val F2 = 12
const val B2 = 13
const val U2 = 14
const val D2 = 15
const val L2 = 16
const val R2 = 17
 */
fun fillIn(index: Int, classes: IntArray, moveList: IntArray): List<IntArray> {
    val result = mutableListOf<IntArray>()

    // if the movelist is complete, finish it
    if (index == classes.size) {
        return listOf(moveList)
    }

    // if index == 0, all possible moves are here
    if (index == 0 || classes[index] != classes[index - 1]) {
        (0..1).forEach { plus ->
            val move = (classes[index] * 2) + plus
            (0..12 step 6).forEach { extra ->
                val decodeMove = decodeMove(move + extra)
                val copyOf = moveList.copyOf()
                copyOf[index] = move + extra
                result.addAll(fillIn(index + 1, classes, copyOf))
            }
        }
    }
    // if we're doing a repeat of the same class
    else if (classes[index] == classes[index - 1]) {
        // we're considering exactly the same class as the move before... watch it! can't add a move on the same face
        // what's the face of the previous one?
        val face = moveList[index - 1] % 6
        // now set the new move to be anything but a move of that face
        val newFace = if (face % 2 == 0) {
            // the other face should be the uneven one -> add one to the values
            face + 1
        } else {
            // subtract one from the values
            face - 1
        }
        // now add the three possible moves that remain to us
        (0..12 step 6).map { it + newFace }.forEach { move ->
            val copyOf = moveList.copyOf()
            copyOf[index] = move
            result.addAll(fillIn(index + 1, classes, copyOf))
        }
    } else {
        throw IllegalStateException()
    }

    return result
}

