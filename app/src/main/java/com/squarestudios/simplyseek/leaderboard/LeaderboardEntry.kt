package com.squarestudios.simplyseek.leaderboard

import com.google.firebase.firestore.DocumentId

/**
 * Data model representing a single entry on the global leaderboard.
 *
 * @property id Firestore Document ID.
 * @property playerName The display name of the player.
 * @property timeInSeconds Puzzle completion time in seconds.
 * @property difficulty Difficulty tier ("Easy", "Medium", "Hard").
 * @property timestamp Unix timestamp (in millis) when the score was recorded.
 */
data class LeaderboardEntry(
    @DocumentId val id: String = "",
    val playerName: String = "",
    val timeInSeconds: Long = 0L,
    val difficulty: String = "",
    val timestamp: Long = System.currentTimeMillis(),
)
