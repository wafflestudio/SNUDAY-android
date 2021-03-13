package com.wafflestudio.snuday.network

import com.wafflestudio.snuday.network.dto.event.EventGetResponse
import com.wafflestudio.snuday.network.dto.event.EventPostRequest
import com.wafflestudio.snuday.network.dto.event.EventPostResponse
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RetrofitEventService {

    // get events
    @GET("/event/")
    fun getEvent(): Single<Response<EventGetResponse>>

    @POST("/event/")
    fun postEvent(
        @Body body: EventPostRequest
    ): Single<Response<EventPostResponse>>

}