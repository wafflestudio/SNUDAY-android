package com.wafflestudio.snuday.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wafflestudio.snuday.model.ChannelColor

@Database(entities = [ChannelColor::class], version = 1)
abstract class SnudayDatabase : RoomDatabase() {
    abstract fun channelColorDao(): ChannelColorDao
}
