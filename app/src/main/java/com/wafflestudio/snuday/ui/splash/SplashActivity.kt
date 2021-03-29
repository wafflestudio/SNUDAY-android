package com.wafflestudio.snuday.ui.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.wafflestudio.snuday.preference.PreferenceHelper
import com.wafflestudio.snuday.ui.login.LoginActivity
import com.wafflestudio.snuday.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkRefreshToken()

    }

    private fun checkRefreshToken() {
        val refresh = PreferenceHelper.getString(this, PreferenceHelper.REFRESH_TOKEN)
        if (refresh != "") {
            viewModel.refreshUser(refresh)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val accessToken = it.access
                    PreferenceHelper.setString(this, PreferenceHelper.ACCESS_TOKEN, accessToken)

                    startActivity(MainActivity.createIntent(this))
                    finish()
                }, {
                    Timber.d(it)

                    startActivity(LoginActivity.createIntent(this))
                    finish()
                })
        } else {
            startActivity(LoginActivity.createIntent(this))
            finish()
        }
    }


}