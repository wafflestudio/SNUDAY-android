package com.wafflestudio.snuday.model

data class NoticeDto(
    val id: Int?,
    val title: String,
    val contents: String,
    val channel: Int,
    val writer: Int?,
    val images: String?
)
