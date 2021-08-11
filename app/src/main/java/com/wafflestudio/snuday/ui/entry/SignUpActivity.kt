package com.wafflestudio.snuday.ui.entry

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import com.wafflestudio.snuday.R
import com.wafflestudio.snuday.databinding.ActivitySignUpBinding
import com.wafflestudio.snuday.utils.showToast
import com.wafflestudio.snuday.utils.subIoObsMain
import com.wafflestudio.snuday.utils.visibleOrGone
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import timber.log.Timber

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignUpBinding
    private val vm: SignUpViewModel by viewModels()
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonBack.setOnClickListener { finish() }

        binding.buttonEmailVerificationSend.setOnClickListener {
            vm.sendEmailVerification(binding.editTextEmailHead.text.toString()).subIoObsMain()
                .subscribe({
                    showToast(it)
                    binding.editTextEmailHead.apply {
                        inputType = InputType.TYPE_NULL
                        setTextColor(resources.getColor(R.color.snuday_hint_text_gray))
                    }
                }, {
                    showToast("네트워크가 불안정합니다.")
                    Timber.d(it)
                }).also { compositeDisposable.add(it) }
        }

        binding.buttonEmailVerificationCheck.setOnClickListener {
            vm.checkEmailVerification(binding.editTextEmailVerificationCode.text.toString()).subIoObsMain()
                .subscribe({
                    showToast(it)
                    binding.editTextEmailVerificationCode.apply {
                        inputType = InputType.TYPE_NULL
                        setTextColor(resources.getColor(R.color.snuday_hint_text_gray))
                    }
                }, {
                    Timber.d(it)
                }).also { compositeDisposable.add(it) }
        }

        binding.buttonSignUp.setOnClickListener {
            if (vm.isSignUpEnabled()) {
                vm.signUpUser(
                    username = binding.editTextId.text.toString(),
                    password = binding.editTextPassword.text.toString(),
                    emailPrefix = binding.editTextEmailHead.text.toString(),
                    firstName = binding.editTextFirstName.text.toString(),
                    lastName = binding.editTextLastName.text.toString()
                ).subIoObsMain()
                    .subscribe({
                        showToast("성공적으로 회원가입 되었습니다!\n가입된 정보로 로그인해주세요!")
                        finish()
                    }, {
                        showToast("회원가입에 실패하였습니다.")
                        Timber.d(it)
                    })
            } else {
                showToast("회원가입을 진행할 수 없습니다.")
                Timber.d("NOT!")
            }
        }

        binding.buttonCheckAll.setOnClickListener { vm.toggleCheckAll() }
        binding.buttonCheckAccessTerms.setOnClickListener { vm.toggleCheckAccessTerms() }
        binding.buttonCheckPrivacyPolicy.setOnClickListener { vm.toggleCheckPrivacyPolicy() }

        binding.editTextId.addTextChangedListener { vm.idChange(it.toString()) }
        binding.editTextPassword.addTextChangedListener { vm.passwordMainChange(it.toString()) }
        binding.editTextPasswordCheck.addTextChangedListener { vm.passwordCheckChange(it.toString()) }
        binding.editTextLastName.addTextChangedListener { binding.warningLastName.visibleOrGone(it.toString().isBlank()) }
        binding.editTextFirstName.addTextChangedListener { binding.warningFirstName.visibleOrGone(it.toString().isBlank()) }

        vm.isIdFit().subscribe {
            binding.warningId.visibleOrGone(!it)
        }.also { compositeDisposable.add(it) }

        vm.isPasswordFit().subscribe {
            binding.warningPassword.visibleOrGone(!it)
        }.also { compositeDisposable.add(it) }

        vm.isPasswordAccord().subscribe {
            binding.textPasswordCheckWrong.visibleOrGone(!it)
            binding.warningPasswordCheck.visibleOrGone(!it)
        }.also { compositeDisposable.add(it) }

        vm.isCheckBoxAccord().subscribe { vm.setCheckAll(it) }
        vm.observeCheckAll().subscribe { binding.buttonCheckAll.isSelected = it }
        vm.observeCheckAccessTerms().subscribe { binding.buttonCheckAccessTerms.isSelected = it }
        vm.observeCheckPrivacyPolicy().subscribe { binding.buttonCheckPrivacyPolicy.isSelected = it }
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