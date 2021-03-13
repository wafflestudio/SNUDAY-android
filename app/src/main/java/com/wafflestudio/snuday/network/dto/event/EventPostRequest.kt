package com.wafflestudio.snuday.network.dto.event

import com.squareup.moshi.Json
import java.time.LocalDateTime

data class EventPostRequest(
    val id: Long,
    @Json(name = "user_id")
    val userId: Long,
    @Json(name = "channel_id")
    val channelId: Long,
    val contents: String,
    @Json(name = "start_date")
    val startDate: LocalDateTime,
    @Json(name = "due_date")
    val dueDate: LocalDateTime,
    @Json(name = "created_at")
    val createdAt: LocalDateTime,
    @Json(name = "updated_at")
    val updatedAt: LocalDateTime
)