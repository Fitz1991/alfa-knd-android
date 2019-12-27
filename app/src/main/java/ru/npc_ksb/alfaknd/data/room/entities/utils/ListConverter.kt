package ru.npc_ksb.alfaknd.data.room.entities.utils

import androidx.room.TypeConverter

class ListConverter {
    @TypeConverter
    fun fromList(collections: List<String>): String {
        return collections.joinToString { s: String -> "$s;" }
    }

    @TypeConverter
    fun fromString(str: String): List<String> {
        return str.split(";")
    }
}