package com.wafflestudio.snuday.repository

import com.wafflestudio.snuday.network.SnudayApi
import com.wafflestudio.snuday.network.dto.PostEventRequest
import com.wafflestudio.snuday.ui.main.notification.NoticeFilter
import com.wafflestudio.snuday.utils.filterPersonalChannel
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDataRepository @Inject constructor(
    private val snudayApi: SnudayApi
) {

    fun fetchNotice(cursor: String?) = snudayApi.fetchNotices(cursor ?: "")

    fun searchNotice(type: NoticeFilter, q: String, cursor: String?) =
        snudayApi.searchNotices(type.type, q, cursor ?: "")

    fun fetchSubscribingChannel(filterPersonal: Boolean = true) = snudayApi.fetchSubscribingChannels().map {
        if (filterPersonal) return@map it.filterPersonalChannel()
        else return@map it
    }

    fun getPersonalChannel() = snudayApi.fetchSubscribingChannels().map { it.find { channel -> channel.isPersonal } }

    fun fetchManagingChannel() = snudayApi.fetchManagingChannels().map { it.filterPersonalChannel() }

    fun fetchEvent() = snudayApi.fetchEvent()

    fun searchUser(q: String) = snudayApi.searchUser(q)
}
