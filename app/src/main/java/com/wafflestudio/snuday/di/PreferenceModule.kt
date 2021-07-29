package com.wafflestudio.snuday.di

import android.content.Context
import android.content.SharedPreferences
import com.wafflestudio.snuday.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object PreferenceModule {
    @Provides
    @Singleton
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(BuildConfig.PREF_KEY, Context.MODE_PRIVATE)
    }
}
