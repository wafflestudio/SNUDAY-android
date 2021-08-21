package com.wafflestudio.snuday.utils

import android.content.Context
import androidx.core.content.ContextCompat
import com.wafflestudio.snuday.R
import com.wafflestudio.snuday.model.ChannelColor

fun ChannelColor?.getResource(context: Context): Int {
    if (this == null) return ContextCompat.getColor(context, R.color.snutt_color_1)

    return when(this.colorCode) {
        0 -> ContextCompat.getColor(context, R.color.snutt_color_1)
        1 -> ContextCompat.getColor(context, R.color.snutt_color_2)
        2 -> ContextCompat.getColor(context, R.color.snutt_color_3)
        3 -> ContextCompat.getColor(context, R.color.snutt_color_4)
        4 -> ContextCompat.getColor(context, R.color.snutt_color_5)
        5 -> ContextCompat.getColor(context, R.color.snutt_color_6)
        6 -> ContextCompat.getColor(context, R.color.snutt_color_7)
        7 -> ContextCompat.getColor(context, R.color.snutt_color_8)
        8 -> ContextCompat.getColor(context, R.color.snutt_color_9)
        else -> ContextCompat.getColor(context, R.color.snutt_color_1)
    }
}