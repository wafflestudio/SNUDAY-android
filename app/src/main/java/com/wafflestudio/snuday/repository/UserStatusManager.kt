package com.wafflestudio.snuday.repository

import android.content.SharedPreferences
import com.wafflestudio.snuday.network.SnudayApi
import com.wafflestudio.snuday.network.dto.*
import com.wafflestudio.snuday.preference.PrefKey
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserStatusManager @Inject constructor(
    private val snudayApi: SnudayApi,
    private val sharedPreferences: SharedPreferences
) {

    fun getSavedRefreshToken(): String? = sharedPreferences.getString(PrefKey.refreshToken, null)

    fun signUpUser(username: String, password: String, email: String, firstName: String, lastName: String) =
        snudayApi.signUpUser(SignUpRequest(username, email, password, firstName, lastName))

    fun loginUser(username: String, password: String) =
        snudayApi.loginUser(LoginRequest(username, password))
            .map {
                sharedPreferences.edit()
                    .putString(PrefKey.accessToken, it.accessToken)
                    .putString(PrefKey.refreshToken, it.refreshToken)
                    .apply()

                true
            }

    fun refreshUser(refreshToken: String) = snudayApi.refreshUser(RefreshRequest(refreshToken))
        .map {
            sharedPreferences.edit()
                .putString(PrefKey.accessToken, it.accessToken)
                .apply()

            true
        }

    fun sendEmailVerification(emailPrefix: String) = snudayApi.sendEmailVerification(SendEmailVerificationRequest(emailPrefix))

    fun checkEmailVerification(emailPrefix: String, code: String) = snudayApi.checkEmailVerification(CheckEmailVerificationRequest(emailPrefix, code))
}
