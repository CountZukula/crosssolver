package be.nielandt

/**
 * This thing helps us to create edgemodels using a counter. The advantage is that the edgemodel doesn't need to be calculated
 * completely from scratch: previous states are kept, so if, e.g., the third digit changes in the counter (of length 5),
 * the previous state that was calculated using the first two states is used to perform move 3,4,5 on.
 *
 * This is probably equivalent to 8 nested for loops, you'd be able to keep track of temporary solutions there too....
 */
class EdgeModelFactory(val original: EdgeModel, val counter: Counter) {
    // keep a modified version of the edgemodel for each digit in the counter, from left to right
    private val history: MutableList<EdgeModel> = mutableListOf()

    // we always have one in the beginning: the initial state of the counter
    private var hasNext: Boolean = true

    init {
        // init the history
        this.history.add(original.doMove(counter.digit(0)))
        for (i in 1 until counter.size()) {
            this.history.add(this.history.last().doMove(counter.digit(i)))
        }
    }

    fun hasNext(): Boolean {
        return hasNext
    }

    fun getNext(): EdgeModel {
        // the counter was increased, hooray
        val lastOverflowIndex = counter.lastModifiedIndex
        // we only need to redo everything starting from the lastoverflowindex
        // these are our moves, but we can salvage everything up to lastoverflowindex
        // we have a history to work with... only redo what's necessary
        for (i in counter.lastModifiedIndex until counter.size()) {
            var start: EdgeModel = if (i == 0)
                original
            else
                history[i - 1]
            history[i] = start.doMove(counter.digit(i))
        }
        // increase the counter for next time
        if (!counter.increase()) {
            this.hasNext = false
        }
        // the last item in the history is now the edgemodel we need to test...
        return history.last()
    }
}