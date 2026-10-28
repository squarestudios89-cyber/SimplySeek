package com.squarestudios.simplyseek.generator

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

class WordSearchGeneratorTest {

    private val generator = WordSearchGenerator(Random(12345))

    @Test
    fun testGridDimensionsAndFullPopulation() {
        val size = 10
        val words = listOf("KOTLIN", "ANDROID", "SEARCH")
        val grid = generator.generateGrid(size, words)

        assertEquals(size, grid.size)
        for (row in grid) {
            assertEquals(size, row.size)
            for (cell in row) {
                assertTrue("Cell should be uppercase letter A-Z", (cell.char in 'A'..'Z'))
            }
        }
    }

    @Test
    fun testWordPlacementAndSearchability() {
        val size = 10
        val words = listOf("KOTLIN", "JAVA", "SEEK")
        val gridState = generator.generateGridWithState(size, size, words)

        assertTrue(gridState.targetWords.isNotEmpty())

        val grid = gridState.grid

        // Verify that every target word placed exists on the grid in some direction
        for (word in gridState.targetWords) {
            var found = false
            for (r in 0 until size) {
                for (c in 0 until size) {
                    for (dir in Direction.all()) {
                        if (checkWordAt(grid, word, r, c, dir)) {
                            found = true
                            break
                        }
                    }
                    if (found) break
                }
                if (found) break
            }
            assertTrue("Word '$word' should be present in the generated grid", found)
        }
    }

    @Test
    fun testCustomGridDimensions() {
        val rows = 12
        val cols = 8
        val words = listOf("MOBILE", "STUDIO")
        val grid = generator.generateGrid(rows, cols, words)

        assertEquals(rows, grid.size)
        assertEquals(cols, grid[0].size)
    }

    private fun checkWordAt(
        grid: List<List<com.squarestudios.simplyseek.model.Cell>>,
        word: String,
        startRow: Int,
        startCol: Int,
        dir: Direction,
    ): Boolean {
        val endRow = startRow + (word.length - 1) * dir.rowDelta
        val endCol = startCol + (word.length - 1) * dir.colDelta

        if (endRow !in grid.indices || endCol !in grid[0].indices) {
            return false
        }

        for (i in word.indices) {
            val r = startRow + i * dir.rowDelta
            val c = startCol + i * dir.colDelta
            if (grid[r][c].char != word[i]) {
                return false
            }
        }
        return true
    }
}
