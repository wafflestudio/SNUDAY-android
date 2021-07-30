package com.wafflestudio.snuday.ui.main.notification

import androidx.lifecycle.ViewModel
import com.wafflestudio.snuday.model.Channel
import com.wafflestudio.snuday.model.Notice
import com.wafflestudio.snuday.network.dto.FetchNoticeResponse
import com.wafflestudio.snuday.network.dto.SearchChannelResponse
import com.wafflestudio.snuday.repository.UserDataRepository
import com.wafflestudio.snuday.ui.main.search.SearchFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository
) : ViewModel() {

    private var searchCursor: String? = null
    private var searchKey: String = ""
    private var savedFilter: NoticeFilter = NoticeFilter.ALL
    private var isEnded: Boolean = false
    private var isLoading: Boolean = false

    fun checkLoadable() = !(isEnded && isLoading)

    private val _channelData = BehaviorSubject.create<List<Channel>>()
    fun observeChannelData() = _channelData.hide()

    private val _noticeList = BehaviorSubject.create<List<Notice>>()
    fun observeNoticeList() = _noticeList.hide()

    private val _noticeFilter = BehaviorSubject.createDefault(NoticeFilter.ALL)
    fun observeNoticeFliter() = _noticeFilter.hide()
    fun setFilter(filter: NoticeFilter) { _noticeFilter.onNext(filter) }


    fun fetchNotice(): Single<FetchNoticeResponse> {
        return searchNotice("")
    }

    fun searchNotice(q: String): Single<FetchNoticeResponse> {
        isEnded = false
        isLoading = true
        savedFilter = _noticeFilter.value
        searchKey = q

        return if (searchKey == "") {
            userDataRepository
                .fetchNotice(null)
                .doOnSuccess { response ->
                    if (response.next == null) isEnded = true
                    searchCursor = response.next
                    _noticeList.onNext(response.notices)
                }
                .doFinally { isLoading = false }
        } else {
            userDataRepository
                .searchNotice(savedFilter, searchKey, null)
                .doOnSuccess { response ->
                    if (response.next == null) isEnded = true
                    searchCursor = response.next
                    _noticeList.onNext(response.notices)
                }
                .doFinally { isLoading = false }
        }
    }

    fun loadNextNotice(): Single<FetchNoticeResponse> {
        isLoading = true

        return if (searchKey == "") {
            userDataRepository
                .fetchNotice(searchCursor)
                .doOnSuccess { response ->
                    if (response.next == null) isEnded = true
                    searchCursor = response.next
                    _noticeList.onNext(_noticeList.value.plus(response.notices))
                }
                .doFinally { isLoading = false }
        } else {
            userDataRepository
                .searchNotice(savedFilter, searchKey, searchCursor)
                .doOnSuccess { response ->
                    if (response.next == null) isEnded = true
                    searchCursor = response.next
                    _noticeList.onNext(_noticeList.value.plus(response.notices))
                }
                .doFinally { isLoading = false }
        }
    }

    fun fetchChannelData() =
        userDataRepository
            .fetchSubscribingChannel()
            .doOnSuccess { response ->
                _channelData.onNext(response)
            }

    fun makeNoticeDataList() = Observable.combineLatest(
        observeNoticeList(),
        observeChannelData(),
        { noticeList, channelData ->
            val noticeDataList = mutableListOf<NotificationAdapter.NotificationItem>()
            noticeList.forEach { notice ->
                val channelName = channelData.find { channel -> channel.id == notice.channelId }?.name ?: ""
                noticeDataList.add(
                    NotificationAdapter.NotificationItem(
                        id = notice.id,
                        channelName = channelName,
                        title = notice.title,
                        channelId = notice.channelId,
                        contents = notice.contents
                    )
                )
            }
            return@combineLatest noticeDataList.toList()
        }
    )
}