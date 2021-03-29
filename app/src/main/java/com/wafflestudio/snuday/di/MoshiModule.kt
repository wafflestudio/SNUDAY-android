package com.wafflestudio.snuday.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.wafflestudio.snuday.moshi.LocalDateTimeAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import java.time.LocalDateTime
import java.util.*
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object MoshiModule {

    @Singleton
    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(LocalDateTimeAdapter())
        .addLast(KotlinJsonAdapterFactory())
        .build()
}