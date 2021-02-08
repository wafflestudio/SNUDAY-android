package com.wafflestudio.snuday.network.dto.user

import com.squareup.moshi.Json

data class SignUpUserResponse(
    val username: String,
    val password: String,
    @Json(name = "first_name")
    val firstName: String,
    @Json(name = "last_name")
    val lastName: String,
    val email: String
)