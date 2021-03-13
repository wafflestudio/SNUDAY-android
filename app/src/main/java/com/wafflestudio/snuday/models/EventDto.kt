package com.wafflestudio.snuday.models

import com.squareup.moshi.Json
import java.time.LocalDateTime
import java.util.*

data class EventDto(
    val id: Long,
    val user_id: Long,
    val channel_id: Long,
    val contents: String,
    val startDate: LocalDateTime,
    val dueDate: LocalDateTime
)