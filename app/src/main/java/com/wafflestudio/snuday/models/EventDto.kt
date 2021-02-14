package com.wafflestudio.snuday.models

import com.squareup.moshi.Json

data class EventDto(
    val id: Long,
    val user_id: Long,
    val channel_id: Long,
    val contents: String,
    @Json(name = "start_date")
    val startDate: String,
    @Json(name = "due_date")
    val dueDate: String
)