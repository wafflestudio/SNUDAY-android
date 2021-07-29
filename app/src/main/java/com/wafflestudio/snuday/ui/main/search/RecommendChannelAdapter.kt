package com.wafflestudio.snuday.ui.main.search

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wafflestudio.snuday.databinding.ItemRecommendedChannelBinding
import com.wafflestudio.snuday.model.Channel
import com.wafflestudio.snuday.utils.getInflater
import com.wafflestudio.snuday.utils.setImage
import com.wafflestudio.snuday.utils.visibleOrGone

class RecommendChannelAdapter : RecyclerView.Adapter<RecommendChannelViewHolder>() {

    var channelList = listOf<Channel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendChannelViewHolder {
        val binding = ItemRecommendedChannelBinding.inflate(parent.getInflater(), parent, false)
        return RecommendChannelViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecommendChannelViewHolder, position: Int) {
        val channel = channelList[position]
        holder.render(channel)
    }

    override fun getItemCount(): Int = channelList.size

}

class RecommendChannelViewHolder(
    binding: ItemRecommendedChannelBinding
) : RecyclerView.ViewHolder(binding.root) {

    private val imageView = binding.imageChannel
    private val channelNameTag = binding.tagChannelName
    private val officialIcon = binding.iconOfficial
    private val channelDetail = binding.textChannelDetail

    fun render(channel: Channel) {
        channel.image?.let { url -> imageView.setImage(url) }
        channelNameTag.channelNameText.text = channel.name
        officialIcon.visibleOrGone(channel.isOfficial)
        channelDetail.text = channel.description
    }

}