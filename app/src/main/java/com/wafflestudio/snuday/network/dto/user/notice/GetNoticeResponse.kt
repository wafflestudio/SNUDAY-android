package com.wafflestudio.snuday.network.dto.user.notice

import com.wafflestudio.snuday.model.NoticeDto

data class GetNoticeResponse(
    val next: String?,
    val previous: String?,
    val results: List<NoticeDto>
)