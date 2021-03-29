package com.wafflestudio.snuday.network.dto.channel

import com.wafflestudio.snuday.model.ChannelDto

data class ChannelGetResponse(
    val next: String?,
    val previous: String?,
    val results: List<ChannelDto>
)