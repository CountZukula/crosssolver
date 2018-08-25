package be.nielandt

import kotlin.math.min

/**
 * Counter for X digits of a given base. Skips situations where faces are the same.
 */
class CounterSkip(size: Int) : Counter(size, 18) {

    /**
     * Increase the counter.
     *
     * @return true if the increase happened, false if we hit the ceiling.
     */
    override fun increase(): Boolean {
        var lmi = lastModifiedIndex
        var last = super.increase()
        lmi = min(lastModifiedIndex, lmi)
        // are we having an invalid situation? this would be two consecutive moves on the same face
        while (
                (containsConsecutiveSameFaceMoves() && !atMax()) ||
                (this.counter?.size > 1 && !atMax() && containsPalindrome())) {
            last = super.increase()
            lmi = min(lastModifiedIndex, lmi)
        }
        // we have to set the lastmodified index to the lowest point that it got to... otherwise we might be skipping some cases
        this.lastModifiedIndex = lmi
        return last
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