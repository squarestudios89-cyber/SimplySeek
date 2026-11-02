package com.squarestudios.simplyseek.db.repository

import com.squarestudios.simplyseek.db.dao.UserStatsDao
import com.squarestudios.simplyseek.db.entity.UserStats
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class UserStatsRepositoryTest {

    private lateinit var mockDao: UserStatsDao
    private lateinit var repository: UserStatsRepository
    private var storedStats: UserStats? = null

    @Before
    fun setUp() {
        mockDao = object : UserStatsDao {
            override suspend fun insertOrUpdateUserStats(stats: UserStats) {
                storedStats = stats
            }

            override suspend fun updateUserStats(stats: UserStats) {
                storedStats = stats
            }

            override suspend fun getUserStats(id: Long): UserStats? = storedStats

            override fun observeUserStats(id: Long): Flow<UserStats?> = flowOf(storedStats)

            override suspend fun deleteUserStats(stats: UserStats) {
                storedStats = null
            }
        }
        repository = UserStatsRepository(mockDao)
    }

    @Test
    fun testGetOrCreateInitializesDefaultStats() = runBlocking {
        val stats = repository.getOrCreateStats()
        assertNotNull(stats)
        assertEquals(0L, stats.totalXp)
        assertEquals(0, stats.totalPuzzlesCompleted)
        assertEquals(0, stats.currentDailyStreak)
    }

    @Test
    fun testRecordPuzzleCompletedAddsXpAndPuzzles() = runBlocking {
        val stats1 = repository.recordPuzzleCompleted("Medium")
        assertEquals(100L, stats1.totalXp)
        assertEquals(1, stats1.totalPuzzlesCompleted)
        assertEquals(1, stats1.currentDailyStreak)

        val stats2 = repository.recordPuzzleCompleted("Hard")
        assertEquals(250L, stats2.totalXp)
        assertEquals(2, stats2.totalPuzzlesCompleted)
    }
}
