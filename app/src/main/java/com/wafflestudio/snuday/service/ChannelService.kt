package com.wafflestudio.snuday.service

import com.wafflestudio.snuday.network.RetrofitChannelService

class ChannelService(
    private val retrofitChannelService: RetrofitChannelService
    ) {

    fun getChannels() = retrofitChannelService.getChannels()

}