package com.wafflestudio.snuday.network

import com.wafflestudio.snuday.model.Channel
import com.wafflestudio.snuday.model.Notice
import com.wafflestudio.snuday.model.User
import com.wafflestudio.snuday.network.dto.*
import com.wafflestudio.snuday.ui.main.search.SearchFilter
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*


interface SnudayApi {

    @POST("users/mail/send/")
    fun sendEmailVerification(
        @Body body: SendEmailVerificationRequest
    ): Single<String>

    @POST("users/mail/verify/")
    fun checkEmailVerification(
        @Body body: CheckEmailVerificationRequest
    ): Single<String>

    @POST("users/")
    fun signUpUser(
        @Body body: SignUpRequest
    ): Single<User>

    @POST("users/login/")
    fun loginUser(
        @Body body: LoginRequest
    ): Single<LoginResponse>

    @POST("users/refresh/")
    fun refreshUser(
        @Body body: RefreshRequest
    ): Single<RefreshResponse>

    @GET("users/me/notices/")
    fun fetchNotices(
        @Query("cursor") cursor: String
    ): Single<FetchNoticeResponse>

    @GET("users/me/notices/search/")
    fun searchNotices(
        @Query("type") type: String,
        @Query("q") q: String,
        @Query("cursor") cursor: String
    ): Single<FetchNoticeResponse>

    @GET("users/me/subscribing_channels")
    fun fetchSubscribingChannels(): Single<List<Channel>>

    @GET("channels/recommend/")
    fun fetchRecommendChannels(): Single<List<Channel>>

    @GET("channels/search/")
    fun searchChannels(
        @Query("type") type: String,
        @Query("q") q: String,
        @Query("cursor") cursor: String
    ): Single<SearchChannelResponse>

    @GET("channels/{channel_id}/notices/{notice_id}/")
    fun fetchNoticeById(
        @Path(value = "channel_id") channelId: Int,
        @Path(value = "notice_id") noticeId: Int
    ): Single<Notice>

}