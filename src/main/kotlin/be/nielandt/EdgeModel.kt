package be.nielandt


/**
 * 24 edge-faces (a single part of an edge), so 24 pieces that can have a color
-         ----------
-         |   16   |
-         |19 W  17|
-         |   18   |
-------------------------------------
|   12   |   00   |   04   |   08   |
|15 O  13|03 G  01|07 R  05|11 B  09|
|   14   |   02   |   06   |   10   |
-------------------------------------
-         |   20   |
-         |23 Y  21|
-         |   22   |
-         ----------
 * white on top, green in front
 *
 *
 *
 *
 */


class EdgeModel {

    val model: Array<Color>

    private val F = intArrayOf(13, 18, 7, 20, 3, 0, 1, 2)
    private val B = intArrayOf(8, 9, 10, 11, 16, 15, 22, 5)
    private val L = intArrayOf(3, 23, 9, 19, 12, 13, 14, 15)
    private val U = intArrayOf(16, 17, 18, 19, 0, 12, 8, 4)
    private val D = intArrayOf(2, 6, 10, 14, 20, 21, 22, 23)
    private val R = intArrayOf(4, 5, 6, 7, 1, 17, 11, 21)

    constructor() {
        // do a sanity check
        val entries = mutableListOf(F, B, L, U, D, R).flatMap { it.asList() }.groupBy { it }.entries
//        println("entries = ${entries}")
        if (entries.any {
                    it.value.size != 2
                }) {
            throw RuntimeException("each index should occur exactly twice in the arrays")
        }

        model = arrayOf(
                Color.GREEN, Color.GREEN, Color.GREEN, Color.GREEN,
                Color.RED, Color.RED, Color.RED, Color.RED,
                Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE,
                Color.ORANGE, Color.ORANGE, Color.ORANGE, Color.ORANGE,
                Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE,
                Color.YELLOW, Color.YELLOW, Color.YELLOW, Color.YELLOW
        )
    }

    constructor(randomMoves: Int) {
        val edgeModel = EdgeModel()
        val doMoves = edgeModel.doMoves(Move.random(randomMoves))
        this.model = doMoves.model
    }

    constructor(model: Array<Color>) {
        this.model = model
    }

    constructor(moves: List<Move>) {
        val edgeModel = EdgeModel()
        val newModel = edgeModel.doMoves(moves)
        this.model = newModel.model
    }

    /**
     * Do a single move and calculate the resulting edge model.
     */
    fun doMove(move: Move): EdgeModel {
        val copyOf = this.model.copyOf()
        // execute the move

        /**
         * Do a non prime move. Go from 0 to 1 to 2 to 3, 4 to 5 to 6 to 7.
         */
        fun nonPrime(s: IntArray) {
            copyOf[s[1]] = model[s[0]]
            copyOf[s[2]] = model[s[1]]
            copyOf[s[3]] = model[s[2]]
            copyOf[s[0]] = model[s[3]]

            copyOf[s[5]] = model[s[4]]
            copyOf[s[6]] = model[s[5]]
            copyOf[s[7]] = model[s[6]]
            copyOf[s[4]] = model[s[7]]
        }

        /**
         * Do a prime move. Go from 3 to 2 to 1 to 0, 7 to 6 to 5 to 4.
         */
        fun prime(s: IntArray) {
            copyOf[s[0]] = model[s[1]]
            copyOf[s[1]] = model[s[2]]
            copyOf[s[2]] = model[s[3]]
            copyOf[s[3]] = model[s[0]]

            copyOf[s[4]] = model[s[5]]
            copyOf[s[5]] = model[s[6]]
            copyOf[s[6]] = model[s[7]]
            copyOf[s[7]] = model[s[4]]
        }

        /**
         * Do a double move, jump one
         */
        fun double(s: IntArray) {
            copyOf[s[0]] = model[s[2]]
            copyOf[s[2]] = model[s[0]]

            copyOf[s[1]] = model[s[3]]
            copyOf[s[3]] = model[s[1]]

            copyOf[s[4]] = model[s[6]]
            copyOf[s[6]] = model[s[4]]

            copyOf[s[5]] = model[s[7]]
            copyOf[s[7]] = model[s[5]]
        }

        when (move) {
            Move.F -> nonPrime(F)
            Move.F_ -> prime(F)
            Move.F2 -> double(F)
            Move.B -> nonPrime(B)
            Move.B_ -> prime(B)
            Move.B2 -> double(B)
            Move.L -> nonPrime(L)
            Move.L_ -> prime(L)
            Move.L2 -> double(L)
            Move.R -> nonPrime(R)
            Move.R_ -> prime(R)
            Move.R2 -> double(R)
            Move.U -> nonPrime(U)
            Move.U_ -> prime(U)
            Move.U2 -> double(U)
            Move.D -> nonPrime(D)
            Move.D_ -> prime(D)
            Move.D2 -> double(D)
        }
        return EdgeModel(copyOf)
    }

    /**
     * Print out the edge model.
     */
    override fun toString(): String {
        val trimMargin = """
        |        ---------
        |        |   ${l(model[16])}   |
        |        | ${l(model[19])} W ${l(model[17])} |
        |        |   ${l(model[18])}   |
        |---------------------------------
        ||   ${l(model[12])}   |   ${l(model[0])}   |   ${l(model[4])}   |   ${l(model[8])}   |
        || ${l(model[15])} O ${l(model[13])} | ${l(model[3])} G ${l(model[1])} | ${l(model[7])} R ${l(model[5])} | ${l(model[11])} B ${l(model[9])} |
        ||   ${l(model[14])}   |   ${l(model[2])}   |   ${l(model[6])}   |   ${l(model[10])}   |
        |---------------------------------
        |        |   ${l(model[20])}   |
        |        | ${l(model[23])} Y ${l(model[21])} |
        |        |   ${l(model[22])}   |
        |        ---------
        """.trimMargin()
        return trimMargin
    }

    fun doMoves(f: Collection<Move>): EdgeModel {
        var edgeModel = this
        f.forEach {
            edgeModel = edgeModel.doMove(it)
        }
        return edgeModel
    }

    fun doMoves(vararg f: Move): EdgeModel {
        return this.doMoves(f.toList())
    }

    fun crossSolved(color: Color): Boolean {
        return when (color) {
            Color.WHITE -> {
                model[WB] == Color.WHITE && model[WG] == Color.WHITE && model[WO] == Color.WHITE && model[WR] == Color.WHITE &&
                        model[BW] == Color.BLUE && model[GW] == Color.GREEN && model[OW] == Color.ORANGE && model[RW] == Color.RED
            }
            Color.YELLOW -> {
                model[YB] == Color.YELLOW && model[YG] == Color.YELLOW && model[YO] == Color.YELLOW && model[YR] == Color.YELLOW &&
                        model[BY] == Color.BLUE && model[GY] == Color.GREEN && model[OY] == Color.ORANGE && model[RY] == Color.RED
            }
            Color.RED -> {
                model[RW] == Color.RED && model[RG] == Color.RED && model[RY] == Color.RED && model[RB] == Color.RED &&
                        model[WR] == Color.WHITE && model[GR] == Color.GREEN && model[YR] == Color.YELLOW && model[BR] == Color.BLUE
            }
            Color.BLUE -> {
                model[BW] == Color.BLUE && model[BR] == Color.BLUE && model[BY] == Color.BLUE && model[BO] == Color.BLUE &&
                        model[WB] == Color.WHITE && model[RB] == Color.RED && model[YB] == Color.YELLOW && model[OB] == Color.ORANGE
            }
            Color.GREEN -> {
                model[GW] == Color.GREEN && model[GO] == Color.GREEN && model[GY] == Color.GREEN && model[GR] == Color.GREEN &&
                        model[WG] == Color.WHITE && model[OG] == Color.ORANGE && model[YG] == Color.YELLOW && model[RG] == Color.RED
            }
            Color.ORANGE -> {
                model[OW] == Color.ORANGE && model[OB] == Color.ORANGE && model[OY] == Color.ORANGE && model[OG] == Color.ORANGE &&
                        model[WO] == Color.WHITE && model[BO] == Color.BLUE && model[YO] == Color.YELLOW && model[GO] == Color.GREEN
            }
        }
    }
}

