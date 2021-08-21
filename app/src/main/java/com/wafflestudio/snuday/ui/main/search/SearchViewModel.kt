package com.wafflestudio.snuday.ui.main.search

import androidx.lifecycle.ViewModel
import com.wafflestudio.snuday.model.Channel
import com.wafflestudio.snuday.network.dto.SearchChannelResponse
import com.wafflestudio.snuday.repository.ChannelDataRepository
import com.wafflestudio.snuday.repository.UserDataRepository
import com.wafflestudio.snuday.utils.filterPersonalChannel
import com.wafflestudio.snuday.utils.getCursor
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.BehaviorSubject
import timber.log.Timber

import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val channelDataRepository: ChannelDataRepository,
    private val userDataRepository: UserDataRepository
) : ViewModel() {

    private var searchCursor: String? = null
    private var searchKey: String = ""
    private var savedFilter: SearchFilter = SearchFilter.ALL
    private var isEnded: Boolean = false
    private var isLoading: Boolean = false
    set(value) {
        Timber.d("isLoading changed into $value")
        field = value
    }

    fun checkLoadable() = !isEnded && !isLoading

    private val _searchedChannelList = BehaviorSubject.create<List<Channel>>()
    fun observeSearchedChannel() = _searchedChannelList.hide()

    private val _subscribingChannelList = BehaviorSubject.create<List<Channel>>()
    fun subscribingChannelList() = _subscribingChannelList.hide()

    private val _searchFilter = BehaviorSubject.createDefault(SearchFilter.ALL)
    fun observeSearchFilter() = _searchFilter.hide()
    fun setFilter(filter: SearchFilter) { _searchFilter.onNext(filter) }

    fun refreshRecommendChannel() = channelDataRepository.fetchRecommendChannel()

    fun searchChannel(q: String): Single<SearchChannelResponse> {
        isEnded = false
        isLoading = true
        savedFilter = _searchFilter.value
        searchKey = q

        return channelDataRepository
            .searchChannel(_searchFilter.value, searchKey, "")
            .doOnSuccess { response ->
                if (response.next == null) isEnded = true
                searchCursor = response.next?.getCursor()
                _searchedChannelList.onNext(response.channels.filterPersonalChannel())
            }.doFinally { isLoading = false }
    }

    fun loadNextSearchChannel(): Single<SearchChannelResponse> {
        isLoading = true

        return channelDataRepository
            .searchChannel(savedFilter, searchKey, searchCursor)
            .doOnSuccess { response ->
                if (response.next == null) isEnded = true
                searchCursor = response.next?.getCursor()
                _searchedChannelList.onNext(_searchedChannelList.value.plus(response.channels.filterPersonalChannel()))
            }.doFinally { isLoading = false }
    }

    fun loadSubscribingChannel() =
        userDataRepository
            .fetchSubscribingChannel()
            .doOnSuccess {
                _subscribingChannelList.onNext(it)
            }

    fun subscribeChannel(channelId: Int) = channelDataRepository.subscribeChannel(channelId)
    fun unsubscribeChannel(channelId: Int) = channelDataRepository.unsubscribeChannel(channelId)
}