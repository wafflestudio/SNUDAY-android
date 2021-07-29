package com.wafflestudio.snuday.di

import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import com.wafflestudio.snuday.BuildConfig
import com.wafflestudio.snuday.network.SnudayApi
import com.wafflestudio.snuday.preference.PrefKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private val baseUrl = BuildConfig.SNUDAY_BASE_URL

    @Provides
    @Singleton
    fun provideOkHttpClient(sharedPreferences: SharedPreferences): OkHttpClient = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val tokenInterceptor = object: Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val token = sharedPreferences.getString(PrefKey.accessToken, "")
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
                val token = sharedPreferences.getString(PrefKey.accessToken, "")
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
    fun provideService(retrofit: Retrofit): SnudayApi {
        return retrofit.create(SnudayApi::class.java)
    }

}