package com.wafflestudio.snuday.repository

import com.wafflestudio.snuday.network.SnudayApi
import com.wafflestudio.snuday.ui.main.notification.NoticeFilter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDataRepository @Inject constructor(
    private val snudayApi: SnudayApi
) {

    fun fetchNotice(cursor: String?) = snudayApi.fetchNotices(cursor ?: "")

    fun searchNotice(type: NoticeFilter, q: String, cursor: String?) =
        snudayApi.searchNotices(type.type, q, cursor ?: "")

    fun fetchSubscribingChannel() = snudayApi.fetchSubscribingChannels()

    fun fetchManagingChannel() = snudayApi.fetchManagingChannels()
}
