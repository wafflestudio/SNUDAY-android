package com.wafflestudio.snuday.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.LocalDate
import java.time.LocalTime

@JsonClass(generateAdapter = true)
data class Event (
    @Json(name = "id")
    val id: Int,
    @Json(name = "title")
    val title: String,
    @Json(name = "channel")
    val channelId: Int,
    @Json(name = "has_time")
    val hasTime: Boolean,
    @Json(name = "memo")
    val memo: String,
    @Json(name = "start_date")
    val startDate: LocalDate,
    @Json(name = "due_date")
    val dueDate: LocalDate,
    @Json(name = "start_time")
    val startTime: LocalTime?,
    @Json(name = "due_time")
    val dueTime: LocalTime?,
    @Json(name = "channel_name")
    val channelName: String?,
    @Json(name = "channel_color")
    var channelColor: ChannelColor?
)