//package com.wafflestudio.snuday.di
//
//import com.wafflestudio.snuday.SnudayApplication
//import com.wafflestudio.snuday.data.channelcolor.ChannelColorDao
//import com.wafflestudio.snuday.data.channelcolor.ChannelColorRepository
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.android.components.ApplicationComponent
//import javax.inject.Singleton
//
//
//@Module
//@InstallIn(ApplicationComponent::class)
//class RepositoryModule {
//
//    @Provides
//    @Singleton
//    fun provideChannelColorRepository(channelColorDao: ChannelColorDao): ChannelColorRepository {
//        return ChannelColorRepository(channelColorDao)
//    }
//
//}