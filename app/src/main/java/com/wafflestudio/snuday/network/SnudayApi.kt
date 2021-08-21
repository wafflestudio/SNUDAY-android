package com.wafflestudio.snuday.network

import com.wafflestudio.snuday.model.Channel
import com.wafflestudio.snuday.model.Event
import com.wafflestudio.snuday.model.Notice
import com.wafflestudio.snuday.model.User
import com.wafflestudio.snuday.network.dto.*
import io.reactivex.rxjava3.core.Single
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.adapter.rxjava3.Result
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

    @GET("users/me/")
    fun getMyData(): Single<User>

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

    @GET("users/me/subscribing_channels/")
    fun fetchSubscribingChannels(): Single<List<Channel>>

    @GET("users/me/managing_channels/")
    fun fetchManagingChannels(): Single<List<Channel>>

    @GET("channels/{channel_id}/awaiters/")
    fun getAwaiters(@Path(value = "channel_id") channelId: Int): Single<List<User>>

    @POST("channels/")
    fun postChannel(
        @Body body: PostChannelRequest
    ): Single<Channel>

    @PUT("channels/{channel_id}/")
    fun patchChannel(
        @Path("channel_id") channelId: Int,
        @Body body: PatchChannelRequest
    ): Single<Channel>

    @DELETE("channels/{channel_id}")
    fun deleteChannel(
        @Path("channel_id") channelId: Int
    ): Single<Response<Unit>>

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

    @GET("channels/{channel_id}/notices/")
    fun fetchChannelNotice(
        @Path(value = "channel_id") channelId: Int,
        @Query("cursor") cursor: String?
    ): Single<FetchNoticeResponse>

    @GET("channels/{channel_id}/recent_notices/")
    fun fetchChannelRecentNotice(
        @Path(value = "channel_id") channelId: Int
    ): Single<List<Notice>>

    @GET("channels/{channel_id}/events/")
    fun fetchChannelEvent(
        @Path(value = "channel_id") channelId: Int
    ): Single<List<Event>>

    @GET("channels/{channel_id}/")
    fun fetchChannelById(
        @Path(value = "channel_id")channelId: Int
    ): Single<Channel>

    @POST("channels/{channel_id}/events/")
    fun postEvent(
        @Path(value = "channel_id") channelId: Int,
        @Body body: PostEventRequest
    ): Single<Event>

    @PATCH("channels/{channel_id}/events/{event_id}/")
    fun patchEvent(
        @Path(value = "channel_id") channelId: Int,
        @Path(value = "event_id") eventId: Int,
        @Body body: PatchEventRequest
    ): Single<Event>

    @DELETE("channels/{channel_id}/events/{event_id}/")
    fun deleteEvent(
        @Path(value = "channel_id") channelId: Int,
        @Path(value = "event_id") eventId: Int
    ): Single<Response<Unit>>

    @GET("users/me/events/")
    fun fetchEvent(): Single<List<Event>>

    @POST("channels/{channel_id}/subscribe/")
    fun subscribeChannel(
        @Path(value = "channel_id") channelId: Int
    ): Single<Response<Unit>>

    @DELETE("channels/{channel_id}/subscribe/")
    fun unsubscribeChannel(
        @Path(value = "channel_id") channelId: Int
    ): Single<Response<Unit>>

    @GET("users/search/")
    fun searchUser(
        @Query("q") q: String,
        @Query("type") type: String = "username"
    ): Single<List<User>>

    @POST("channels/{channel_id}/awaiters/allow/{user_id}/")
    fun acceptAwaiter(
        @Path (value = "channel_id") channelId: Int,
        @Path (value = "user_id") userId: Int
    ): Single<Response<Unit>>

    @DELETE("channels/{channel_id}/awaiters/allow/{user_id}/")
    fun rejectAwaiter(
        @Path (value = "channel_id") channelId: Int,
        @Path (value = "user_id") userId: Int
    ): Single<Response<Unit>>

}