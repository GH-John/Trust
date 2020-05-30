package com.application.arenda.entities.room.converters

import androidx.room.TypeConverter
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime

class DateTimeConverter {
    @TypeConverter
    fun fromTime(time: LocalTime): String {
        return time.toString() ?: ""
    }

    @TypeConverter
    fun toTime(time: String?): LocalTime {
        return LocalTime.parse(time)
    }

    @TypeConverter
    fun fromDate(date: LocalDate): String {
        return date.toString() ?: ""
    }

    @TypeConverter
    fun toDate(date: String?): LocalDate {
        return LocalDate.parse(date)
    }

    @TypeConverter
    fun fromDateTime(dateTime: LocalDateTime): String {
        return dateTime.toString()
    }

    @TypeConverter
    fun toDateTime(dateTime: String?): LocalDateTime {
        return LocalDateTime.parse(dateTime)
    }
}