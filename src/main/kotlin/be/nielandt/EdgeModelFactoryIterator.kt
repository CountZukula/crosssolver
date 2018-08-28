package be.nielandt

/**
 * This thing helps us to create edgemodels using a counter. The advantage is that the edgemodel doesn't need to be calculated
 * completely from scratch: previous states are kept, so if, e.g., the third digit changes in the counter (of length 5),
 * the previous state that was calculated using the first two states is used to perform move 3,4,5 on.
 *
 * This is probably equivalent to 8 nested for loops, you'd be able to keep track of temporary solutions there too....
 */
class EdgeModelFactoryIterator(val original: EdgeModel, val counter: Iterator<IntArray>) {
    // keep a modified version of the edgemodel for each digit in the counter, from left to right
    private val history: MutableList<EdgeModel> = mutableListOf()
    private var previous:IntArray
    private var lastMoves: IntArray

    init {
        // init the history
        val firstOne = counter.next()
        lastMoves = firstOne
        this.history.add(original.doMove(firstOne[0]))
        for (i in 1 until firstOne.size) {
            this.history.add(this.history.last().doMove(firstOne[i]))
        }
        // keep track of the 'previous move that was performed'
        previous = firstOne
    }

    fun hasNext(): Boolean {
        return counter.hasNext()
    }

    fun movesOfCurrent() : IntArray {
        return previous
    }

    fun getNext(): EdgeModel {
        // the next set of moves
        val moves = counter.next()
        this.lastMoves = moves
        // figure out the first index that differs
        val lastModifiedIndex = firstDifferingIndex(previous, moves)
        // we only need to redo everything starting from the lastoverflowindex
        // these are our moves, but we can salvage everything up to lastoverflowindex
        // we have a history to work with... only redo what's necessary
        for (i in lastModifiedIndex until moves.size) {
            var start: EdgeModel = if (i == 0)
                original
            else
                history[i - 1]
            history[i] = start.doMove(moves[i])
        }
        // bump the previous value
        previous = moves
        // the last item in the history is now the edgemodel we need to test...
        return history.last()
    }

    private fun firstDifferingIndex(previous: IntArray, moves: IntArray): Int {
        previous.forEachIndexed { index, i ->
            if(previous[index] != moves[index])
                return index
        }
        // nothing changed.. that's probably impossible
        throw IllegalStateException()
    }
}