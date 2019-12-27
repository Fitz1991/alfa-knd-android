package ru.npc_ksb.alfaknd.utils

import org.threeten.bp.DateTimeUtils.toSqlDate
import java.util.*
import java.text.SimpleDateFormat
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter


val TIMEZONE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"
val SIMPLE_FORMAT = "dd.MM.yyyy HH:mm"

fun getUTCdatetimeAsString(): String {
    val dateFormat = SimpleDateFormat(TIMEZONE_FORMAT)
    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"))
    return dateFormat.format(Date())
}

fun stringDateTimeToLocalDateTime(strDateTime: String): LocalDateTime {
    val formatter = DateTimeFormatter.ofPattern(SIMPLE_FORMAT)
    return LocalDateTime.parse(strDateTime, formatter)
}

fun localDateTimeInMillis(dateTime: LocalDateTime): Long{
    val calendar = Calendar.getInstance()
    calendar.time = toSqlDate(dateTime.toLocalDate())
    calendar.set(Calendar.HOUR_OF_DAY, dateTime.hour)
    calendar.set(Calendar.MINUTE, dateTime.minute)
    return calendar.timeInMillis
}