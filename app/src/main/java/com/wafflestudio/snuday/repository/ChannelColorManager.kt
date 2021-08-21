package com.wafflestudio.snuday.repository

import com.wafflestudio.snuday.db.ChannelColorDao
import com.wafflestudio.snuday.model.ChannelColor
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChannelColorManager @Inject constructor(
    private val channelColorDao: ChannelColorDao
) {

    fun getChannelColor() = channelColorDao.getChannelColor()

    fun getChannelColorById(channelId: Int) = channelColorDao.getChannelColorById(channelId)

    fun updateChannelColor(channelColorList: List<ChannelColor>) = channelColorDao.insert(channelColorList)

    fun addNewChannelColor(channelId: Int): Completable {
        val colorCode = createRandomColorCode()
        return updateChannelColor(listOf(ChannelColor(channelId, colorCode)))
    }

    fun setChannelColor(channelId: Int, colorCode: Int): Completable {
        return updateChannelColor(listOf(ChannelColor(channelId, colorCode)))
    }

    private fun createRandomColorCode(): Int = (0..8).random()

}