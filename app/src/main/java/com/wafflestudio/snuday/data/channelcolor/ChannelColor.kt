package com.wafflestudio.snuday.data.channelcolor

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "channel_color_table")
data class ChannelColor(
    @PrimaryKey @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "color_code") val colorCode: Int
)