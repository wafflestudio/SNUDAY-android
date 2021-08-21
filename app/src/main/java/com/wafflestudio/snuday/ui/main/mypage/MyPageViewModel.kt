package com.wafflestudio.snuday.ui.main.mypage

import androidx.lifecycle.ViewModel
import com.wafflestudio.snuday.repository.UserStatusManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val userStatusManager: UserStatusManager
): ViewModel() {

    fun getMyData() = userStatusManager.getMyData()

}