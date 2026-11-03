package com.squarestudios.simplyseek.leaderboard

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class LeaderboardEntryTest {

    @Test
    fun testLeaderboardEntryCreation() {
        val entry = LeaderboardEntry(
            id = "doc_123",
            playerName = "CyberHunter",
            timeInSeconds = 84L,
            difficulty = "Hard",
            userId = "user_abc",
            userPhotoUrl = "https://example.com/avatar.png",
            categoryTitle = "Harry Potter",
        )

        assertEquals("doc_123", entry.id)
        assertEquals("CyberHunter", entry.playerName)
        assertEquals(84L, entry.timeInSeconds)
        assertEquals("Hard", entry.difficulty)
        assertEquals("user_abc", entry.userId)
        assertEquals("https://example.com/avatar.png", entry.userPhotoUrl)
        assertEquals("Harry Potter", entry.categoryTitle)
        assertNotNull(entry.timestamp)
    }

    @Test
    fun testDefaultValues() {
        val defaultEntry = LeaderboardEntry()
        assertEquals("", defaultEntry.id)
        assertEquals("", defaultEntry.playerName)
        assertEquals(0L, defaultEntry.timeInSeconds)
        assertEquals("", defaultEntry.difficulty)
        assertEquals("", defaultEntry.userId)
        assertEquals("", defaultEntry.userPhotoUrl)
        assertEquals("", defaultEntry.categoryTitle)
    }
}
