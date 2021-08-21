package com.wafflestudio.snuday.repository

import com.wafflestudio.snuday.db.ChannelColorDao
import com.wafflestudio.snuday.network.SnudayApi
import com.wafflestudio.snuday.network.dto.*
import com.wafflestudio.snuday.preference.SnudayPrefObjects
import com.wafflestudio.snuday.ui.main.search.SearchFilter
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChannelDataRepository @Inject constructor(
    private val snudayApi: SnudayApi,
    snudayPrefObjects: SnudayPrefObjects
) {

    val channelFilter = snudayPrefObjects.channelFilter

    fun postChannel(name: String,
                    description: String,
                    isPrivate: Boolean,
                    managerNameList: List<String>
    ) = snudayApi.postChannel(PostChannelRequest(name, description, isPrivate, managerNameList))

    fun patchChannel(channelId: Int,
                     name: String,
                     description: String,
                     isPrivate: Boolean,
                     managerNameList: List<String>
    ) = snudayApi.patchChannel(channelId, PatchChannelRequest(name, description, isPrivate, managerNameList))

    fun deleteChannel(channelId: Int) = snudayApi.deleteChannel(channelId)

    fun getAwaiter(channelId: Int) = snudayApi.getAwaiters(channelId)

    fun allowAwaiter(channelId: Int, userId: Int) = snudayApi.acceptAwaiter(channelId, userId)

    fun rejectAwaiter(channelId: Int, userId: Int) = snudayApi.rejectAwaiter(channelId, userId)

    fun fetchRecommendChannel() = snudayApi.fetchRecommendChannels()

    fun searchChannel(type: SearchFilter, q: String, cursor: String?) =
        snudayApi.searchChannels(type.type, q, cursor ?: "")

    fun fetchChannelNotice(channelId: Int, cursor: String?) = snudayApi.fetchChannelNotice(channelId, cursor ?: "")

    fun fetchChannelRecentNotice(channelId: Int) = snudayApi.fetchChannelRecentNotice(channelId)

    fun fetchChannelEvent(channelId: Int) = snudayApi.fetchChannelEvent(channelId)

    fun fetchNoticeById(channelId: Int, noticeId: Int) = snudayApi.fetchNoticeById(channelId, noticeId)

    fun fetchChannelById(channelId: Int) = snudayApi.fetchChannelById(channelId)

    fun subscribeChannel(channelId: Int) = snudayApi.subscribeChannel(channelId).doOnSuccess {
        channelFilter.getValue()
    }

    fun unsubscribeChannel(channelId: Int) = snudayApi.unsubscribeChannel(channelId)

    fun postEvent(title: String,
                  hasTime: Boolean,
                  startDateTime: LocalDateTime,
                  dueDateTime: LocalDateTime,
                  memo: String,
                  channelId: Int
    ) = snudayApi.postEvent(channelId,
        PostEventRequest(
            title,
            memo,
            channelId,
            hasTime,
            startDateTime.toLocalDate(),
            dueDateTime.toLocalDate(),
            if (hasTime) startDateTime.toLocalTime() else null,
            if (hasTime) dueDateTime.toLocalTime() else null
        )
    )

    fun updateEvent(title: String,
                    hasTime: Boolean,
                    startDateTime: LocalDateTime,
                    dueDateTime: LocalDateTime,
                    memo: String,
                    channelId: Int,
                    eventId: Int
    ) = snudayApi.patchEvent(channelId,
        eventId,
        PatchEventRequest(
            title,
            memo,
            channelId,
            hasTime,
            startDateTime.toLocalDate(),
            dueDateTime.toLocalDate(),
            if (hasTime) startDateTime.toLocalTime() else null,
            if (hasTime) dueDateTime.toLocalTime() else null
        )
    )

    fun postNotice(
        channelId: Int,
        title: String,
        contents: String
    ) = snudayApi.postNotice(channelId, PostNoticeRequest(title, contents))

    fun deleteEvent(channelId: Int, eventId: Int) = snudayApi.deleteEvent(channelId, eventId)
}
