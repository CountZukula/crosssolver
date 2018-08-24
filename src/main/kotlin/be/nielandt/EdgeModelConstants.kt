package be.nielandt

/**
 * These are the indices in the EdgeModel, given a readable name.
 * GO = green orange, meaning the green sticker of the green/orange cubie, on the green face
 */
const val GO = 3
const val GW = 0
const val GR = 1
const val GY = 2

const val WG = 18
const val WO = 19
const val WB = 16
const val WR = 17

const val YG = 20
const val YO = 23
const val YB = 22
const val YR = 21

const val RG = 7
const val RW = 4
const val RB = 5
const val RY = 6

const val BR = 11
const val BW = 8
const val BO = 9
const val BY = 10

const val OB = 15
const val OW = 12
const val OG = 13
const val OY = 14

/**
 * Six colors
 */
enum class Color(index: Int) {
    WHITE(0), YELLOW(1), ORANGE(2), RED(3), GREEN(4), BLUE(5)
}


