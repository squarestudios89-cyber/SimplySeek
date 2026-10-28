package com.squarestudios.simplyseek.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room Entity representing accumulated user progress, experience, and daily streak.
 *
 * @property id Unique identifier for user statistics (defaults to 1 for single player).
 * @property totalXp Total experience points earned.
 * @property totalPuzzlesCompleted Total number of word search puzzles completed.
 * @property currentDailyStreak Current daily consecutive streak count.
 * @property lastStreakDate Last date string (YYYY-MM-DD) streak was updated.
 */
@Entity(tableName = "user_stats")
data class UserStats(
    @PrimaryKey val id: Long = 1L,
    val totalXp: Long = 0L,
    val totalPuzzlesCompleted: Int = 0,
    val currentDailyStreak: Int = 0,
    val lastStreakDate: String = "",
)
