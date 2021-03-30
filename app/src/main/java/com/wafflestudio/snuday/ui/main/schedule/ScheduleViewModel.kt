package com.wafflestudio.snuday.ui.main.schedule

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.wafflestudio.snuday.model.ChannelDto
import com.wafflestudio.snuday.model.EventDto
import com.wafflestudio.snuday.service.UserService

class ScheduleViewModel @ViewModelInject constructor(private val userService: UserService) : ViewModel() {

    var events: List<EventDto> = emptyList()
    var channels: List<ChannelDto> = emptyList()

    fun getEvents() = userService.getEvent()

    fun getSubscribingChannels() = userService.getSubscribingChannel()

}
