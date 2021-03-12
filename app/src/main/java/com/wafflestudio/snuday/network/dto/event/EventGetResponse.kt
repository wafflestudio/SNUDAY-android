package com.wafflestudio.snuday.network.dto.event

import com.wafflestudio.snuday.models.EventDto

data class EventGetResponse (
    val events: List<EventDto>
)