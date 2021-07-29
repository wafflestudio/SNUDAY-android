package com.wafflestudio.snuday.ui.main.schedule

import androidx.lifecycle.ViewModel
import com.wafflestudio.snuday.repository.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository
): ViewModel() {

}