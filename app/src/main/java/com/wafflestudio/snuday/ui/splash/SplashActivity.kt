package com.wafflestudio.snuday.ui.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wafflestudio.snuday.R
import com.wafflestudio.snuday.repository.UserStatusManager
import com.wafflestudio.snuday.ui.entry.LoginActivity
import com.wafflestudio.snuday.ui.main.RootActivity
import com.wafflestudio.snuday.utils.showToast
import com.wafflestudio.snuday.utils.subIoObsMain
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.net.UnknownHostException
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var userStatusManager: UserStatusManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val refresh = userStatusManager.getSavedRefreshToken()

        if (refresh == null) {
            Timber.d("going LoginAct")
            startActivity(LoginActivity.createIntent(this))
            finish()
        } else {
            userStatusManager.refreshUser(refresh)
                .subIoObsMain()
                .subscribe({
                     if(it) {
                         startActivity(RootActivity.createIntent(this))
                         finish()
                     } else {
                         startActivity(LoginActivity.createIntent(this))
                         finish()
                     }
                }, {
                    Timber.e(it)
                    if (it is UnknownHostException) {
                        showToast(getString(R.string.splash_no_internet_connection))
                        finish()
                    } else {
                        startActivity(LoginActivity.createIntent(this))
                        finish()
                    }
                })

        }

    }
}