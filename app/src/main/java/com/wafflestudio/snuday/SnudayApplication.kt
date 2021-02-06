package com.wafflestudio.snuday

import android.app.Application
import android.content.Context
import androidx.viewbinding.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class SnudayApplication : Application() {

    companion object {
        lateinit var APP: Context
    }

    override fun onCreate() {
        super.onCreate()

        APP = this

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

    }
}