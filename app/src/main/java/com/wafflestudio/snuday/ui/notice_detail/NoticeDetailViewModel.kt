package com.wafflestudio.snuday.ui.notice_detail

import androidx.lifecycle.ViewModel
import com.wafflestudio.snuday.repository.ChannelColorManager
import com.wafflestudio.snuday.repository.ChannelDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NoticeDetailViewModel @Inject constructor(
    private val channelDataRepository: ChannelDataRepository,
    private val channelColorManager: ChannelColorManager
) : ViewModel() {

    fun fetchNoticeById(channelId: Int, noticeId: Int) = channelDataRepository.fetchNoticeById(channelId, noticeId)

    fun getChannelColorById(channelId: Int) = channelColorManager.getChannelColorById(channelId)
}