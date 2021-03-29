package com.wafflestudio.snuday.ui.splash

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.wafflestudio.snuday.service.UserService

class SplashViewModel @ViewModelInject constructor(private val userService: UserService) : ViewModel() {

    fun refreshUser(refresh: String) = userService.refreshUser(refresh)

}