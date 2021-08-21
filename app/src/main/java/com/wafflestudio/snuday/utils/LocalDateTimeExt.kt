package com.wafflestudio.snuday.utils

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

fun LocalDateTime.toPrettyString(): String {
    val year = this.year
    val month = this.monthValue
    val day = this.dayOfMonth
    val dayOfWeek = this.dayOfWeek
    val hour = this.hour
    val minute = this.minute

    return "${year}년 ${month}월 ${day}일 (${dayOfWeek.toKorean()}) " + String.format("%02d", hour) +":" + String.format("%02d", minute)
}

fun LocalDateTime.toPrettyStringDateWithOutYear(): String {
    val year = this.year
    val month = this.monthValue
    val day = this.dayOfMonth
    val dayOfWeek = this.dayOfWeek

    return "${month}월 ${day}일 (${dayOfWeek.toKorean()})"
}

fun LocalDateTime.toPrettyStringTimeWithNoonData(): String {
    var hour = this.hour
    val minute = this.minute
    var noonData = ""

    if (hour > 12) {
        noonData = "오후"
        hour -= 12
    } else {
        noonData = "오전"
    }

    return "${noonData} "+ String.format("%02d", hour) +":" + String.format("%02d", minute)
}

fun LocalDate.toPrettyStringDateWithOutYear(): String {
    val month = this.monthValue
    val day = this.dayOfMonth
    val dayOfWeek = this.dayOfWeek

    return "${month}월 ${day}일 (${dayOfWeek.toKorean()})"
}

fun LocalDate.toPrettyStringDate(): String {
    val year = this.year
    val month = this.monthValue
    val day = this.dayOfMonth
    val dayOfWeek = this.dayOfWeek

    return "${year}년 ${month}월 ${day}일 (${dayOfWeek.toKorean()})"
}

fun LocalTime.toPrettyString(): String {
    val hour = this.hour
    val minute = this.minute
    return String.format("%02d", hour) +":" + String.format("%02d", minute)
}

fun DayOfWeek.toKorean(): String {
    return when (this.value) {
        1 -> "월"
        2 -> "화"
        3 -> "수"
        4 -> "목"
        5 -> "금"
        6 -> "토"
        7 -> "일"
        else -> "-"
    }
}