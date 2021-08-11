package com.wafflestudio.snuday.network.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SendEmailVerificationRequest(
    @Json(name = "email_prefix") val emailPrefix: String
)