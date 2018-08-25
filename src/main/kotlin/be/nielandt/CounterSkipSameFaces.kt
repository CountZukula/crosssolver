package be.nielandt

import kotlin.math.min

/**
 * Counter for X digits of a given base. Skips situations where faces are the same.
 */
class CounterSkipSameFaces(size: Int, base: Int = 10): Counter(size, base) {

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
        while (containsConsecutiveSameFaceMoves() && !atMax()) {
            last = super.increase()
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
}