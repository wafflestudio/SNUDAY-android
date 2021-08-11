package com.wafflestudio.snuday.ui.entry

import androidx.lifecycle.ViewModel
import com.wafflestudio.snuday.repository.UserStatusManager
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val userStatusManager: UserStatusManager): ViewModel() {

    private val _id = BehaviorSubject.create<String>()
    fun isIdFit() = _id.map { id ->
        when {
            !isIdMatchGuide(id) -> false
            else -> true
        }
    }
    fun idChange(id: String) { _id.onNext(id) }

    private val _passwordMain = BehaviorSubject.create<String>()
    fun observePasswordMain() = _passwordMain.hide()
    fun isPasswordFit() = _passwordMain.map { password ->
        when {
            !isPasswordMatchGuide(password) -> false
            else -> true
        }
    }
    fun passwordMainChange(password: String) { _passwordMain.onNext(password) }

    private val _passwordCheck = BehaviorSubject.create<String>()
    fun observePasswordCheck() = _passwordCheck.hide()
    fun passwordCheckChange(password: String) { _passwordCheck.onNext(password) }

    private val _checkAll = BehaviorSubject.createDefault(false)
    fun observeCheckAll() = _checkAll.hide()
    fun toggleCheckAll() {
        val nextValue = !_checkAll.value
        _checkAll.onNext(nextValue)
        setCheckAccessTerms(nextValue)
        setCheckPrivacyPolicy(nextValue)
    }
    fun setCheckAll(value: Boolean) { _checkAll.onNext(value) }

    private val _checkAccessTerms = BehaviorSubject.createDefault(false)
    fun observeCheckAccessTerms() = _checkAccessTerms.hide()
    fun toggleCheckAccessTerms() { _checkAccessTerms.onNext(!_checkAccessTerms.value) }
    private fun setCheckAccessTerms(value: Boolean) { _checkAccessTerms.onNext(value) }

    private val _checkPrivacyPolicy = BehaviorSubject.createDefault(false)
    fun observeCheckPrivacyPolicy() = _checkPrivacyPolicy.hide()
    fun toggleCheckPrivacyPolicy() { _checkPrivacyPolicy.onNext(!_checkPrivacyPolicy.value) }
    private fun setCheckPrivacyPolicy(value: Boolean) { _checkPrivacyPolicy.onNext(value) }

    private var sentEmailPrefix = ""

    fun isPasswordAccord(): Observable<Boolean> = Observable.combineLatest(
            observePasswordMain(),
            observePasswordCheck(),
            { passwordMain, passwordCheck ->
                if (passwordMain.isNotBlank() && passwordCheck.isNotBlank()) return@combineLatest passwordMain.equals(passwordCheck)
                else return@combineLatest true
            }
    )

    fun isCheckBoxAccord(): Observable<Boolean> = Observable
        .combineLatest(
            observeCheckAccessTerms(),
            observeCheckPrivacyPolicy(),
            { accessTerms, privacyPolicy -> return@combineLatest accessTerms && privacyPolicy }
        )

    fun sendEmailVerification(emailPrefix: String): Single<String> {
        sentEmailPrefix = emailPrefix
        return userStatusManager.sendEmailVerification(emailPrefix)
    }

    fun checkEmailVerification(code: String) = userStatusManager.checkEmailVerification(sentEmailPrefix, code)

    fun isSignUpEnabled() = _checkAll.value &&
        isIdMatchGuide(_id.value) &&
        isPasswordMatchGuide(_passwordMain.value) &&
        (_passwordMain.value.equals(_passwordCheck.value))

    fun signUpUser(username: String, password: String, emailPrefix: String, firstName: String, lastName: String) =
        userStatusManager.signUpUser(username, password, "$emailPrefix@snu.ac.kr", firstName, lastName)

    private fun isIdMatchGuide(id: String): Boolean {
        if (id.length < 5) return false
        id.forEach {
            if(!(it in '0'..'9' || it in 'a'..'z')) {
                return false
            }
        }
        return true
    }

    private fun isPasswordMatchGuide(password: String): Boolean {
        if (password.length < 8) return false
        password.forEach {
            if (!(it in '0'..'9' || it in 'A'..'z')) {
                return false
            }
        }
        return true
    }
}