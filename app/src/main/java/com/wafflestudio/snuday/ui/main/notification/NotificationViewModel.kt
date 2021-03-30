package com.wafflestudio.snuday.ui.main.notification

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.wafflestudio.snuday.service.UserService

class NotificationViewModel @ViewModelInject constructor(private val userService: UserService) : ViewModel() {

    fun getNotice() = userService.getNotice()

}