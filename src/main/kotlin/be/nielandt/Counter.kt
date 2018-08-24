package be.nielandt

import kotlin.math.min

/**
 * Counter for X digits of a given base.
 */
class Counter(size: Int, val base: Int = 10) {

    /**
     * Empty counter, all 0 values for each digit.
     */
    private var counter: Array<Int> = Array(size) { 0 }

    /**
     *  The last (highest significance) index that overflowed and has been changed in the counter. Could be null, if it never overflowed.
     *  Start with saying that everything changed.
     */
    private var lastModifiedIndex: Int = 0

    fun getLastModifiedIndex(): Int {
        return this.lastModifiedIndex
    }

    /**
     * Increase the counter.
     *
     * @return true if the increase happened, false if we hit the ceiling.
     */
    fun increase(): Boolean {
        if (atMax()) {
            return false
        }
        for (i in this.counter.size - 1 downTo 0) {
            this.counter[i]++
            this.lastModifiedIndex = i
            if (this.counter[i] == base) {
                this.counter[i] = 0
            } else {
                // keep track of the digit index we're breaking on.
                break
            }
        }
        return true
    }

    fun increaseAndSkipInvalid(): Boolean {
        var lmi = lastModifiedIndex
        var last = increase()
        lmi = min(lastModifiedIndex, lmi)
        // are we having an invalid situation? this would be two consecutive moves on the same face
        while (containsConsecutiveSameFaceMoves() && !atMax()) {
            last = increase()
            lmi = min(lastModifiedIndex, lmi)
        }
        // we have to set the lastmodified index to the lowest point that it got to... otherwise we might be skipping some cases
        this.lastModifiedIndex = lmi
        return last
    }

    /**
     * Are there two moves in the current counter / chain that act on the same face? This would be F+F2 for example.
     */
    private fun containsConsecutiveSameFaceMoves(): Boolean {
        for (i in 1 until this.counter.size) {
            val current = Move.values()[this.counter[i]]
            val previous = Move.values()[this.counter[i - 1]]
            if (current sameFace previous)
                return true
        }
        return false
    }

    /**
     * How many digits does this counter contain?
     */
    fun size(): Int {
        return this.counter.size
    }

    /**
     * Get the digit at the given index.
     */
    fun digit(index: Int): Int {
        return counter[index]
    }

    override fun toString(): String = this.counter.joinToString(".")

    /**
     * Have we reached the maximum value?
     */
    fun atMax(): Boolean {
        return counter.all {
            it == this.base - 1
        }
    }
}