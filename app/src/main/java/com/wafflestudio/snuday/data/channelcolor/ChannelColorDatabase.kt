package com.wafflestudio.snuday.data.channelcolor

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ChannelColor::class], version = 1, exportSchema = false)
abstract class ChannelColorDatabase : RoomDatabase() {

    abstract fun channelColorDao(): ChannelColorDao

    companion object {
        @Volatile
        private var INSTANCE: ChannelColorDatabase? = null

        fun getInstance(context: Context): ChannelColorDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ChannelColorDatabase::class.java,
                    "channel_color_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance

            }
        }
    }


}