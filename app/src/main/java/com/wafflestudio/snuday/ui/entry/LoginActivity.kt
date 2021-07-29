package com.wafflestudio.snuday.ui.entry

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.wafflestudio.snuday.databinding.ActivityLoginBinding
import com.wafflestudio.snuday.repository.UserStatusManager
import com.wafflestudio.snuday.ui.main.RootActivity
import com.wafflestudio.snuday.utils.subIoObsMain
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    @Inject
    lateinit var userStatusManager: UserStatusManager

    private lateinit var binding: ActivityLoginBinding
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Timber.d("LoginActivity")

        binding.buttonLogin.setOnClickListener {
            userStatusManager
                .loginUser(binding.editTextId.text.toString(), binding.editTextPassword.text.toString())
                .subIoObsMain()
                .subscribe({
                    if (it == true) {
                        startActivity(RootActivity.createIntent(this))
                        finish()
                    }
                }, {
                    Timber.d(it)
                }).also { compositeDisposable.add(it) }
        }
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