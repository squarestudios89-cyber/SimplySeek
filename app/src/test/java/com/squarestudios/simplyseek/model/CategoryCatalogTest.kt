package com.squarestudios.simplyseek.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CategoryCatalogTest {

    @Test
    fun testAllCategoriesPopulated() {
        val categories = CategoryCatalog.allCategories
        assertTrue("Expected at least 10 categories, found ${categories.size}", categories.size >= 10)

        // Verify key categories exist
        val ids = categories.map { it.id }
        assertTrue(ids.contains("computer_hardware"))
        assertTrue(ids.contains("car_engine"))
        assertTrue(ids.contains("car_makes"))
        assertTrue(ids.contains("playstation_hits"))
        assertTrue(ids.contains("xbox_franchises"))
        assertTrue(ids.contains("movies_cinema"))
        assertTrue(ids.contains("comic_characters"))
    }

    @Test
    fun testCategoriesByGroupFilter() {
        val techGaming = CategoryCatalog.getCategoriesByGroup(CategoryGroup.TECH_GAMING)
        assertTrue(techGaming.isNotEmpty())
        assertTrue(techGaming.all { it.group == CategoryGroup.TECH_GAMING })

        val automotive = CategoryCatalog.getCategoriesByGroup(CategoryGroup.AUTOMOTIVE)
        assertTrue(automotive.isNotEmpty())
        assertTrue(automotive.all { it.group == CategoryGroup.AUTOMOTIVE })

        val all = CategoryCatalog.getCategoriesByGroup(CategoryGroup.ALL)
        assertEquals(CategoryCatalog.allCategories.size, all.size)
    }

    @Test
    fun testGetCategoryById() {
        val hardware = CategoryCatalog.getCategoryById("computer_hardware")
        assertNotNull(hardware)
        assertEquals("Computer Hardware", hardware?.title)
        assertTrue(hardware?.wordPool?.contains("CPU") == true)
        assertTrue(hardware?.wordPool?.contains("GPU") == true)
        assertTrue(hardware?.wordPool?.contains("RAM") == true)

        val carEngine = CategoryCatalog.getCategoryById("car_engine")
        assertNotNull(carEngine)
        assertEquals("Car Engine Parts", carEngine?.title)
        assertTrue(carEngine?.wordPool?.contains("PISTON") == true)
        assertTrue(carEngine?.wordPool?.contains("TURBO") == true)
    }

    @Test
    fun testGetWordsForCategorySampleCounts() {
        val easyWords = CategoryCatalog.getWordsForCategory("computer_hardware", "Easy", seed = 1)
        assertEquals(5, easyWords.size)
        assertTrue("Easy words must fit within 8x8 grid", easyWords.all { it.length <= 8 })

        val mediumWords = CategoryCatalog.getWordsForCategory("computer_hardware", "Medium", seed = 1)
        assertEquals(7, mediumWords.size)
        assertTrue("Medium words must fit within 10x10 grid", mediumWords.all { it.length <= 10 })

        val hardWords = CategoryCatalog.getWordsForCategory("computer_hardware", "Hard", seed = 1)
        assertEquals(9, hardWords.size)
        assertTrue("Hard words must fit within 12x12 grid", hardWords.all { it.length <= 12 })
    }

    @Test
    fun testSeedDeterminismAndVariation() {
        val wordsSeed1A = CategoryCatalog.getWordsForCategory("computer_hardware", "Medium", seed = 42)
        val wordsSeed1B = CategoryCatalog.getWordsForCategory("computer_hardware", "Medium", seed = 42)
        assertEquals("Same seed must produce identical word set", wordsSeed1A, wordsSeed1B)

        val wordsSeed2 = CategoryCatalog.getWordsForCategory("computer_hardware", "Medium", seed = 999)
        assertFalse("Different seeds should generally produce varied word sets", wordsSeed1A == wordsSeed2)
    }
}
