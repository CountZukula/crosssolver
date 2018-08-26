package be.nielandt.counter

import java.util.*

/**
 * Counter for X digits of a given base. Skips situations where faces are the same.
 */
class CounterBasic(size: Int) : Counter(size, 18) {

    /**
     * Increase the counter.
     *
     * @return true if the increase happened, false if we hit the ceiling.
     */
    override fun increase(): Boolean {
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
}

fun main(args: Array<String>) {
    val counterSkip = CounterSkip(4)
    while (counterSkip.increase()) {
        println("counterSkip.counter = ${Arrays.toString(counterSkip.counter)}")
    }
}