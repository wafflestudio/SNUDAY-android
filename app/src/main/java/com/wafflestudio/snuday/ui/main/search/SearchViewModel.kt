package com.wafflestudio.snuday.ui.main.search

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.wafflestudio.snuday.service.ChannelService

class SearchViewModel @ViewModelInject constructor(private val channelService: ChannelService) : ViewModel() {

    fun getChannels() = channelService.getChannels()

}