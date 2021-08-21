package com.wafflestudio.snuday.utils

import timber.log.Timber
import java.net.URLDecoder

fun String.getCursor(): String {
    Timber.d(this)
    val targetNum = indexOf("?cursor=")
    val urlCursorData = substring(
        targetNum,
        (this.substring(targetNum).indexOf("&q=")) + targetNum
    ).replace("?cursor=", "")
    val decodedCursor = URLDecoder.decode(urlCursorData, "UTF-8")
    return decodedCursor
}