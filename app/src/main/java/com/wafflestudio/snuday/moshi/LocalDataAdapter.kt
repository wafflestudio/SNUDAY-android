package com.wafflestudio.snuday.moshi

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class LocalDateTimeAdapter {
    @ToJson
    fun toJson(value: LocalDateTime): String {
        return FORMATTER.format(value)
    }

    @FromJson
    fun fromJson(value: String): LocalDateTime {
        return LocalDateTime.parse(value, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    }

    companion object {
        private val FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME
    }
}

class LocalDateAdapter {
    @ToJson
    fun toJson(value: LocalDate): String {
        return FORMATTER.format(value)
    }

    @FromJson
    fun fromJson(value: String): LocalDate {
        return LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE)
    }

    companion object {
        private val FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE
    }
}

class LocalTimeAdapter {
    @ToJson
    fun toJson(value: LocalTime): String {
        return FORMATTER.format(value)
    }

    @FromJson
    fun fromJson(value: String): LocalTime {
        return LocalTime.parse(value, DateTimeFormatter.ofPattern("HH:mm:ss"))
    }

    companion object {
        private val FORMATTER = DateTimeFormatter.ofPattern("HH:mm")
    }
}