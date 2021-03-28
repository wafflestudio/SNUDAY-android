package com.wafflestudio.snuday.data.channelcolor

import androidx.room.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single

@Dao
interface ChannelColorDao {

    @Query("SELECT * FROM channel_color_table")
    fun getAllChannelColors(): Single<List<ChannelColor>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertChannelColor(channelColor: ChannelColor): Completable


}