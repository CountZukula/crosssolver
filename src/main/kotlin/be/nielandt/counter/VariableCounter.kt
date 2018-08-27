package be.nielandt.counter

import java.util.*

/**
 * The variable counter has irregular base size for each digit.
 *
 * basesize: [1,2,1]
 * iterates:
 * - [0,0,0]
 * - [0,0,1]
 * - [0,1,0]
 * - [0,1,1]
 * - [0,2,0]
 * - ...
 */
class VariableCounter(private val baseSizes: IntArray) : Iterator<Array<Int>> {

    // init the counter with 0's, we only have a maximum as our basesize
    val counter = Array(baseSizes.size) {
        when (it) {
            baseSizes.size - 1 -> -1
            else -> 0
        }
    }

    override fun hasNext(): Boolean {
        // check if all elements in the counter has reached their maximum (basesize - 1)
        counter.forEachIndexed { index, sh ->
            if (sh < baseSizes[index] - 1)
                return true
        }
        return false
    }

    override fun next(): Array<Int> {
        for (i in this.counter.size - 1 downTo 0) {
            this.counter[i]++
            if (this.counter[i] == baseSizes[i]) {
                this.counter[i] = 0
            } else {
                return counter.copyOf()
            }
        }
        return counter.copyOf()
    }
}

fun main(args: Array<String>) {
    val counter = VariableCounter(intArrayOf(2, 3, 2))
    while (counter.hasNext()) {
        val next = counter.next()
        println("counter.next() = ${Arrays.toString(next)} ${counter.hasNext()}")
    }
}