package be.nielandt.counter

/**
 * Counter for X digits of a given base.
 */
open abstract class Counter(size: Int, val base: Int = 18) {

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
     * Increase the counter one step. True if it succeeded and a new value is there, false if we've reached the end.
     */
    abstract fun increase(): Boolean

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