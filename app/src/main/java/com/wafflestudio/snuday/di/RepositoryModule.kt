package com.wafflestudio.snuday.di

import com.wafflestudio.snuday.SnudayApplication
import com.wafflestudio.snuday.data.channelcolor.ChannelColorRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideChannelColorRepository(): ChannelColorRepository {
        return ChannelColorRepository(SnudayApplication.appContext)
    }

}