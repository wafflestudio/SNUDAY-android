package com.wafflestudio.snuday.utils

import android.content.Context
import android.provider.Settings.Global.getString
import com.wafflestudio.snuday.R
import com.wafflestudio.snuday.SnudayApplication
import java.util.*

fun Calendar.getMonthText(context: Context): String {
    return context.getString( when(this.get(Calendar.MONTH)) {
        1 -> R.string.January
        2 -> R.string.February
        3 -> R.string.March
        4 -> R.string.April
        5 -> R.string.May
        6 -> R.string.June
        7 -> R.string.July
        8 -> R.string.August
        9 -> R.string.September
        10 -> R.string.October
        11 -> R.string.November
        12 -> R.string.December
        else -> R.string.January
    })
}

fun Calendar.getYearText(): String {
    return this.get(Calendar.YEAR).toString()
}