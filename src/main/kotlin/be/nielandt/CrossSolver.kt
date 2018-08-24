package be.nielandt

import java.time.Duration
import java.time.Instant

/**
 * Let's look for symmetries.
 */
fun main(args: Array<String>) {

//    val u2 = Move.U2
//    val doMove = edgeModel.doMove(Move.U2)
//    println(u2)
//    println(doMove)
//    println("doMove.whiteCrossSolved() = ${doMove.whiteCrossSolved()}")

//    val message = EdgeModel().doMoves(Move.R, Move.U, Move.R_)
//    println(message)
//    println("message.whiteCrossSolved() = ${message.whiteCrossSolved()}")
//
//    val doMoves = EdgeModel().doMoves(Move.random(15))
//    println("random 15 moves = ${doMoves}")
//    println("doMoves.whiteCrossSolved() = ${doMoves.whiteCrossSolved()}")


//    val begin = Instant.now()
//    var i: Long = 0
//    while (Duration.between(begin, Instant.now()) < Duration.ofMinutes(1)) {
//        EdgeModel().doMoves(Move.random(15))
//        i++
//    }
//    println("i = ${i}")
//
//    val counter = Counter(4, 3)
//    while (counter.increase()) {
//        println("counter = ${counter}")
//    }


    // make a beginning model, then start doing crazy shit
    val moves = Move.random(20)
    println("Scramble: $moves")
    val scrambledModel = EdgeModel(moves)
    println(scrambledModel)

//    doAllCrossMoveCounts(scrambledModel)
//    val allCrossMoveCount = allCrossMoveCount(scrambledModel)
//    allCrossMoveCount.forEach { color, moves ->
//        println("cross for color: ${color} in ${moves.size}: ${moves.joinToString(" ")}")
//    }
    val allCrossMoveCountUpgraded = allCrossMoveCountUpgraded(scrambledModel)
    allCrossMoveCountUpgraded.forEach { color, moves ->
        println("upgrade cross for color: ${color} in ${moves.size}: ${moves.joinToString(" ")}")
    }
    val allCrossMoveCountUpgradedSkip = allCrossMoveCountUpgradedSkip(scrambledModel)
    allCrossMoveCountUpgradedSkip.forEach { color, moves ->
        println("skip upgrade cross for color: ${color} in ${moves.size}: ${moves.joinToString(" ")}")
    }
}

/**
 * Do the color solves separately. Not very efficient, this rehashes a lot of things.
 */
fun doAllCrossMoveCounts(edgeModel: EdgeModel) {
    for (c: Color in Color.values()) {
        val crossMoveCount = crossMoveCount(edgeModel, c)
        println("${c} in ${crossMoveCount?.size}: ${crossMoveCount?.joinToString()}")
    }
}

/**
 * For a single color, go 8 deep and try and find the minimal amount of moves to solve that cross.
 */
fun crossMoveCount(edgeModel: EdgeModel, color: Color): List<Move>? {
    val moveCounts = Array<List<Move>?>(6) { null }

    for (moveCount in 1..8) {
        // build a counter of moveCount big
        val counter = Counter(moveCount, Move.values().size)

        // count up, each state of the counter corresponds to a combination of moves
        do {
            // what is the move combination we're looking at?
            val moves = Move.combo(counter)
            // execute the moves
            val afterMoves = edgeModel.doMoves(moves)
            val crossSolved = afterMoves.crossSolved(color)
            if (crossSolved) {
                return@crossMoveCount moves
            }

        } while (counter.increase())
    }
    return null
}

/**
 * Solve the minimal cross for all colors. Try to upgrade the method... Can we cache the 'previous results'?
 */
fun allCrossMoveCountUpgradedSkip(edgeModel: EdgeModel): Map<Color, List<Move>> {
    val start = Instant.now()
    val moveCounts = mutableMapOf<Color, List<Move>>()

    for (moveCount in 1..8) {
        println("all cross move count upgrade doing $moveCount")
        // build a counter of moveCount big
        val counter = Counter(moveCount, Move.values().size)
        val edgeModelFactory = EdgeModelFactory(edgeModel, counter, true)

        println("moveCounts = ${moveCounts}")

        while (edgeModelFactory.hasNext()) {
            // get the next model, using the internal counter which simply iterates over possible combinations of moves
            val next = edgeModelFactory.getNext()

            // check crosses that have not been found yet
            Color.values().forEach { color ->
                if (!moveCounts.containsKey(color)) {
                    val crossSolved = next.crossSolved(color)
                    if (crossSolved) {
                        // what is the move combination we're looking at?
                        val moves = Move.combo(counter)
                        moveCounts[color] = moves
                    }
                }
            }
            // break if we have found hem all
            if (moveCounts.keys.size == Color.values().size) {
                println("Execution time: ${Duration.between(start, Instant.now()).toMillis() / 1000}s")
//                println("counter.skipInvalidCount = ${counter.skipInvalidCount}")
                return moveCounts
            }
        }
    }
    println("Execution time: ${Duration.between(start, Instant.now()).toMillis() / 1000}s")
    return moveCounts
}
/**
 * Solve the minimal cross for all colors. Try to upgrade the method... Can we cache the 'previous results'?
 */
fun allCrossMoveCountUpgraded(edgeModel: EdgeModel): Map<Color, List<Move>> {
    val start = Instant.now()
    val moveCounts = mutableMapOf<Color, List<Move>>()

    for (moveCount in 1..8) {
        println("all cross move count upgrade doing $moveCount")
        // build a counter of moveCount big
        val counter = Counter(moveCount, Move.values().size)
        val edgeModelFactory = EdgeModelFactory(edgeModel, counter)

        while (edgeModelFactory.hasNext()) {
            // get the next model, using the internal counter which simply iterates over possible combinations of moves
            val next = edgeModelFactory.getNext()

            // check crosses that have not been found yet
            Color.values().forEach { color ->
                if (!moveCounts.containsKey(color)) {
                    val crossSolved = next.crossSolved(color)
                    if (crossSolved) {
                        // what is the move combination we're looking at?
                        val moves = Move.combo(counter)
                        moveCounts[color] = moves
                    }
                }
            }
            // break if we have found hem all
            if (moveCounts.keys.size == Color.values().size) {
                println("Execution time: ${Duration.between(start, Instant.now()).toMillis() / 1000}s")
                return moveCounts
            }
        }
    }
    println("Execution time: ${Duration.between(start, Instant.now()).toMillis() / 1000}s")
    return moveCounts
}

/**
 * This thing helps us to create edgemodels using a counter. The advantage is that the edgemodel doesn't need to be calculated
 * completely from scratch: previous states are kept, so if, e.g., the third digit changes in the counter (of length 5),
 * the previous state that was calculated using the first two states is used to perform move 3,4,5 on.
 *
 * This is probably equivalent to 8 nested for loops, you'd be able to keep track of temporary solutions there too....
 */
class EdgeModelFactory(val original: EdgeModel, val counter: Counter, val skip: Boolean = false) {
    // keep a modified version of the edgemodel for each digit in the counter, from left to right
    private val history: MutableList<EdgeModel> = mutableListOf()

    // we always have one in the beginning: the initial state of the counter
    private var hasNext: Boolean = true

    init {
        // init the history
        this.history.add(original.doMove(Move.values()[counter.digit(0)]))
        for (i in 1 until counter.size()) {
            this.history.add(this.history.last().doMove(Move.values()[counter.digit(i)]))
        }
    }

    fun hasNext(): Boolean {
        return hasNext
    }

    fun getNext(): EdgeModel {
        // the counter was increased, hooray
        val lastOverflowIndex = counter.getLastModifiedIndex()
        // we only need to redo everything starting from the lastoverflowindex
        // these are our moves, but we can salvage everything up to lastoverflowindex
        val moves = Move.combo(counter)
        // we have a history to work with... only redo what's necessary
        for (i in counter.getLastModifiedIndex() until counter.size()) {
            var start: EdgeModel?
            start = if (i == 0)
                original
            else
                history[i - 1]
            history[i] = start.doMove(Move.values()[counter.digit(i)])
        }
        // increase the counter for next time
        if(!skip) {
            if (!counter.increase()) {
                this.hasNext = false
            }
        } else {
            if (!counter.increaseAndSkipInvalid()) {
                this.hasNext = false
            }
        }
        // the last item in the history is now the edgemodel we need to test...
        return history.last()
    }
}

/**
 * Solve the minimal cross for all colors.
 */
fun allCrossMoveCount(edgeModel: EdgeModel): Map<Color, List<Move>> {
    val start = Instant.now()
    val moveCounts = mutableMapOf<Color, List<Move>>()

    for (moveCount in 1..8) {
        // build a counter of moveCount big
        println("allCrossMoveCount basic doing $moveCount")
        val counter = Counter(moveCount, Move.values().size)

        // count up, each state of the counter corresponds to a combination of moves
        do {
            // what is the move combination we're looking at?
            val moves = Move.combo(counter)
            // execute the moves
            val afterMoves = edgeModel.doMoves(moves)
            // check crosses that have not been found yet
            Color.values().forEach { color ->
                if (!moveCounts.containsKey(color)) {
                    val crossSolved = afterMoves.crossSolved(color)
                    if (crossSolved) {
                        moveCounts[color] = moves
                    }
                }
            }

            if (moveCounts.keys.size == Color.values().size) {
                println("Execution time: ${Duration.between(start, Instant.now()).toMillis() / 1000}s")
                return@allCrossMoveCount moveCounts
            }

        } while (counter.increase())
    }
    println("Execution time: ${Duration.between(start, Instant.now()).toMillis() / 1000}s")
    return moveCounts
}

/**
 * Convert the color into a single digit for print purposes.
 */
fun l(c: Color): String {
    return c.name.first().toString()
}
