package com.wafflestudio.snuday.ui.main.schedule

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wafflestudio.snuday.databinding.ItemChannelNameBinding
import com.wafflestudio.snuday.databinding.ItemScheduleTagBinding
import com.wafflestudio.snuday.model.Channel
import com.wafflestudio.snuday.utils.getInflater
import com.wafflestudio.snuday.utils.getResource

class ChannelTagAdapter : RecyclerView.Adapter<ChannelTagAdapter.ChannelTagViewHolder>() {

    var channelList = listOf<Channel>()

    inner class ChannelTagViewHolder(val binding: ItemScheduleTagBinding, val parentContext: Context) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelTagViewHolder {
        val binding = ItemScheduleTagBinding.inflate(parent.getInflater(), parent, false)
        return ChannelTagViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: ChannelTagViewHolder, position: Int) {
        val channel = channelList[position]
        holder.apply {
            binding.tag.cardView.setCardBackgroundColor(channel.channelColor.getResource(parentContext))
            binding.tag.channelNameText.text = if (channel.isPersonal) "내 일정" else channel.name
        }
    }

    override fun getItemCount(): Int = channelList.size
}