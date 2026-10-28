package com.squarestudios.simplyseek.model

import org.junit.Assert.assertEquals
import org.junit.Test

class WordSearchModelsTest {

    @Test
    fun testCellCreation() {
        val cell = Cell(char = 'A', rowIndex = 0, colIndex = 1)
        assertEquals('A', cell.char)
        assertEquals(0, cell.rowIndex)
        assertEquals(1, cell.colIndex)
    }

    @Test
    fun testFoundWordCreation() {
        val startCell = Cell(char = 'C', rowIndex = 0, colIndex = 0)
        val endCell = Cell(char = 'T', rowIndex = 0, colIndex = 2)
        val foundWord = FoundWord(word = "CAT", startCell = startCell, endCell = endCell)

        assertEquals("CAT", foundWord.word)
        assertEquals(startCell, foundWord.startCell)
        assertEquals(endCell, foundWord.endCell)
    }

    @Test
    fun testGridStateCreation() {
        val cell1 = Cell('C', 0, 0)
        val cell2 = Cell('A', 0, 1)
        val cell3 = Cell('T', 0, 2)
        val row = listOf(cell1, cell2, cell3)
        val grid = listOf(row)
        val targetWords = listOf("CAT")
        val foundWord = FoundWord("CAT", cell1, cell3)

        val gridState = GridState(
            grid = grid,
            targetWords = targetWords,
            foundWords = listOf(foundWord),
        )

        assertEquals(1, gridState.grid.size)
        assertEquals(3, gridState.grid[0].size)
        assertEquals(listOf("CAT"), gridState.targetWords)
        assertEquals(1, gridState.foundWords.size)
        assertEquals("CAT", gridState.foundWords[0].word)
    }
}
