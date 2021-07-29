package com.wafflestudio.snuday.ui.notice_detail

import androidx.lifecycle.ViewModel
import com.wafflestudio.snuday.repository.ChannelDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NoticeDetailViewModel @Inject constructor(
    private val channelDataRepository: ChannelDataRepository
) : ViewModel() {

    fun fetchNoticeById(channelId: Int, noticeId: Int) = channelDataRepository.fetchNoticeById(channelId, noticeId)

}