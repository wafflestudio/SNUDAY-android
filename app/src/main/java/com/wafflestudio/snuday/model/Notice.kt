package com.wafflestudio.snuday.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Notice(
    @Json(name = "id") val id: Int,
    @Json(name = "title") val title: String,
    @Json(name = "contents") val contents: String,
    @Json(name = "channel") val channelId: Int,
    @Json(name = "images") val images: List<String>?
)