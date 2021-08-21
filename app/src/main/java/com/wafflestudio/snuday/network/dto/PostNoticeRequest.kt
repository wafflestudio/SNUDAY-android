package com.wafflestudio.snuday.network.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PostNoticeRequest(
    @Json(name = "title") val title: String,
    @Json(name = "contents") val contents: String
)