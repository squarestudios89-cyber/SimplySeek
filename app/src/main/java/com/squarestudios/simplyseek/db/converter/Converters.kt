package com.squarestudios.simplyseek.db.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squarestudios.simplyseek.model.Cell

/**
 * Type converters for Room Database to handle complex nested objects like List<List<Cell>> and List<String>.
 */
class Converters {

    private val gson = Gson()

    @TypeConverter
    fun fromCellGrid(grid: List<List<Cell>>?): String {
        if (grid == null) return "[]"
        return gson.toJson(grid)
    }

    @TypeConverter
    fun toCellGrid(gridJson: String?): List<List<Cell>> {
        if (gridJson.isNullOrEmpty()) return emptyList()
        val type = object : TypeToken<List<List<Cell>>>() {}.type
        return gson.fromJson(gridJson, type) ?: emptyList()
    }

    @TypeConverter
    fun fromStringList(list: List<String>?): String {
        if (list == null) return "[]"
        return gson.toJson(list)
    }

    @TypeConverter
    fun toStringList(listJson: String?): List<String> {
        if (listJson.isNullOrEmpty()) return emptyList()
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(listJson, type) ?: emptyList()
    }
}
