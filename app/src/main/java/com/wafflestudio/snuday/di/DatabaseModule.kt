package com.wafflestudio.snuday.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.wafflestudio.snuday.data.channelcolor.ChannelColorDao
import com.wafflestudio.snuday.data.channelcolor.ChannelColorDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideChannelColorDatabase(@ApplicationContext context: Context): ChannelColorDatabase =
        ChannelColorDatabase.getInstance(context)

    @Provides
    fun provideChannelColorDao(channelColorDatabase: ChannelColorDatabase): ChannelColorDao =
        channelColorDatabase.channelColorDao()

}