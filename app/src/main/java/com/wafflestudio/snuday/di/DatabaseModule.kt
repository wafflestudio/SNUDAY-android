package com.wafflestudio.snuday.di

import android.content.Context
import androidx.room.Room
import com.wafflestudio.snuday.db.ChannelColorDao
import com.wafflestudio.snuday.db.SnudayDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SnudayDatabase {
        return Room.databaseBuilder(context, SnudayDatabase::class.java, "snuday-db").build()
    }

    @Provides
    @Singleton
    fun provideChannelColorDao(db: SnudayDatabase): ChannelColorDao {
        return db.channelColorDao()
    }
}