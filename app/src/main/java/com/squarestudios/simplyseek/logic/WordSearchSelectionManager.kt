package com.squarestudios.simplyseek.logic

import com.squarestudios.simplyseek.model.Cell
import com.squarestudios.simplyseek.model.FoundWord
import kotlin.math.abs

/**
 * Handles word extraction and matching logic for swipe-to-select word search interactions.
 */
object WordSearchSelectionManager {

    /**
     * Extracts the list of [Cell] objects along a straight line path between [startCell] and [endCell].
     * Returns empty list if cells do not form a valid horizontal, vertical, or 45-degree diagonal line.
     */
    fun extractCellsBetween(
        startCell: Cell,
        endCell: Cell,
        grid: List<List<Cell>>,
    ): List<Cell> {
        val rowDiff = endCell.rowIndex - startCell.rowIndex
        val colDiff = endCell.colIndex - startCell.colIndex

        val absRowDiff = abs(rowDiff)
        val absColDiff = abs(colDiff)

        // Must be horizontal, vertical, or 45-degree diagonal
        if (rowDiff != 0 && colDiff != 0 && absRowDiff != absColDiff) {
            return emptyList()
        }

        val steps = maxOf(absRowDiff, absColDiff)
        if (steps == 0) {
            return listOf(startCell)
        }

        val rowStep = rowDiff / steps
        val colStep = colDiff / steps

        val cells = mutableListOf<Cell>()
        var r = startCell.rowIndex
        var c = startCell.colIndex

        repeat(steps + 1) {
            if (r in grid.indices && c in grid[r].indices) {
                cells.add(grid[r][c])
            }
            r += rowStep
            c += colStep
        }

        return cells
    }

    /**
     * Extracts the string of letters between [startCell] and [endCell].
     */
    fun extractWordString(
        startCell: Cell,
        endCell: Cell,
        grid: List<List<Cell>>,
    ): String {
        return extractCellsBetween(startCell, endCell, grid)
            .map { it.char }
            .joinToString("")
    }

    /**
     * Checks if the extracted string (forward or reverse) matches a word in [targetWords].
     *
     * @return A new [FoundWord] if matched and not already found, or `null` otherwise.
     */
    fun checkWordMatch(
        startCell: Cell,
        endCell: Cell,
        grid: List<List<Cell>>,
        targetWords: List<String>,
        alreadyFoundWords: List<FoundWord> = emptyList(),
    ): FoundWord? {
        val extracted = extractWordString(startCell, endCell, grid)
        if (extracted.isEmpty()) return null

        val reversed = extracted.reversed()
        val upperTargets = targetWords.map { it.uppercase() }

        val matchedIndex = when {
            upperTargets.contains(extracted) -> upperTargets.indexOf(extracted)
            upperTargets.contains(reversed) -> upperTargets.indexOf(reversed)
            else -> -1
        }

        if (matchedIndex == -1) return null

        val matchedTarget = targetWords[matchedIndex]

        // Prevent duplicate entries for already discovered words
        val isAlreadyFound = alreadyFoundWords.any {
            it.word.equals(matchedTarget, ignoreCase = true)
        }

        if (isAlreadyFound) return null

        return FoundWord(
            word = matchedTarget,
            startCell = startCell,
            endCell = endCell,
        )
    }
}
