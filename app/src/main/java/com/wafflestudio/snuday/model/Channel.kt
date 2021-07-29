package com.wafflestudio.snuday.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Channel(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "image") val image: String?,
    @Json(name = "description") val description: String,
    @Json(name = "is_private") val isPrivate: Boolean,
    @Json(name = "is_official") val isOfficial: Boolean,
    @Json(name = "is_personal") val isPersonal: Boolean,
    @Json(name = "subscribers_count") val subCount: Int,
    @Json(name = "managers") val managerList: List<User>
)
