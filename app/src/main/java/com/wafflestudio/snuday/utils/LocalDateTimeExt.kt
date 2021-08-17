package com.wafflestudio.snuday.utils

import java.time.LocalDateTime

fun LocalDateTime.toPrettyString(): String {
    val year = this.year
    val month = this.month
    val day = this.dayOfMonth
    val dayOfWeek = this.dayOfWeek
    val hour = this.hour
    val minute = this.minute

    return "${year}년 ${month}월 ${day}일 (${dayOfWeek}) ${hour}:${minute}"
}