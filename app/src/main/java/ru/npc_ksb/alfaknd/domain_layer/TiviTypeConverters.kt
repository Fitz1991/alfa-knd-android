package ru.npc_ksb.alfaknd.domain_layer

import androidx.room.TypeConverter
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

/*class TiviTypeConverters {
}*/
object TiviTypeConverters{
    private  val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    //Нам возвращается уже дата в формате OffsetDateTime
    //@label
    @TypeConverter
    @JvmStatic
    fun fromStringtoDateTime(value: String?): LocalDateTime? {
        return value?.let {
            return formatter.parse(it, LocalDateTime::from)
        }
    }

    //Когда хотим получить lastUpdated, делаем запрос:
    //getLastUpdatedDate(): OffsetDateTime
    // БД сначала получает String методом fromDateTimeToString, fа потом @label
    @TypeConverter
    @JvmStatic
    fun fromDateTimeToString(date: LocalDateTime?): String? {
        return date?.format(formatter)
    }
}