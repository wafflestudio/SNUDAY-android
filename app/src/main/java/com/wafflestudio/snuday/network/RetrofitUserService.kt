package com.wafflestudio.snuday.network

import com.wafflestudio.snuday.network.dto.user.SignInUserRequest
import com.wafflestudio.snuday.network.dto.user.SignInUserResponse
import com.wafflestudio.snuday.network.dto.user.SignUpUserRequest
import com.wafflestudio.snuday.network.dto.user.SignUpUserResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RetrofitUserService {

    // User Sign Up
    @POST("/user/")
    fun signUpUser(
        @Body body: SignUpUserRequest
    ): Single<Response<SignUpUserResponse>>

    // User Sign In
    @POST("/user/login/")
    fun signInUser(
        @Body body: SignInUserRequest
    ): Single<Response<SignInUserResponse>>
}