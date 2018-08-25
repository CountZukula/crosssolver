package be.nielandt


/**
 * 24 edge-faces (a single part of an edge), so 24 pieces that can have a color
-         ----------
-         |   16   |
-         |19 W  17|
-         |   18   |
-------------------------------------
|   12   |   00   |   04   |   08   |
|15 O  13|03 G  01|07 R_indices  05|11 B_indices  09|
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

    val model: Array<Int>

    private val F_indices = intArrayOf(13, 18, 7, 20, 3, 0, 1, 2)
    private val B_indices = intArrayOf(8, 9, 10, 11, 16, 15, 22, 5)
    private val L_indices = intArrayOf(3, 23, 9, 19, 12, 13, 14, 15)
    private val U_indices = intArrayOf(16, 17, 18, 19, 0, 12, 8, 4)
    private val D_indices = intArrayOf(2, 6, 10, 14, 20, 21, 22, 23)
    private val R_indices = intArrayOf(4, 5, 6, 7, 1, 17, 11, 21)

    constructor() {
        // do a sanity check
        val entries = mutableListOf(F_indices, B_indices, L_indices, U_indices, D_indices, R_indices).flatMap { it.asList() }.groupBy { it }.entries
//        println("entries = ${entries}")
        if (entries.any {
                    it.value.size != 2
                }) {
            throw RuntimeException("each index should occur exactly twice in the arrays")
        }

        model = arrayOf(
                GREEN, GREEN, GREEN, GREEN,
                RED, RED, RED, RED,
                BLUE, BLUE, BLUE, BLUE,
                ORANGE, ORANGE, ORANGE, ORANGE,
                WHITE, WHITE, WHITE, WHITE,
                YELLOW, YELLOW, YELLOW, YELLOW
        )
    }

    constructor(randomMoves: Int) {
        val edgeModel = EdgeModel()
        val r: Array<Int> = randomMoves(randomMoves)
        val doMoves = edgeModel.doMoves(r)
        this.model = doMoves.model
    }

    constructor(model: Array<Int>) {
        this.model = model
    }

    constructor(moves: List<Int>) {
        val edgeModel = EdgeModel()
        val newModel = edgeModel.doMoves(moves)
        this.model = newModel.model
    }

    /**
     * Do a single move and calculate the resulting edge model.
     */
    fun doMove(move: Int): EdgeModel {
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
            F -> nonPrime(F_indices)
            F_ -> prime(F_indices)
            F2 -> double(F_indices)
            B -> nonPrime(B_indices)
            B_ -> prime(B_indices)
            B2 -> double(B_indices)
            L -> nonPrime(L_indices)
            L_ -> prime(L_indices)
            L2 -> double(L_indices)
            R -> nonPrime(R_indices)
            R_ -> prime(R_indices)
            R2 -> double(R_indices)
            U -> nonPrime(U_indices)
            U_ -> prime(U_indices)
            U2 -> double(U_indices)
            D -> nonPrime(D_indices)
            D_ -> prime(D_indices)
            D2 -> double(D_indices)
        }
        return EdgeModel(copyOf)
    }

    /**
     * Print out the edge model.
     */
    override fun toString(): String {
        val trimMargin = """
        |        ---------
        |        |   ${colorLetter(model[16])}   |
        |        | ${colorLetter(model[19])} W ${colorLetter(model[17])} |
        |        |   ${colorLetter(model[18])}   |
        |---------------------------------
        ||   ${colorLetter(model[12])}   |   ${colorLetter(model[0])}   |   ${colorLetter(model[4])}   |   ${colorLetter(model[8])}   |
        || ${colorLetter(model[15])} O ${colorLetter(model[13])} | ${colorLetter(model[3])} G ${colorLetter(model[1])} | ${colorLetter(model[7])} R ${colorLetter(model[5])} | ${colorLetter(model[11])} B ${colorLetter(model[9])} |
        ||   ${colorLetter(model[14])}   |   ${colorLetter(model[2])}   |   ${colorLetter(model[6])}   |   ${colorLetter(model[10])}   |
        |---------------------------------
        |        |   ${colorLetter(model[20])}   |
        |        | ${colorLetter(model[23])} Y ${colorLetter(model[21])} |
        |        |   ${colorLetter(model[22])}   |
        |        ---------
        """.trimMargin()
        return trimMargin
    }

    fun doMoves(f: Array<Int>) : EdgeModel {
        var edgeModel = this
        f.forEach {
            edgeModel = edgeModel.doMove(it)
        }
        return edgeModel
    }

    fun doMoves(f: Collection<Int>): EdgeModel {
        var edgeModel = this
        f.forEach {
            edgeModel = edgeModel.doMove(it)
        }
        return edgeModel
    }

    fun doMoves(vararg f: Int): EdgeModel {
        return this.doMoves(f.toList())
    }

    /**
     * Pass any of the colors WHITE, YELLOW, RED, ...
     */
    fun crossSolved(color: Int): Boolean {
        return when (color) {
            WHITE -> {
                model[WB] == WHITE && model[WG] == WHITE && model[WO] == WHITE && model[WR] == WHITE &&
                        model[BW] == BLUE && model[GW] == GREEN && model[OW] == ORANGE && model[RW] == RED
            }
            YELLOW -> {
                model[YB] == YELLOW && model[YG] == YELLOW && model[YO] == YELLOW && model[YR] == YELLOW &&
                        model[BY] == BLUE && model[GY] == GREEN && model[OY] == ORANGE && model[RY] == RED
            }
            RED -> {
                model[RW] == RED && model[RG] == RED && model[RY] == RED && model[RB] == RED &&
                        model[WR] == WHITE && model[GR] == GREEN && model[YR] == YELLOW && model[BR] == BLUE
            }
            BLUE -> {
                model[BW] == BLUE && model[BR] == BLUE && model[BY] == BLUE && model[BO] == BLUE &&
                        model[WB] == WHITE && model[RB] == RED && model[YB] == YELLOW && model[OB] == ORANGE
            }
            GREEN -> {
                model[GW] == GREEN && model[GO] == GREEN && model[GY] == GREEN && model[GR] == GREEN &&
                        model[WG] == WHITE && model[OG] == ORANGE && model[YG] == YELLOW && model[RG] == RED
            }
            ORANGE -> {
                model[OW] == ORANGE && model[OB] == ORANGE && model[OY] == ORANGE && model[OG] == ORANGE &&
                        model[WO] == WHITE && model[BO] == BLUE && model[YO] == YELLOW && model[GO] == GREEN
            }
            else -> {
                throw RuntimeException()
            }
        }
    }
}

