package com.wafflestudio.snuday.network.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.wafflestudio.snuday.model.Notice

@JsonClass(generateAdapter = true)
data class FetchNoticeResponse(
    @Json(name = "next") val next: String?,
    @Json(name = "previous") val previous: String?,
    @Json(name = "results") val notices: List<Notice>
)

