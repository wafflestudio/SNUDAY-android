package com.wafflestudio.snuday.network.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.LocalDate
import java.time.LocalTime

@JsonClass(generateAdapter = true)
data class PatchEventRequest(
    @Json(name = "title")
    val title: String,
    @Json(name = "memo")
    val memo: String,
    @Json(name = "channel")
    val channelId: Int,
    @Json(name = "has_time")
    val hasTime: Boolean,
    @Json(name = "start_date")
    val startDate: LocalDate,
    @Json(name = "due_date")
    val dueDate: LocalDate,
    @Json(name = "start_time")
    val startTime: LocalTime?,
    @Json(name = "due_time")
    val dueTime: LocalTime?
)