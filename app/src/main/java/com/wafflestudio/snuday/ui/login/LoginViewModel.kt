package com.wafflestudio.snuday.ui.login

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.wafflestudio.snuday.service.UserService

class LoginViewModel @ViewModelInject constructor(private val userService: UserService) : ViewModel() {

    fun login(username: String, password: String) = userService.loginUser(username, password)

}