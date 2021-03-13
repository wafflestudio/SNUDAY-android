package com.wafflestudio.snuday.data.channelcolor

import android.content.Context
import io.reactivex.rxjava3.core.Single

class ChannelColorRepository(context: Context) {

    private val channelColorDao = ChannelColorDatabase.getInstance(context).channelColorDao()
    private var colorList: List<ChannelColor>? = null

    fun getAllChannelColors(): Single<List<ChannelColor>> = colorList?.let { Single.just(it) }
        ?: channelColorDao.getAllChannelColors().doOnSuccess {
            colorList = it
        }

}