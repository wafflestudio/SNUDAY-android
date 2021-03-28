package com.wafflestudio.snuday.color

import android.content.Context
import androidx.core.content.ContextCompat
import com.wafflestudio.snuday.R
import com.wafflestudio.snuday.data.channelcolor.ChannelColor
import com.wafflestudio.snuday.data.channelcolor.ChannelColorRepository
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton


class ChannelColorManager (private val context: Context) {

    private val channelColorRepository: ChannelColorRepository by lazy { ChannelColorRepository(context) }

    companion object {

        private var INSTANCE: ChannelColorManager? = null

        fun getInstance(context: Context): ChannelColorManager =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ChannelColorManager(context).also {
                    INSTANCE = it
                }
            }
    }

    init {
        channelColorRepository.getAllChannelColors()
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
            .doOnSuccess {
            updateColorList()
        }
    }

    private fun updateColorList() = channelColorRepository
        .getChannelColors()
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .subscribe { colorList = it }

    private var colorList: List<ChannelColor> = listOf()

    fun getColorById(id: Long, context: Context): Int {
        // check existing id first
        colorList.forEach { channelColor ->
            if (channelColor.id == id) return getResource(channelColor.colorCode, context)
        }

        // if not, make color for id
        return getResource(makeColorOfId(id), context)
    }

    // set color and return colorCode
    private fun makeColorOfId(id: Long): Int {
        val colorCount = arrayListOf<Int>()

        colorList.forEach {
            colorCount[it.colorCode]++
        }

        // Pair<colorCode, count>
        var minCount = Pair(-1, Int.MAX_VALUE)
        colorCount.forEachIndexed { colorCode, count ->
            if (minCount.second > count) minCount = Pair(colorCode, count)
        }

        channelColorRepository
            .insertChannelColors(ChannelColor(id, minCount.first))
            .subscribeOn(Schedulers.io())


        updateColorList()

        return minCount.first

    }

    private fun getResource(id: Int, context: Context): Int {
        return when (id) {
            0 -> ContextCompat.getColor(context, R.color.waffle_red)
            1 -> ContextCompat.getColor(context, R.color.waffle_orange)
            2 -> ContextCompat.getColor(context, R.color.waffle_yellow)
            3 -> ContextCompat.getColor(context, R.color.waffle_yellow_green)
            4 -> ContextCompat.getColor(context, R.color.waffle_green)
            5 -> ContextCompat.getColor(context, R.color.waffle_sky_blue)
            6 -> ContextCompat.getColor(context, R.color.waffle_blue)
            7 -> ContextCompat.getColor(context, R.color.waffle_dark_blue)
            8 -> ContextCompat.getColor(context, R.color.waffle_purple)
            else -> ContextCompat.getColor(context, R.color.waffle_red)
        }
    }


}