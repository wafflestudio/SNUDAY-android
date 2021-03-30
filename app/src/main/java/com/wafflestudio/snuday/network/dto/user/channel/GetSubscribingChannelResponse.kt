package com.wafflestudio.snuday.network.dto.user.channel

import com.wafflestudio.snuday.model.ChannelDto

data class GetSubscribingChannelResponse(
    val next: String?,
    val previous: String?,
    val results: List<ChannelDto>
)