package com.wafflestudio.snuday.repository

import android.content.SharedPreferences
import com.wafflestudio.snuday.network.SnudayApi
import com.wafflestudio.snuday.network.dto.LoginRequest
import com.wafflestudio.snuday.network.dto.RefreshRequest
import com.wafflestudio.snuday.preference.PrefKey
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserStatusManager @Inject constructor(
    private val snudayApi: SnudayApi,
    private val sharedPreferences: SharedPreferences
) {

    fun getSavedRefreshToken(): String? = sharedPreferences.getString(PrefKey.refreshToken, null)

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

}
