package com.wafflestudio.snuday.ui.awaiter

import androidx.lifecycle.ViewModel
import com.wafflestudio.snuday.repository.ChannelDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AwaiterViewModel @Inject constructor(
    private val channelDataRepository: ChannelDataRepository
) : ViewModel() {

    fun getAwaiter(channelId: Int) = channelDataRepository.getAwaiter(channelId)

    fun acceptAwaiter(channelId: Int, userId: Int) = channelDataRepository.allowAwaiter(channelId, userId)

    fun rejectAwaiter(channelId: Int, userId: Int) = channelDataRepository.rejectAwaiter(channelId, userId)

}