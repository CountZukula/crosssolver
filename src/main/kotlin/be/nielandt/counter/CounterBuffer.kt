package be.nielandt.counter

import be.nielandt.classOf

/**
 * Counter for X digits of a given base. Skips situations where faces are the same.
 */
class CounterBuffer(size: Int) : Counter(size, 18) {

    val buffer = mutableListOf<IntArray>()

    init {
        // initialise the buffer of valid counters
        val counterBasic = CounterBasic(size)
        do {
            if (counterBasic.isValid()) {
                buffer.add(counterBasic.counter.copyOf())
            }
        } while (counterBasic.increase())
        println("Init of counter buffer, ${buffer.size} valid combos")
    }

//    /**
//     * Increase the counter.
//     *
//     * @return true if the increase happened, false if we hit the ceiling.
//     */
//    override fun increase(): Boolean {
//        var lmi = lastModifiedIndex
//        var last = super.increase()
//        lmi = min(lastModifiedIndex, lmi)
//        // are we having an invalid situation? this would be two consecutive moves on the same face
//        while (
//                (containsConsecutiveSameFaceMoves() && !atMax()) ||
//                (this.counter?.size > 1 && !atMax() && containsPalindrome())) {
//            last = super.increase()
//            lmi = min(lastModifiedIndex, lmi)
//        }
//        // we have to set the lastmodified index to the lowest point that it got to... otherwise we might be skipping some cases
//        this.lastModifiedIndex = lmi
//        return last
//    }

    override fun increase(): Boolean {
        var increasing = true
        // while we're increasing, start from scratch (from back to front)
        while (increasing) {
            // do old school counter increase, with overflow
            for (i in this.counter.size - 1 downTo 0) {
                // increase the current digit by 1
                this.counter[i]++
                // keep track of the last modified index!
                this.lastModifiedIndex = i

                // if we've hit the maximum value on the first digit, return false
                if (this.counter[i] == base && i == 0)
                    return false

                // have we overflowed?
                if (this.counter[i] == base) {
                    this.counter[i] = 0
                } else {
                    // we didn't overflow, so we have increase and we can stop the regular increase
                    break
                }
            }
            // old school increment has completed... can we speed up the process somehow?
            val lastSeriesStart = findLastSeries()
            if (lastSeriesStart != -1) {
                // floor the rest of the counter with sane values, starting from lastSeriesStart+2 (chop off the third value)
                floorCounterWithSaneValues(lastSeriesStart + 2)
            } else {
                // didn't do anything crazy, stop the increase
                increasing = false
            }
        }
        return !atMax()
    }

    /**
     * If you have X X A B C D X X you can potentially
     */
    private fun floorCounterWithSaneValues(firstIndexToChange: Int) {
        // the class of the item to change
        val currentClass = classOf(counter[firstIndexToChange])
        // each item
        for (i in firstIndexToChange until counter.size) {
            // this value is the lowest valid move available.
            // 1. cannot lengthen a series,

            // 2. cannot be the same face as the previous value

        }
    }

    /**
     * Find the last series in the counter, if it's there. -1 if not found, index of the start of the series if found.
     */
    private fun findLastSeries(): Int {
        var currentClass = classOf(counter.last())
        var length = 1
        for (i in counter.size - 2 downTo 0) {
            if (classOf(counter[i]) == currentClass) {
                length++
            } else {
                // we hit the end, if the length exceeds 3, return the last index that matched the series
                if (length >= 3) {
                    return i + 1
                }
                length = 1
                currentClass = classOf(counter[i])
            }
        }
        // we processed everything, perhaps there's a series waiting, starting at index 0?
        if (length >= 3)
            return 0
        // nothing found
        return -1
    }

    private fun goBackAndFindClassSeries(startIndex: Int): Boolean {
        // if we don't have 3+ elements before startindex, just ignore this step
        if (startIndex < 2)
            return false
        val theClass = classOf(this.counter[startIndex])
        val c1 = classOf(this.counter[startIndex - 1])
        val c2 = classOf(this.counter[startIndex - 2])
        return theClass == c1 && c1 == c2
    }

    /**
     * Are there two moves in the current counter / chain that act on the same face? This would be F+F2 for example.
     */
    private fun containsConsecutiveSameFaceMoves(): Boolean {
        for (i in 1 until this.counter.size) {
            // perhaps is a speedup
            if (this.counter[i] % 6 == this.counter[i - 1] % 6)
                return true
        }
        return false
    }


}

fun main(args: Array<String>) {
    val counterSkip = CounterBuffer(7)
    println(counterSkip)
}