package be.nielandt.iterator

import be.nielandt.counter.VariableCounter
import be.nielandt.decodeMove
import java.util.*

/**
 * Iterates over the different moves of the given specific classes.
 */
class ValidMoveIterator(val classes: List<Int>) : Iterator<IntArray> {

    internal var expansionCounter: VariableCounter

    /**
     * At initialisation time, create the internal expansion counter. This one will count appropriately for each class type,
     * depending on whether the class is alone or is a duplicate.
     */
    init {
        // create the variable counter: if a class is 'alone', it can iterate over all 6 values. otherwise, each part can iterate over 3
        val intArray = IntArray(classes.size)
        var i = 0
        while (i < classes.size - 1) {
            if (i < classes.size - 1 && classes[i] == classes[i + 1]) {
                intArray[i] = 3
                intArray[i + 1] = 3
                i += 2
            } else {
                intArray[i] = 6
                i++
            }
        }
        this.expansionCounter = VariableCounter(intArray)
    }

    override fun hasNext(): Boolean = this.expansionCounter.hasNext()

    override fun next(): IntArray {
        val next = this.expansionCounter.next()
        println("Expansioncounter next = ${Arrays.toString(next)}")
        // translate this state into a list of moves
        var i = 0
        while (i < next.size) {
            // process the double situation
            if (classes[i] == classes[i + 1]) {
                // so, we're in the same state, the first counter will get the 'low' value, the second the 'high' value
                // for class FB, that would be F and B respectively
                // class 0 (FB) has to expand to 0,6,12 (F,F_F2) and 1,7,13 (B,B_,B2) respectively
                // class 1 (UD) has to expand to 2,8,14 (U,U_U2) and 3,9,15 (D,D_,D2) respectively
                // class 2 (LR) has to expand to 4,10,16 (L,L_L2) and 5,11,17 (R,R_,R2) respectively
                // next will contain 0,1,3, as the classes are a pair
                val c1 = (classes[i] * 2) + 6 * next[i]
                val c2 = (classes[i] * 2) + 6 * next[i + 1]+1
                next[i] = c1
                next[i + 1] = c2

                // bump up the counter by two
                i += 2
            }
            // now the single situation
            else {
                // class 0 (FB) has to expand to 0,1,6,7,12,13 (F,B,F_,B_,F2,B2)
                // class 1 (UD) has to expand to 2,3,8,9,14,15 (U,D,U_,D_,U2,D2)
                // class 2 (LR) has to expand to 4,5,10,11,16,17 (L,R,L_,R_,L2,R2)
                next[i] = (classes[i] * 2) + ((next[i] / 2) * 6) + (next[i] % 2)
                i++
            }
        }
        return next
    }
}

fun main(args: Array<String>) {
    val validMoveIterator = ValidMoveIterator(listOf(0, 0, 2, 1, 2, 0, 1, 1))
    println("classes = ${validMoveIterator.classes}")
    println("basesizes = ${Arrays.toString(validMoveIterator.expansionCounter.baseSizes)}")
    var count = 0
    while (validMoveIterator.hasNext()) {
        println("Arrays.toString(validMoveIterator.next()) = ${Arrays.toString(validMoveIterator.next())}")
        println("validMoveIterator = ${validMoveIterator.next().map { decodeMove(it) }}")
        count++
    }
    println("count = $count")
}