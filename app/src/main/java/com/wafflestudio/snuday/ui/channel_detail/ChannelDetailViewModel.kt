package com.wafflestudio.snuday.ui.channel_detail

import androidx.lifecycle.ViewModel
import com.wafflestudio.snuday.repository.ChannelDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChannelDetailViewModel @Inject constructor(private val channelDataRepository: ChannelDataRepository) : ViewModel() {



    fun fetchChannelData(channelId: Int) = channelDataRepository.fetchChannelById(channelId)

}