package com.squarestudios.simplyseek.logic

import com.squarestudios.simplyseek.model.Cell
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class WordSearchSelectionManagerTest {

    private val sampleGrid = listOf(
        listOf(Cell('K', 0, 0), Cell('O', 0, 1), Cell('T', 0, 2), Cell('L', 0, 3), Cell('I', 0, 4), Cell('N', 0, 5)),
        listOf(Cell('A', 1, 0), Cell('N', 1, 1), Cell('D', 1, 2), Cell('R', 1, 3), Cell('O', 1, 4), Cell('I', 1, 5)),
        listOf(Cell('S', 2, 0), Cell('E', 2, 1), Cell('E', 2, 2), Cell('K', 2, 3), Cell('X', 2, 4), Cell('Y', 2, 5)),
    )

    private val targetWords = listOf("KOTLIN", "SEEK")

    @Test
    fun testExtractWordForward() {
        val startCell = sampleGrid[0][0]
        val endCell = sampleGrid[0][5]

        val extracted = WordSearchSelectionManager.extractWordString(startCell, endCell, sampleGrid)
        assertEquals("KOTLIN", extracted)
    }

    @Test
    fun testExtractWordReverse() {
        val startCell = sampleGrid[0][5]
        val endCell = sampleGrid[0][0]

        val extracted = WordSearchSelectionManager.extractWordString(startCell, endCell, sampleGrid)
        assertEquals("NILTOK", extracted)
    }

    @Test
    fun testCheckWordMatchForward() {
        val startCell = sampleGrid[0][0]
        val endCell = sampleGrid[0][5]

        val match = WordSearchSelectionManager.checkWordMatch(
            startCell = startCell,
            endCell = endCell,
            grid = sampleGrid,
            targetWords = targetWords,
        )

        assertNotNull(match)
        assertEquals("KOTLIN", match?.word)
        assertEquals(startCell, match?.startCell)
        assertEquals(endCell, match?.endCell)
    }

    @Test
    fun testCheckWordMatchReverse() {
        val startCell = sampleGrid[0][5]
        val endCell = sampleGrid[0][0]

        val match = WordSearchSelectionManager.checkWordMatch(
            startCell = startCell,
            endCell = endCell,
            grid = sampleGrid,
            targetWords = targetWords,
        )

        assertNotNull(match)
        assertEquals("KOTLIN", match?.word)
    }

    @Test
    fun testInvalidWordNoMatch() {
        val startCell = sampleGrid[1][0]
        val endCell = sampleGrid[1][5]

        val match = WordSearchSelectionManager.checkWordMatch(
            startCell = startCell,
            endCell = endCell,
            grid = sampleGrid,
            targetWords = targetWords,
        )

        assertNull(match)
    }
}
