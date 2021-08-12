package com.wafflestudio.snuday.ui.main.channel

import androidx.lifecycle.ViewModel
import com.wafflestudio.snuday.model.Channel
import com.wafflestudio.snuday.repository.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

@HiltViewModel
class ChannelViewModel @Inject constructor(private val userDataRepository: UserDataRepository) : ViewModel() {

    private val _subscribingChannel = BehaviorSubject.create<List<Channel>>()
    fun observeSubscribingChannel() = _subscribingChannel.hide()

    private val _managingChannel = BehaviorSubject.create<List<Channel>>()
    fun observeManagingChannel() = _managingChannel.hide()

    private val _whichChannelList = BehaviorSubject.createDefault(ChannelFragment.ChannelListInfo.SUBSCRIBING)
    fun whichChannelList() = _whichChannelList.hide()
    fun setChannelList(listInfo: ChannelFragment.ChannelListInfo) = _whichChannelList.onNext(listInfo)

    fun loadSubscribingChannel() =
        userDataRepository
            .fetchSubscribingChannel()
            .doOnSuccess {
                _subscribingChannel.onNext(it)
            }

    fun loadManagingChannel() =
        userDataRepository
            .fetchManagingChannel()
            .doOnSuccess {
                _managingChannel.onNext(it)
            }

}