package com.wafflestudio.snuday.ui.main.channel

import androidx.lifecycle.ViewModel
import com.wafflestudio.snuday.repository.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

@HiltViewModel
class ChannelViewModel @Inject constructor(private val userDataRepository: UserDataRepository) : ViewModel() {

    private val _whichChannelList = BehaviorSubject.createDefault(ChannelFragment.ChannelListInfo.SUBSCRIBING)
    fun whichChannelList() = _whichChannelList.hide()
    fun setChannelList(listInfo: ChannelFragment.ChannelListInfo) = _whichChannelList.onNext(listInfo)



}