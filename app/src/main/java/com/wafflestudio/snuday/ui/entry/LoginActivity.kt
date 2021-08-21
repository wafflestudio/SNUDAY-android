package com.wafflestudio.snuday.ui.entry

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.wafflestudio.snuday.BuildConfig
import com.wafflestudio.snuday.R
import com.wafflestudio.snuday.databinding.ActivityLoginBinding
import com.wafflestudio.snuday.model.ChannelFilter
import com.wafflestudio.snuday.preference.SnudayPrefObjects
import com.wafflestudio.snuday.repository.ChannelColorManager
import com.wafflestudio.snuday.repository.UserDataRepository
import com.wafflestudio.snuday.repository.UserStatusManager
import com.wafflestudio.snuday.ui.main.RootActivity
import com.wafflestudio.snuday.utils.showToast
import com.wafflestudio.snuday.utils.subIoObsMain
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import timber.log.Timber
import java.net.UnknownHostException
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    @Inject
    lateinit var userStatusManager: UserStatusManager
    @Inject
    lateinit var snudayPrefObjects: SnudayPrefObjects
    @Inject
    lateinit var userDataRepository: UserDataRepository
    @Inject
    lateinit var channelColorManager: ChannelColorManager

    private lateinit var binding: ActivityLoginBinding
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Timber.d("LoginActivity")

        binding.buttonLogin.setOnClickListener {

            // FIXME : For debug
            if(BuildConfig.DEBUG) {
                if (binding.editTextId.text.toString().equals("testing")) {
                    startActivity(RootActivity.createIntent(this))
                    finish()
                    return@setOnClickListener
                }
            }

            userStatusManager
                .loginUser(binding.editTextId.text.toString(), binding.editTextPassword.text.toString())
                .subIoObsMain()
                .subscribe({
                    if (it == true) {
                        checkPersonalChannel()
                    }
                }, {
                    Timber.d(it)
                }).also { compositeDisposable.add(it) }
        }

        binding.buttonSignUp.setOnClickListener {
            startActivity(SignUpActivity.createIntent(this))
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
                }
            }).also { compositeDisposable.add(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    companion object {
        fun createIntent(context : Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }
}