package com.wafflestudio.snuday.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity(tableName = "channel_color")
data class ChannelColor(
    @PrimaryKey
    @ColumnInfo(name = "channel_id")
    val channelId: Int,
    @ColumnInfo(name = "color_code")
    val colorCode: Int
)