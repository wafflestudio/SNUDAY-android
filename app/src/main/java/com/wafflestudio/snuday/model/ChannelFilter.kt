package com.wafflestudio.snuday.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChannelFilter(
    val filterList: List<Int>
)
