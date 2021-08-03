package com.wafflestudio.snuday.ui.entry

import androidx.lifecycle.ViewModel
import com.wafflestudio.snuday.repository.UserStatusManager
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val userStatusManager: UserStatusManager): ViewModel() {

    private val _passwordMain = BehaviorSubject.create<String>()
    fun observePasswordMain() = _passwordMain.hide()
    fun passwordMainChange(password: String) { _passwordMain.onNext(password) }

    private val _passwordCheck = BehaviorSubject.create<String>()
    fun observePasswordCheck() = _passwordCheck.hide()
    fun passwordCheckChange(password: String) { _passwordCheck.onNext(password) }

    fun isPasswordAccord() = Observable.combineLatest(
            observePasswordMain(),
            observePasswordCheck(),
            { passwordMain, passwordCheck ->
                if (passwordMain.isNotBlank() && passwordCheck.isNotBlank()) return@combineLatest passwordMain.equals(passwordCheck)
                else return@combineLatest true
            }
    )


}