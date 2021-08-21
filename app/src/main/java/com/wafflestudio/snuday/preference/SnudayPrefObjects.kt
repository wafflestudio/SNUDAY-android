package com.wafflestudio.snuday.preference

import android.content.SharedPreferences
import com.wafflestudio.snuday.model.ChannelFilter
import com.wafflestudio.snuday.moshi.Serializer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SnudayPrefObjects @Inject constructor(
    sharedPreferences: SharedPreferences,
    serializer: Serializer
) {

    val channelFilter: Preference<ChannelFilter> =
        Preference(
            "channelFilter",
            ChannelFilter(listOf()),
            sharedPreferences,
            serializer,
            ChannelFilter::class.java
        )

}