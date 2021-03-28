package com.wafflestudio.snuday.data.channelcolor

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "channel_color_table")
data class ChannelColor(
    @ColumnInfo(name = "event_id") val eventId: Long,
    @ColumnInfo(name = "color_code") val colorCode: Int
) {
    @PrimaryKey(autoGenerate = true) var id: Long = 0
}