package com.wafflestudio.snuday.service

import com.wafflestudio.snuday.network.RetrofitUserService
import com.wafflestudio.snuday.network.dto.user.RefreshRequest
import com.wafflestudio.snuday.network.dto.user.SignInUserRequest

class UserService (
    private val retrofitUserService: RetrofitUserService
    ) {


    fun loginUser(username: String, password: String) = retrofitUserService.signInUser(
        SignInUserRequest(username, password)
    )

    fun refreshUser(refresh: String) = retrofitUserService.refreshUser(RefreshRequest(refresh))

    fun getEvent() = retrofitUserService.getEvent()

}