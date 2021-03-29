package com.wafflestudio.snuday.ui.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.wafflestudio.snuday.databinding.ActivityLoginBinding
import com.wafflestudio.snuday.preference.PreferenceHelper
import com.wafflestudio.snuday.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonLogin.setOnClickListener {
            val username = binding.editTextId.text.toString()
            val password = binding.editTextPassword.text.toString()

            viewModel.login(username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    PreferenceHelper.setString(this, PreferenceHelper.ACCESS_TOKEN, response.access)
                    PreferenceHelper.setString(this, PreferenceHelper.REFRESH_TOKEN, response.refresh)

                    startActivity(MainActivity.createIntent(this))
                    finish()
                }, {
                    Toast.makeText(this, "로그인 정보가 옳지 않습니다.", Toast.LENGTH_SHORT).show()
                    Timber.d(it)
                }).also { compositeDisposable.add(it) }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }
}