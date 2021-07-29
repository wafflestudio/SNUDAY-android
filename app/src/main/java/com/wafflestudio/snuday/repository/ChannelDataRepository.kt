package com.wafflestudio.snuday.repository

import com.wafflestudio.snuday.network.SnudayApi
import com.wafflestudio.snuday.ui.main.search.SearchFilter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChannelDataRepository @Inject constructor(
    private val snudayApi: SnudayApi
) {

    fun fetchRecommendChannel() = snudayApi.fetchRecommendChannels()

    fun searchChannel(type: SearchFilter, q: String, cursor: String?) =
        snudayApi.searchChannels(type.type, q, cursor ?: "")

    fun fetchNoticeById(channelId: Int, noticeId: Int) = snudayApi.fetchNoticeById(channelId, noticeId)
}
