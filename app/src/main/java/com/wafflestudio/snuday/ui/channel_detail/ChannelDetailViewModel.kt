package com.wafflestudio.snuday.ui.channel_detail

import androidx.lifecycle.ViewModel
import com.wafflestudio.snuday.model.Channel
import com.wafflestudio.snuday.model.Event
import com.wafflestudio.snuday.model.Notice
import com.wafflestudio.snuday.network.dto.FetchNoticeResponse
import com.wafflestudio.snuday.repository.ChannelDataRepository
import com.wafflestudio.snuday.repository.UserDataRepository
import com.wafflestudio.snuday.utils.getCursor
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

@HiltViewModel
class ChannelDetailViewModel @Inject constructor(
    private val channelDataRepository: ChannelDataRepository,
    private val userDataRepository: UserDataRepository
    ) : ViewModel() {

    var channelData: Channel? = null

    private var cursor: String? = null
    private var isEnded: Boolean = false
    private var isLoading: Boolean = false

    fun checkLoadable() = !isEnded && !isLoading

    private val _noticeList = BehaviorSubject.createDefault<List<Notice>>(listOf())
    val noticeList = _noticeList.hide()

    private val _eventList = BehaviorSubject.createDefault<List<Event>>(listOf())
    val eventList = _eventList.hide()

    fun checkSubscribing(channelId: Int) = userDataRepository.fetchSubscribingChannel().map { subList ->
        subList.find { channel -> channel.id == channelId }?.let { true } ?: false
    }

    fun checkManaging(channelId: Int) = userDataRepository.fetchManagingChannel().map { manList ->
        manList.find { channel -> channel.id == channelId}?.let { true } ?: false
    }

    fun fetchChannelNotice(channelId: Int): Single<FetchNoticeResponse> {
        isEnded = false
        isLoading = true

        return channelDataRepository.fetchChannelNotice(channelId, cursor)
            .doOnSuccess { response ->
                if (response.next == null) isEnded = true
                cursor = response.next?.getCursor()
                _noticeList.onNext(response.notices)
            }.doFinally { isLoading = false }
    }

    fun loadChannelNotice(channelId: Int): Single<FetchNoticeResponse> {
        isLoading = true

        return channelDataRepository.fetchChannelNotice(channelId, cursor)
            .doOnSuccess { response ->
                if (response.next == null) isEnded = true
                cursor = response.next?.getCursor()
                _noticeList.onNext(_noticeList.value.plus(response.notices))
            }.doFinally { isLoading = false }
    }

    fun fetchRecentNotice(channelId: Int) =
        channelDataRepository.fetchChannelRecentNotice(channelId)


    fun fetchChannelEvent(channelId: Int) =
        channelDataRepository.fetchChannelEvent(channelId)
            .doOnSuccess { eventList ->
                _eventList.onNext(eventList)
            }

    fun fetchNoticeById(channelId: Int, noticeId: Int) = channelDataRepository.fetchNoticeById(channelId, noticeId)

    fun fetchChannelData(channelId: Int) = channelDataRepository.fetchChannelById(channelId).doOnSuccess { channelData = it }

    fun postNotice(channelId: Int, title: String, contents: String) = channelDataRepository.postNotice(channelId, title, contents)

    fun subscribeChannel(channelId: Int) = channelDataRepository.subscribeChannel(channelId)
    fun unsubscribeChannel(channelId: Int) = channelDataRepository.unsubscribeChannel(channelId)

}