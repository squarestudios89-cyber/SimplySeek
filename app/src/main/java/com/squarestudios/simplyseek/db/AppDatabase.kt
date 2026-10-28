package com.squarestudios.simplyseek.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.squarestudios.simplyseek.db.converter.Converters
import com.squarestudios.simplyseek.db.dao.SavedGameDao
import com.squarestudios.simplyseek.db.dao.UserStatsDao
import com.squarestudios.simplyseek.db.entity.SavedGame
import com.squarestudios.simplyseek.db.entity.UserStats

/**
 * Main Room Database for SimplySeek application.
 */
@Database(
    entities = [
        SavedGame::class,
        UserStats::class,
    ],
    version = 1,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun savedGameDao(): SavedGameDao
    abstract fun userStatsDao(): UserStatsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "simply_seek_database",
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
