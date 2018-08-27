package be.nielandt.counter

import be.nielandt.classOf

/**
 * Counter for X digits of a given base. Skips situations where faces are the same.
 */
class CounterBasic(size: Int) : Counter(size, 18) {

    /t **
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

    fun isValid(): Boolean {
        return !hasSeries() && !hasConsecutiveFaces()
    }

    private fun hasConsecutiveFaces(): Boolean {
        for(i in 1 until counter.size) {
           if(counter[i]%6 == counter[i-1]%6)
               return true
        }
        return false
    }


    private fun hasSeries(): Boolean {
        var currentClass = classOf(counter.last())
        var length = 1
        for(i in 1 until counter.size) {
            if(classOf(counter[i])==currentClass) {
                length++
            } else {
                // we hit the end, if the length exceeds 3, return the last index that matched the series
                if(length>=3) {
                    return true
                }
                length = 1
                currentClass = classOf(counter[i])
            }
        }
        return false
    }

}
