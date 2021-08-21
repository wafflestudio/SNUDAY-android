package com.wafflestudio.snuday.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.wafflestudio.snuday.moshi.LocalDateAdapter
import com.wafflestudio.snuday.moshi.LocalDateTimeAdapter
import com.wafflestudio.snuday.moshi.LocalTimeAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MoshiModule {

    @Singleton
    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(LocalDateTimeAdapter())
        .add(LocalDateAdapter())
        .add(LocalTimeAdapter())
        .addLast(KotlinJsonAdapterFactory())
        .build()


}