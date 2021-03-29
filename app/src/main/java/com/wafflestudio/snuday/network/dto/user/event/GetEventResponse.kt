package com.wafflestudio.snuday.network.dto.user.event

import com.wafflestudio.snuday.model.EventDto

data class GetEventResponse(
    val next: String?,
    val previous: String?,
    val results: List<EventDto>
)