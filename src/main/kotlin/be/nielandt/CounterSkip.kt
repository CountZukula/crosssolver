package be.nielandt

import java.util.*

/**
 * Counter for X digits of a given base. Skips situations where faces are the same.
 */
class CounterSkip(size: Int) : Counter(size, 18) {

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
        if (atMax()) {
            return false
        }
        for (i in this.counter.size - 1 downTo 0) {
            this.counter[i]++

            // if we've hit the maximum value on the first digit, return false
            if(this.counter[i]==base && i == 0)
                return false

            this.lastModifiedIndex = i
            // if we're overflowing, go to 0
            if (this.counter[i] == base) {
                this.counter[i] = 0
            }
            // we're not overflowing, this is the right digit. if we have a same-face situation, increase again
            else if (i > 0 && this.counter[i] % 6 == this.counter[i - 1] % 6) {
                // we're increasing again, this time try to get into the next 'class'
                this.counter[i]++
                // which potentially overflows again
                if (this.counter[i] == base) {
                    this.counter[i] = 0
                } else {
                    // we didn't overflow, so we have increase
                    return true
                }
            }
            // didn't find a reason to inhibit this combination, break and return true
            else {
                // keep track of the digit index we're breaking on.
                return true
            }
        }
        return true
    }

    private fun containsPalindrome(): Boolean {
        val i1 = this.counter.size / 2
        for (i in 0 until i1) {
            if (this.counter[i] != this.counter[this.counter.size - 1 - i])
                return false
        }
        return true
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
    val counterSkip = CounterSkip(4)
    while (counterSkip.increase()) {
        println("counterSkip.counter = ${Arrays.toString(counterSkip.counter)}")
    }
}