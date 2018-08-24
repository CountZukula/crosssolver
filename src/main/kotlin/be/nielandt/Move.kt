package be.nielandt

import java.util.*

/**
 * All the possible moves on the cube.
 */
enum class Move {
    F, F_, F2, B, B_, B2, L, L_, L2, R, R_, R2, U, U_, U2, D, D_, D2;

    /**
     * Static methods for the Move object.
     */
    companion object {
        /**
         * Make a random set of moves that makes sense.
         */
        fun random(amount: Int): List<Move> {
            val rgen = Random()
            val result = mutableListOf<Move>()
            while (result.size < amount) {
                val randomMove = Move.values()[rgen.nextInt(Move.values().size)]
                // check if it makes sense?
                if (result.isNotEmpty() && result.last() otherFace randomMove)
                    result.add(randomMove)
                else if (result.isEmpty())
                    result.add(randomMove)
            }
            return result
        }

        /**
         * Use the given counter, which contains digits that correspond with the amount of Moves, to generate a list of Moves.
         * Highest significant digits of the counter are added first (are first in the result list).
         */
        fun combo(counter: Counter): List<Move> {
            val res = mutableListOf<Move>()
            for (i in 0 until counter.size()) {
                res.add(Move.values()[counter.digit(i)])
            }
            return res
        }
    }

    /**
     * Use this as `Move otherFace Move`, a quick way to check if the moves manipulate the same face or not.
     * Example: `Move.F otherFace Move.U_ == false`
     */
    infix fun otherFace(otherMove: Move): Boolean {
        return this.toString().substring(0, 1) != otherMove.toString().substring(0, 1)
    }

    /**
     * Are both moves on the same face? Would be true for F2 and F_, for example.
     */
    infix fun sameFace(otherMove: Move): Boolean {
        return !otherFace(otherMove)
    }

}