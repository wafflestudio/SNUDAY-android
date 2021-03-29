package com.wafflestudio.snuday.network

import com.wafflestudio.snuday.network.dto.user.*
import com.wafflestudio.snuday.network.dto.user.channel.GetChannelResponse
import com.wafflestudio.snuday.network.dto.user.event.GetEventResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RetrofitUserService {

    // User Sign Up
    @POST("users/")
    fun signUpUser(
        @Body body: PostUserRequest
    ): Single<PostUserResponse>

    // User Sign In
    @POST("users/login/")
    fun signInUser(
        @Body body: SignInUserRequest
    ): Single<SignInUserResponse>

    // User Refresh
    @POST("users/refresh/")
    fun refreshUser(
        @Body body: RefreshRequest
    ): Single<RefreshResponse>

    // Get Events of User
    @GET("users/me/events/")
    fun getEvent(): Single<GetEventResponse>

    // Get Channels of User
    @GET("users/me/channels/")
    fun getChannel(): Single<GetChannelResponse>
}