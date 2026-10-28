package com.squarestudios.simplyseek.model

/**
 * Represents a single cell in the Word Search grid.
 *
 * @property char The character stored in this cell.
 * @property rowIndex The row index in the grid.
 * @property colIndex The column index in the grid.
 */
data class Cell(
    val char: Char,
    val rowIndex: Int,
    val colIndex: Int,
)

/**
 * Represents a word that has been found on the grid.
 *
 * @property word The target word string.
 * @property startCell The starting [Cell] of the found word.
 * @property endCell The ending [Cell] of the found word.
 */
data class FoundWord(
    val word: String = "",
    val startCell: Cell,
    val endCell: Cell,
)

/**
 * Represents the full state of the Word Search game grid.
 *
 * @property grid A 2D list of [Cell] objects (`List<List<Cell>>`).
 * @property targetWords A list of target word strings to find.
 * @property foundWords A list of [FoundWord] objects that have been discovered.
 */
data class GridState(
    val grid: List<List<Cell>> = emptyList(),
    val targetWords: List<String> = emptyList(),
    val foundWords: List<FoundWord> = emptyList(),
)
