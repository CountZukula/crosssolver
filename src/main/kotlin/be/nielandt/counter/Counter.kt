package be.nielandt.counter

import be.nielandt.decodeMove
import java.time.Duration
import java.time.Instant

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
    open fun atMax(): Boolean {
        return counter.any { it >= base } || counter.all {
            it == this.base - 1
        }
    }

    fun toStringMove(): String {
        return counter.joinToString(", ") { decodeMove(it) }
    }
}

fun main(args: Array<String>) {
    val size = 7
    // make a counter and see how many iterations it did
    testCounter(CounterBasic(size))
    testCounter(CounterSkip(size))
    testCounter(CounterBuffer(size))
}

private fun testCounter(c: Counter) {
    var l: Long = 0
    val now = Instant.now()
    while (c.increase()) {
        l++
    }
    val seconds = Duration.between(now, Instant.now()).seconds
    println("${c.javaClass} l = ${l.toDouble()/1_000_000}m ${seconds}s")
}