package com.wafflestudio.snuday.db

import androidx.room.*
import com.wafflestudio.snuday.model.ChannelColor
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

@Dao
interface ChannelColorDao {

    @Query("SELECT * FROM channel_color")
    fun getChannelColor(): Observable<List<ChannelColor>?>

    @Query("SELECT * FROM channel_color WHERE channel_id=:channelId")
    fun getChannelColorById(channelId: Int): Observable<ChannelColor?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(channelColorList: List<ChannelColor>): Completable

    @Query("DELETE FROM channel_color WHERE channel_id=:channelId")
    fun deleteByChannelId(channelId: Int): Completable
}
