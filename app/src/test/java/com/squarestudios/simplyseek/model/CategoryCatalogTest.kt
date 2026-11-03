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
        assertTrue("Expected at least 25 categories, found ${categories.size}", categories.size >= 25)

        // Verify key categories exist including new fandoms
        val ids = categories.map { it.id }
        assertTrue(ids.contains("harry_potter"))
        assertTrue(ids.contains("big_bang_theory"))
        assertTrue(ids.contains("lord_of_the_rings"))
        assertTrue(ids.contains("star_wars"))
        assertTrue(ids.contains("the_office"))
        assertTrue(ids.contains("friends_tv"))
        assertTrue(ids.contains("breaking_bad"))
        assertTrue(ids.contains("stranger_things"))
        assertTrue(ids.contains("pokemon_world"))
        assertTrue(ids.contains("anime_legends"))
        assertTrue(ids.contains("computer_hardware"))
        assertTrue(ids.contains("playstation_hits"))
    }

    @Test
    fun testCategoriesByGroupFilter() {
        val moviesTv = CategoryCatalog.getCategoriesByGroup(CategoryGroup.MOVIES_TV)
        assertTrue(moviesTv.isNotEmpty())
        assertTrue(moviesTv.all { it.group == CategoryGroup.MOVIES_TV })
        assertTrue(moviesTv.any { it.id == "harry_potter" })
        assertTrue(moviesTv.any { it.id == "big_bang_theory" })

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
        val harryPotter = CategoryCatalog.getCategoryById("harry_potter")
        assertNotNull(harryPotter)
        assertEquals("Harry Potter", harryPotter?.title)
        assertTrue(harryPotter?.wordPool?.contains("HOGWARTS") == true)
        assertTrue(harryPotter?.wordPool?.contains("SNITCH") == true)
        assertTrue(harryPotter?.wordPool?.contains("VOLDEMORT") == true)

        val bbt = CategoryCatalog.getCategoryById("big_bang_theory")
        assertNotNull(bbt)
        assertEquals("Big Bang Theory", bbt?.title)
        assertTrue(bbt?.wordPool?.contains("BAZINGA") == true)
        assertTrue(bbt?.wordPool?.contains("SHELDON") == true)

        val hardware = CategoryCatalog.getCategoryById("computer_hardware")
        assertNotNull(hardware)
        assertEquals("Computer Hardware", hardware?.title)
        assertTrue(hardware?.wordPool?.contains("CPU") == true)
    }

    @Test
    fun testGetWordsForCategorySampleCounts() {
        val easyWords = CategoryCatalog.getWordsForCategory("computer_hardware", "Easy", seed = 1)
        assertEquals(6, easyWords.size)
        assertTrue("Easy words must fit within 8x8 grid", easyWords.all { it.length <= 8 })

        val mediumWords = CategoryCatalog.getWordsForCategory("computer_hardware", "Medium", seed = 1)
        assertEquals(8, mediumWords.size)
        assertTrue("Medium words must fit within 9x9 grid", mediumWords.all { it.length <= 9 })

        val hardWords = CategoryCatalog.getWordsForCategory("computer_hardware", "Hard", seed = 1)
        assertEquals(13, hardWords.size)
        assertTrue("Hard words must fit within 10x10 grid", hardWords.all { it.length <= 10 })
    }

    @Test
    fun testSeedDeterminismAndVariation() {
        val wordsSeed1A = CategoryCatalog.getWordsForCategory("computer_hardware", "Medium", seed = 42)
        val wordsSeed1B = CategoryCatalog.getWordsForCategory("computer_hardware", "Medium", seed = 42)
        assertEquals("Same seed must produce identical word set", wordsSeed1A, wordsSeed1B)

        val wordsSeed2 = CategoryCatalog.getWordsForCategory("computer_hardware", "Medium", seed = 999)
        assertFalse("Different seeds should generally produce varied word sets", wordsSeed1A == wordsSeed2)
    }

    @Test
    fun testWordPoolDepthAndLengthLimits() {
        val allCategories = CategoryCatalog.allCategories
        val totalWords = allCategories.sumOf { it.wordPool.size }

        assertTrue("Expected at least 1,500 total words across the catalog, found $totalWords", totalWords >= 1500)

        for (category in allCategories) {
            assertTrue(
                "Category ${category.id} should have at least 50 words for deep replayability, found ${category.wordPool.size}",
                category.wordPool.size >= 50,
            )
            for (word in category.wordPool) {
                assertTrue(
                    "Word '$word' in ${category.id} exceeds max phone grid length (10)",
                    word.length <= 10,
                )
                assertTrue(
                    "Word '$word' in ${category.id} must be at least 2 characters",
                    word.length >= 2,
                )
                assertTrue(
                    "Word '$word' in ${category.id} should only contain uppercase letters",
                    word.all { it in 'A'..'Z' },
                )
            }
        }
    }
}
