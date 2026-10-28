package com.squarestudios.simplyseek.generator

import com.squarestudios.simplyseek.model.Cell
import com.squarestudios.simplyseek.model.GridState
import kotlin.random.Random

/**
 * Represents directional deltas for placing words on a 2D grid.
 */
enum class Direction(val rowDelta: Int, val colDelta: Int) {
    HORIZONTAL_FORWARD(0, 1),
    HORIZONTAL_BACKWARD(0, -1),
    VERTICAL_FORWARD(1, 0),
    VERTICAL_BACKWARD(-1, 0),
    DIAGONAL_DOWN_RIGHT(1, 1),
    DIAGONAL_UP_LEFT(-1, -1),
    DIAGONAL_DOWN_LEFT(1, -1),
    DIAGONAL_UP_RIGHT(-1, 1);

    companion object {
        fun all(): Array<Direction> = entries.toTypedArray()
    }
}

/**
 * Generator class responsible for procedurally generating a Word Search grid.
 */
class WordSearchGenerator(
    private val random: Random = Random.Default,
) {

    /**
     * Generates a 2D grid of [Cell] objects filled with placed words and random uppercase letters.
     *
     * @param size The width and height of a square grid (e.g., 10 for 10x10).
     * @param words The list of target words to place in the grid.
     * @param maxAttemptsPerWord Maximum attempts to randomly place each word before skipping.
     * @return A 2D list (`List<List<Cell>>`) representing the populated board.
     */
    fun generateGrid(
        size: Int,
        words: List<String>,
        maxAttemptsPerWord: Int = 100,
    ): List<List<Cell>> {
        return generateGrid(
            rows = size,
            cols = size,
            words = words,
            maxAttemptsPerWord = maxAttemptsPerWord,
        )
    }

    /**
     * Generates a 2D grid of [Cell] objects filled with placed words and random uppercase letters.
     *
     * @param rows The height of the grid.
     * @param cols The width of the grid.
     * @param words The list of target words to place in the grid.
     * @param maxAttemptsPerWord Maximum attempts to randomly place each word before skipping.
     * @return A 2D list (`List<List<Cell>>`) representing the populated board.
     */
    fun generateGrid(
        rows: Int,
        cols: Int,
        words: List<String>,
        maxAttemptsPerWord: Int = 100,
    ): List<List<Cell>> {
        return generateGridWithState(rows, cols, words, maxAttemptsPerWord).grid
    }

    /**
     * Generates a complete [GridState] containing the 2D grid of [Cell] objects,
     * target words successfully placed, and empty found words list.
     *
     * @param rows The height of the grid.
     * @param cols The width of the grid.
     * @param words The list of target words to place in the grid.
     * @param maxAttemptsPerWord Maximum attempts to randomly place each word before skipping.
     * @return [GridState] holding the populated grid and placed target words.
     */
    fun generateGridWithState(
        rows: Int,
        cols: Int,
        words: List<String>,
        maxAttemptsPerWord: Int = 100,
    ): GridState {
        require((rows > 0) && (cols > 0)) { "Grid dimensions must be positive numbers." }

        val gridChars = Array(rows) { CharArray(cols) { ' ' } }

        val maxDimension = maxOf(rows, cols)
        val sanitizedWords = words
            .asSequence()
            .map { it.trim().uppercase() }
            .filter { it.isNotEmpty() && it.length <= maxDimension }
            .sortedByDescending { it.length }
            .toList()

        val placedWords = mutableListOf<String>()

        for (word in sanitizedWords) {
            if (tryPlaceWord(gridChars, rows, cols, word, maxAttemptsPerWord)) {
                placedWords.add(word)
            }
        }

        // Fill remaining empty cells with random uppercase letters ('A'..'Z')
        for (r in 0 until rows) {
            for (c in 0 until cols) {
                if (gridChars[r][c] == ' ') {
                    gridChars[r][c] = ('A'..'Z').random(random)
                }
            }
        }

        // Build immutable 2D List<List<Cell>>
        val cellGrid = List(rows) { r ->
            List(cols) { c ->
                Cell(
                    char = gridChars[r][c],
                    rowIndex = r,
                    colIndex = c,
                )
            }
        }

        return GridState(
            grid = cellGrid,
            targetWords = placedWords,
            foundWords = emptyList(),
        )
    }

    private fun tryPlaceWord(
        gridChars: Array<CharArray>,
        rows: Int,
        cols: Int,
        word: String,
        maxAttempts: Int,
    ): Boolean {
        val directions = Direction.all()

        repeat(maxAttempts) {
            val direction = directions[random.nextInt(directions.size)]
            val startRow = random.nextInt(rows)
            val startCol = random.nextInt(cols)

            if (canPlaceWord(gridChars, rows, cols, word, startRow, startCol, direction)) {
                placeWord(gridChars, word, startRow, startCol, direction)
                return true
            }
        }
        return false
    }

    private fun canPlaceWord(
        gridChars: Array<CharArray>,
        rows: Int,
        cols: Int,
        word: String,
        startRow: Int,
        startCol: Int,
        direction: Direction,
    ): Boolean {
        val endRow = startRow + (word.length - 1) * direction.rowDelta
        val endCol = startCol + (word.length - 1) * direction.colDelta

        // Check if the word fits within grid boundaries
        if (endRow !in 0 until rows || endCol !in 0 until cols) {
            return false
        }

        // Verify that each cell is either empty or matches the target char
        for (i in word.indices) {
            val r = startRow + i * direction.rowDelta
            val c = startCol + i * direction.colDelta
            val currentChar = gridChars[r][c]

            if (currentChar != ' ' && currentChar != word[i]) {
                return false
            }
        }

        return true
    }

    private fun placeWord(
        gridChars: Array<CharArray>,
        word: String,
        startRow: Int,
        startCol: Int,
        direction: Direction,
    ) {
        for (i in word.indices) {
            val r = startRow + i * direction.rowDelta
            val c = startCol + i * direction.colDelta
            gridChars[r][c] = word[i]
        }
    }
}
