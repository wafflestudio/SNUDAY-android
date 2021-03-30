package com.wafflestudio.snuday.utils

import android.content.Context
import android.provider.Settings.Global.getString
import com.wafflestudio.snuday.R
import com.wafflestudio.snuday.SnudayApplication
import java.util.*

fun Calendar.getMonthText(context: Context): String {
    return context.getString( when(this.get(Calendar.MONTH)) {
        0 -> R.string.January
        1 -> R.string.February
        2 -> R.string.March
        3 -> R.string.April
        4 -> R.string.May
        5 -> R.string.June
        6 -> R.string.July
        7 -> R.string.August
        8 -> R.string.September
        9 -> R.string.October
        10 -> R.string.November
        11 -> R.string.December
        else -> R.string.January
    })
}

fun Calendar.getYearText(): String {
    return this.get(Calendar.YEAR).toString() + "년"
}