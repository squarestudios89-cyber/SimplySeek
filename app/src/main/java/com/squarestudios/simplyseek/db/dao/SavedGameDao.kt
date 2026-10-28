package com.squarestudios.simplyseek.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.squarestudios.simplyseek.db.entity.SavedGame
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for [SavedGame] database operations.
 */
@Dao
interface SavedGameDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateSavedGame(savedGame: SavedGame)

    @Update
    suspend fun updateSavedGame(savedGame: SavedGame)

    @Query("SELECT * FROM saved_games WHERE id = :id LIMIT 1")
    suspend fun getSavedGameById(id: Long = 1L): SavedGame?

    @Query("SELECT * FROM saved_games WHERE id = :id LIMIT 1")
    fun observeSavedGameById(id: Long = 1L): Flow<SavedGame?>

    @Query("SELECT * FROM saved_games ORDER BY updatedAt DESC")
    fun getAllSavedGames(): Flow<List<SavedGame>>

    @Delete
    suspend fun deleteSavedGame(savedGame: SavedGame)

    @Query("DELETE FROM saved_games WHERE id = :id")
    suspend fun deleteSavedGameById(id: Long = 1L)
}
