package com.squarestudios.simplyseek.db.converter

import com.squarestudios.simplyseek.model.Cell
import org.junit.Assert.assertEquals
import org.junit.Test

class ConvertersTest {

    private val converters = Converters()

    @Test
    fun testCellGridConverter() {
        val row1 = listOf(Cell('K', 0, 0), Cell('O', 0, 1))
        val row2 = listOf(Cell('T', 1, 0), Cell('L', 1, 1))
        val originalGrid = listOf(row1, row2)

        val json = converters.fromCellGrid(originalGrid)
        val reconstructedGrid = converters.toCellGrid(json)

        assertEquals(originalGrid, reconstructedGrid)
    }

    @Test
    fun testStringListConverter() {
        val originalList = listOf("KOTLIN", "ANDROID", "SEEK")

        val json = converters.fromStringList(originalList)
        val reconstructedList = converters.toStringList(json)

        assertEquals(originalList, reconstructedList)
    }

    @Test
    fun testNullAndEmptyInputs() {
        assertEquals("[]", converters.fromCellGrid(null))
        assertEquals(emptyList<List<Cell>>(), converters.toCellGrid(null))
        assertEquals(emptyList<List<Cell>>(), converters.toCellGrid(""))

        assertEquals("[]", converters.fromStringList(null))
        assertEquals(emptyList<String>(), converters.toStringList(null))
        assertEquals(emptyList<String>(), converters.toStringList(""))
    }
}
