package com.wafflestudio.snuday.model

import com.squareup.moshi.Json

data class UserDto(
    val id: Long,
    val username: String,
    val email: String,
    @Json(name = "first_name")
    val firstName: String,
    @Json(name = "last_name")
    val lastName: String
)