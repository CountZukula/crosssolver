package be.nielandt.iterator

import java.time.Duration
import java.time.Instant

class ValidClassesIterator(val size: Int) : Iterator<List<Int>> {

    private var classes: Iterator<List<Int>> = classIterator().iterator()

    override fun hasNext(): Boolean {
        return this.classes.hasNext()
    }

    override fun next(): List<Int> {
        return this.classes.next()
    }

    /**
     * These are the classes (FB/UD/RL), correctly configured (no illegal combinations here)
     */
    private fun classIterator(): List<List<Int>> {
        val classes = appendRandomClass(size, listOf())
        return classes
    }

    /**
     * Helper function for classIterator()
     */
    private fun appendRandomClass(size: Int, base: List<Int>): List<List<Int>> {
        val result = mutableListOf<List<Int>>()
        when {
            base.size < size - 1 -> // add all classes, but don't repeat a group already there
                (0..2).filter { if (base.isNotEmpty()) it != base.last() else true }
                        .forEach { theClass ->
                            // and both amounts, 1+2
                            (1..2).forEach { amount ->
                                val l = base.toMutableList()
                                for (i in 1..amount) {
                                    l.add(theClass)
                                }
                                result.addAll(appendRandomClass(size, l))
                            }
                        }
            base.size == size -> {
                // we're done here
                result.add(base)
            }
            base.size == size - 1 -> // the base is not empty, only add class that is not at the end of the base list
                (0..2).filter { if (base.isEmpty()) true else it != base.last() }.forEach { theClass ->
                    val l = base.toMutableList()
                    l.add(theClass)
                    result.add(l)
                }
        }
        return result
    }
}

fun main(args: Array<String>) {
    val now = Instant.now()
    val iter = ValidClassesIterator(8)
    var count: Int = 0
    while(iter.hasNext()) {
        println("iter.next() = ${iter.next()}")
        count++
    }
    println("count = ${count}")
    println("Duration.between(Instant.now(), now) = ${Duration.between(now, Instant.now()).toMillis()}")
}

