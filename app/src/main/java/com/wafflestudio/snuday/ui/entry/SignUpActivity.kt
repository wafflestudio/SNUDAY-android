package com.wafflestudio.snuday.ui.entry

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import com.wafflestudio.snuday.databinding.ActivitySignUpBinding
import com.wafflestudio.snuday.utils.visibleOrGone
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignUpBinding
    private val vm: SignUpViewModel by viewModels()
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonBack.setOnClickListener {
            finish()
        }

        binding.editTextPassword.addTextChangedListener { vm.passwordMainChange(it.toString()) }
        binding.editTextPasswordCheck.addTextChangedListener { vm.passwordCheckChange(it.toString()) }

        vm.isPasswordAccord().subscribe { binding.textPasswordCheckWrong.visibleOrGone(!it) }.also { compositeDisposable.add(it) }

    }

    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable.dispose()
    }

    companion object {
        fun createIntent(context : Context): Intent {
            return Intent(context, SignUpActivity::class.java)
        }
    }
}