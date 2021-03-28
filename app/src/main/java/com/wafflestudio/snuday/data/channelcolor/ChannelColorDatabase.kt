package com.wafflestudio.snuday.data.channelcolor

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [ChannelColor::class], version = 1, exportSchema = false)
abstract class ChannelColorDatabase : RoomDatabase() {

    abstract fun channelColorDao(): ChannelColorDao

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: ChannelColorDatabase? = null

        fun getInstance(context: Context): ChannelColorDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context,
                    ChannelColorDatabase::class.java,
                    "channel_color_database"
                ).fallbackToDestructiveMigration().build()
            }
        }
    }
}
