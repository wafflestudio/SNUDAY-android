package com.wafflestudio.snuday.di

import androidx.preference.PreferenceManager
import com.squareup.moshi.Moshi
import com.wafflestudio.snuday.BuildConfig
import com.wafflestudio.snuday.SnudayApplication
import com.wafflestudio.snuday.network.RetrofitChannelService
import com.wafflestudio.snuday.network.RetrofitUserService
import com.wafflestudio.snuday.preference.PreferenceHelper
import com.wafflestudio.snuday.service.ChannelService
import com.wafflestudio.snuday.service.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class NetworkModule {

    private val baseUrl = BuildConfig.SNUDAY_BASE_URL

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val tokenInterceptor = object: Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val token = PreferenceHelper.getString(SnudayApplication.appContext, PreferenceHelper.ACCESS_TOKEN)
                val request = chain.request()
                if (token != "") {
                    return chain.proceed(request.newBuilder().header("Authorization", "Bearer $token").build())
                }
                return chain.proceed(request.newBuilder().build())
            }
        }

        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(tokenInterceptor)
            .build()
    } else {
        val tokenInterceptor = object: Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val token = PreferenceHelper.getString(SnudayApplication.appContext, PreferenceHelper.ACCESS_TOKEN)
                val request = chain.request()

                if (token != "") {
                    return chain.proceed(request.newBuilder().header("Authorization", "Bearer $token").build())
                }
                return chain.proceed(request.newBuilder().build())
            }
        }

        OkHttpClient.Builder()
            .addInterceptor(tokenInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): Retrofit =
        Retrofit.Builder()
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .build()

    @Provides
    @Singleton
    fun provideChannelService(retrofit: Retrofit): ChannelService {
        return ChannelService(retrofit.create(RetrofitChannelService::class.java))
    }

    @Provides
    @Singleton
    fun provideUserService(retrofit: Retrofit): UserService {
        return UserService(retrofit.create(RetrofitUserService::class.java))
    }
}