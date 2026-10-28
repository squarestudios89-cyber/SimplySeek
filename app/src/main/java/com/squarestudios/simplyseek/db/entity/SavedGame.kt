package com.squarestudios.simplyseek.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squarestudios.simplyseek.model.Cell

/**
 * Room Entity representing a saved Word Search game session and state.
 *
 * @property id Unique identifier for the saved game session (defaults to 1 for active save).
 * @property grid The 2D grid of [Cell] objects serialized by Room TypeConverters.
 * @property targetWords List of target words to find.
 * @property elapsedTimeSeconds Total elapsed time played in seconds.
 * @property difficultyLevel Difficulty level of the game (e.g., "Easy", "Medium", "Hard").
 * @property updatedAt Timestamp (in millis) when this game was last saved.
 */
@Entity(tableName = "saved_games")
data class SavedGame(
    @PrimaryKey val id: Long = 1L,
    val grid: List<List<Cell>>,
    val targetWords: List<String>,
    val elapsedTimeSeconds: Long = 0L,
    val difficultyLevel: String = "Medium",
    val updatedAt: Long = System.currentTimeMillis(),
)
