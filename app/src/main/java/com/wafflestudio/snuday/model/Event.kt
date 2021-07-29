package com.wafflestudio.snuday.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Event (
    @Json(name = "id")
    val id: Int,
    @Json(name = "title")
    val title: String,
    @Json(name = "channel")
    val channelId: Int,
    @Json(name = "has_time")
    val hasTime: Boolean
    )