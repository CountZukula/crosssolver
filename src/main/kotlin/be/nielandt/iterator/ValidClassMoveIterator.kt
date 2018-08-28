package be.nielandt.iterator

class ValidClassMoveIterator(val size: Int) : Iterator<IntArray> {

    // current level of the iterator
    var classesIterator = ValidClassesIterator(size)
    var movesIterator: ValidMoveIterator

    init {
        this.movesIterator = ValidMoveIterator(classesIterator.next())
    }

    override fun hasNext(): Boolean {
        // check if we have another move in the move iterator
        return if (this.movesIterator.hasNext()) {
            true
        } else {
            // the moves iterator does not has any more juice, move to the next class
            if (!this.classesIterator.hasNext()) {
                false
            } else {
                nextClass()
                this.movesIterator.hasNext()
            }
        }
    }

    override fun next(): IntArray {
        // check if there's a valid move left
        if (!this.movesIterator.hasNext()) {
            nextClass()
        }
        // now we're sure there's something left
        return this.movesIterator.next()
    }

    /**
     * Bump the 'current class'. This means we have a new moves iterator at our disposal.
     */
    private fun nextClass() {
        val next = this.classesIterator.next()
        this.movesIterator = ValidMoveIterator(next)
    }
}

fun main(args: Array<String>) {
    val validClassMoveIterator = ValidClassMoveIterator(8)
    var count=0
    while (validClassMoveIterator.hasNext()) {
        validClassMoveIterator.next()
//        println("Arrays.toString(validClassMoveIterator.next()) = ${validClassMoveIterator.next().map { decodeMove(it) }}")
        count++
    }
    println("count = ${count}")
}