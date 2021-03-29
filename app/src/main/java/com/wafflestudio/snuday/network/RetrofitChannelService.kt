package com.wafflestudio.snuday.network

import com.wafflestudio.snuday.network.dto.channel.ChannelGetResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.GET

interface RetrofitChannelService {

    // Get all channels
    @GET("channels/")
    fun getChannels(): Single<ChannelGetResponse>

}