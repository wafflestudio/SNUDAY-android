package com.wafflestudio.snuday.data.channelcolor

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import io.reactivex.rxjava3.core.Single

@Dao
interface ChannelColorDao {

    @Query("SELECT * FROM channel_color_table")
    fun getAllChannelColors(): Single<List<ChannelColor>>

    @Insert
    fun insertChannelColor(channelColor: ChannelColor)
}