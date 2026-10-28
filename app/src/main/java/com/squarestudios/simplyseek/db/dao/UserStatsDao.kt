package com.squarestudios.simplyseek.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.squarestudios.simplyseek.db.entity.UserStats
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for [UserStats] database operations.
 */
@Dao
interface UserStatsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateUserStats(stats: UserStats)

    @Update
    suspend fun updateUserStats(stats: UserStats)

    @Query("SELECT * FROM user_stats WHERE id = :id LIMIT 1")
    suspend fun getUserStats(id: Long = 1L): UserStats?

    @Query("SELECT * FROM user_stats WHERE id = :id LIMIT 1")
    fun observeUserStats(id: Long = 1L): Flow<UserStats?>

    @Delete
    suspend fun deleteUserStats(stats: UserStats)
}
