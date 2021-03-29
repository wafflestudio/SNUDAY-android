package com.wafflestudio.snuday.model

import com.squareup.moshi.Json
import java.time.LocalDateTime

data class EventDto(
    val id: Long,
    val title: String,
    val memo: String,
    @Json(name = "channel")
    val channelId: Long,
    @Json(name = "channel_name")
    val channelName: String,
    val writer: Int,
    @Json(name = "has_time")
    val hasTime: Boolean,
    @Json(name = "start_date")
    val startDate: LocalDateTime,
    @Json(name = "due_date")
    val dueDate: LocalDateTime
)