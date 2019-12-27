package ru.npc_ksb.alfaknd.domain_layer.entities.utils

import androidx.room.TypeConverter
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

/*class TiviTypeConverters {
}*/
object TiviTypeConverters{
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @TypeConverter
    @JvmStatic
    fun fromStringToDateTime(value: String?): LocalDateTime? {
        return value?.let {
            return formatter.parse(it, LocalDateTime::from)
        }
    }

    @TypeConverter
    @JvmStatic
    fun fromDateTimeToString(date: LocalDateTime?): String? {
        return date?.format(formatter)
    }
}