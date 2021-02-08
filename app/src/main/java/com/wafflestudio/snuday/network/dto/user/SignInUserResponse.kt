package com.wafflestudio.snuday.network.dto.user

data class SignInUserResponse(
    val access: String,
    val refresh: String
)