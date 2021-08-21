package com.wafflestudio.snuday.ui.main.channel

import androidx.lifecycle.ViewModel
import com.wafflestudio.snuday.model.Channel
import com.wafflestudio.snuday.model.User
import com.wafflestudio.snuday.repository.ChannelDataRepository
import com.wafflestudio.snuday.repository.UserDataRepository
import com.wafflestudio.snuday.repository.UserStatusManager
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

@HiltViewModel
class ChannelViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val channelDataRepository: ChannelDataRepository,
    private val userStatusManager: UserStatusManager
    ) : ViewModel() {

    var myData: User? = null

    private val _subscribingChannel = BehaviorSubject.create<List<Channel>>()
    fun observeSubscribingChannel() = _subscribingChannel.hide()

    private val _managingChannel = BehaviorSubject.create<List<Channel>>()
    fun observeManagingChannel() = _managingChannel.hide()

    private val _whichChannelList =
        BehaviorSubject.createDefault(ChannelFragment.ChannelListInfo.SUBSCRIBING)

    fun whichChannelList() = _whichChannelList.hide()
    fun setChannelList(listInfo: ChannelFragment.ChannelListInfo) =
        _whichChannelList.onNext(listInfo)

    fun getAwaiter(channelId: Int) = channelDataRepository.getAwaiter(channelId)

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

    fun unsubscribeChannel(channelId: Int) = channelDataRepository.unsubscribeChannel(channelId)

    fun searchUser(q: String) = userDataRepository.searchUser(q)

    fun getSpecificUser(userName: String) = userDataRepository.searchUser(userName).map {
        it.find { it.username == userName }
    }

    fun getMyData() = userStatusManager.getMyData().doOnSuccess { myData = it }

    fun postChannel(name: String,
                    description: String,
                    isPrivate: Boolean,
                    managerNameList: List<String>) = channelDataRepository.postChannel(name, description, isPrivate, managerNameList)

    fun updateChannel(channelId: Int,
                      name: String,
                      description: String,
                      isPrivate: Boolean,
                      managerNameList: List<String>) = channelDataRepository.patchChannel(channelId, name, description, isPrivate, managerNameList)

    fun deleteChannel(channelId: Int) = channelDataRepository.deleteChannel(channelId)
    }