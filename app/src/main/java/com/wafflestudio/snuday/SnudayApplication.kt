package com.wafflestudio.snuday

import android.app.Application
import android.content.Context
import com.wafflestudio.snuday.preference.PrefKey
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class SnudayApplication : Application() {

    companion object {
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()

        appContext = this

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
//            val sp = getSharedPreferences(BuildConfig.PREF_KEY, Context.MODE_PRIVATE)
//            sp.edit().remove(PrefKey.accessToken).remove(PrefKey.refreshToken).apply()
//            Timber.d("remove pref")
        }
    }
}
