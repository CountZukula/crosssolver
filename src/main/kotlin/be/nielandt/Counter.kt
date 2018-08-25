package be.nielandt

/**
 * Counter for X digits of a given base.
 */
open class Counter(size: Int, val base: Int = 18) {

    /**
     * Empty counter, all 0 values for each digit.
     */
    var counter: Array<Int> = Array(size) { 0 }

    /**
     *  The last (highest significance) index that overflowed and has been changed in the counter. Could be null, if it never overflowed.
     *  Start with saying that everything changed.
     */
     var lastModifiedIndex: Int = 0

    /**
     * Increase the counter.
     *
     * @return true if the increase happened, false if we hit the ceiling.
     */
    open fun increase(): Boolean {
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