package com.wafflestudio.snuday.ui.main.notification

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wafflestudio.snuday.databinding.ItemNotificationBinding
import com.wafflestudio.snuday.databinding.ItemTagBinding
import com.wafflestudio.snuday.model.NoticeDto

class NotificationAdapter : RecyclerView.Adapter<NotificationViewHolder>() {

    var notificationList: MutableList<NoticeDto> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val data = notificationList[position]
        holder.render(data)
    }

    override fun getItemCount(): Int = notificationList.size

}

class NotificationViewHolder(binding: ItemNotificationBinding) : RecyclerView.ViewHolder(binding.root) {
    private val titleView = binding.textTitle
    private val channelTextView = binding.tagItemDetail.textChannelName

    fun render(data: NoticeDto) {
        titleView.text = data.title
    }

}