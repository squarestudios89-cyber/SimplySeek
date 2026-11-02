package com.squarestudios.simplyseek.db.repository

import android.content.Context
import com.squarestudios.simplyseek.db.AppDatabase
import com.squarestudios.simplyseek.db.dao.UserStatsDao
import com.squarestudios.simplyseek.db.entity.UserStats
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Repository providing user progression and statistics management backed by Room.
 */
class UserStatsRepository(
    private val dao: UserStatsDao,
) {
    constructor(database: AppDatabase) : this(database.userStatsDao())

    /**
     * Observes real-time [UserStats].
     */
    fun observeStats(): Flow<UserStats?> = dao.observeUserStats()

    /**
     * Fetches current snapshot of [UserStats] or initializes default.
     */
    suspend fun getOrCreateStats(): UserStats {
        return dao.getUserStats() ?: UserStats(
            id = 1L,
            totalXp = 0L,
            totalPuzzlesCompleted = 0,
            currentDailyStreak = 0,
            lastStreakDate = "",
        ).also { dao.insertOrUpdateUserStats(it) }
    }

    /**
     * Records a puzzle completion, updating XP, puzzle count, and daily streak.
     *
     * @param difficulty Difficulty tier ("Easy", "Medium", "Hard")
     * @return Updated [UserStats]
     */
    suspend fun recordPuzzleCompleted(difficulty: String): UserStats {
        val current = getOrCreateStats()
        val xpReward = when (difficulty.lowercase()) {
            "easy" -> 50L
            "hard" -> 150L
            else -> 100L // Medium
        }

        val todayDate = getCurrentDateString()
        val yesterdayDate = getYesterdayDateString()

        val newStreak = when (current.lastStreakDate) {
            todayDate -> current.currentDailyStreak.coerceAtLeast(1)
            yesterdayDate -> current.currentDailyStreak + 1
            else -> 1 // Streak broken or first day
        }

        val updated = current.copy(
            totalXp = current.totalXp + xpReward,
            totalPuzzlesCompleted = current.totalPuzzlesCompleted + 1,
            currentDailyStreak = newStreak,
            lastStreakDate = todayDate,
        )

        dao.insertOrUpdateUserStats(updated)
        return updated
    }

    private fun getCurrentDateString(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return sdf.format(Date())
    }

    private fun getYesterdayDateString(): String {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -1)
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return sdf.format(cal.time)
    }

    companion object {
        @Volatile
        private var INSTANCE: UserStatsRepository? = null

        fun getInstance(context: Context): UserStatsRepository {
            return INSTANCE ?: synchronized(this) {
                val db = AppDatabase.getInstance(context)
                val instance = UserStatsRepository(db)
                INSTANCE = instance
                instance
            }
        }
    }
}
