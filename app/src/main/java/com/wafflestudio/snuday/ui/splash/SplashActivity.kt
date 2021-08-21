package com.wafflestudio.snuday.ui.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wafflestudio.snuday.R
import com.wafflestudio.snuday.model.ChannelFilter
import com.wafflestudio.snuday.preference.SnudayPrefObjects
import com.wafflestudio.snuday.repository.ChannelColorManager
import com.wafflestudio.snuday.repository.UserDataRepository
import com.wafflestudio.snuday.repository.UserStatusManager
import com.wafflestudio.snuday.ui.entry.LoginActivity
import com.wafflestudio.snuday.ui.main.RootActivity
import com.wafflestudio.snuday.utils.showToast
import com.wafflestudio.snuday.utils.subIoObsMain
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import timber.log.Timber
import java.net.UnknownHostException
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var userStatusManager: UserStatusManager
    @Inject
    lateinit var snudayPrefObjects: SnudayPrefObjects
    @Inject
    lateinit var userDataRepository: UserDataRepository
    @Inject
    lateinit var channelColorManager: ChannelColorManager

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val refresh = userStatusManager.getSavedRefreshToken()

        if (refresh == null) {
            startActivity(LoginActivity.createIntent(this))
            finish()
        } else {
            userStatusManager.refreshUser(refresh)
                .subIoObsMain()
                .subscribe({
                     if(it) {
                         checkPersonalChannel()
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
                }).also { compositeDisposable.add(it) }
        }
    }

    private fun checkPersonalChannel() {
        userDataRepository.getPersonalChannel()
            .subIoObsMain()
            .subscribe({ channel ->
                val channelFilterObj = snudayPrefObjects.channelFilter.getValue()
                val filter = channelFilterObj.filterList
                channel?.let {
                    if (filter.find { it == channel.id } == null) {
                        val modFilter = filter.toMutableList()
                        modFilter.add(channel.id)
                        snudayPrefObjects.channelFilter.setValue(ChannelFilter(modFilter)).subscribe()
                        channelColorManager.addNewChannelColor(channel.id).subIoObsMain()
                            .subscribe({
                                startActivity(RootActivity.createIntent(this))
                                finish()
                            }, {
                                Timber.d(it)
                                finish()
                            }).also { compositeDisposable.add(it) }
                    } else {
                        startActivity(RootActivity.createIntent(this))
                        finish()
                    }
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
            }).also { compositeDisposable.add(it) }
    }

    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable.dispose()
    }
}