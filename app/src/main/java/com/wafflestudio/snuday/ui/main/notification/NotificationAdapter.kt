package com.wafflestudio.snuday.ui.main.notification

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wafflestudio.snuday.databinding.ItemNotificationBinding
import com.wafflestudio.snuday.model.ChannelColor
import com.wafflestudio.snuday.utils.getInflater
import com.wafflestudio.snuday.utils.getResource

class NotificationAdapter(
    private val onNoticeClickListener: (Int, String, Int) -> (Unit)
) : RecyclerView.Adapter<NotificationViewHolder>() {

    var noticeList = listOf<NotificationItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = ItemNotificationBinding.inflate(parent.getInflater(), parent, false)
        return NotificationViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val data = noticeList[position]
        holder.render(data, onNoticeClickListener)
    }

    override fun getItemCount(): Int = noticeList.size

    data class NotificationItem(
        val id: Int,
        val channelName: String,
        val title: String,
        val channelId: Int,
        val contents: String,
        val channelColor: ChannelColor?
    )
}

class NotificationViewHolder(binding: ItemNotificationBinding, private val parentContext: Context) : RecyclerView.ViewHolder(binding.root) {

    private val layout = binding.root
    private val channelNameText = binding.tagChannel.channelNameText
    private val titleText = binding.textTitle
    private val contentText = binding.textContents
    private val channelTag = binding.tagChannel

    fun render(
        notice: NotificationAdapter.NotificationItem,
        onNoticeClickListener: (Int, String, Int) -> Unit
    ) {
        layout.setOnClickListener {
            onNoticeClickListener.invoke(notice.channelId, notice.channelName, notice.id)
        }
        channelNameText.text = notice.channelName
        titleText.text = notice.title
        contentText.text = notice.contents
        channelTag.cardView.setCardBackgroundColor(notice.channelColor.getResource(parentContext))
    }
}