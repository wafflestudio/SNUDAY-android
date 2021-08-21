package com.wafflestudio.snuday.network.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PatchChannelRequest(
    @Json(name = "name") val name: String,
    @Json(name = "description") val description: String,
    @Json(name = "is_private") val isPrivate: Boolean,
    @Json(name = "managers_id") val managerNameList: List<String>
)