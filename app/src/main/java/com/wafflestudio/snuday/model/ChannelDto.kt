package com.wafflestudio.snuday.model

import com.squareup.moshi.Json
import java.time.LocalDateTime

data class ChannelDto(
    val id: Int,
    val name: String,
    val description: String,
    @Json(name = "is_private")
    val isPrivate: Boolean,
    @Json(name = "is_official")
    val isOfficial: Boolean,
    @Json(name = "created_at")
    val createdAt: LocalDateTime,
    @Json(name = "updated_at")
    val updatedAt: LocalDateTime,
    @Json(name = "subscribers_count")
    val subscribersCount: Int,
    val managers: List<UserDto>
    )