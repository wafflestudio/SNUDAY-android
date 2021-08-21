package com.wafflestudio.snuday.ui.main.search

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wafflestudio.snuday.databinding.ItemRecommendedChannelBinding
import com.wafflestudio.snuday.model.Channel
import com.wafflestudio.snuday.utils.getInflater
import com.wafflestudio.snuday.utils.setImage
import com.wafflestudio.snuday.utils.visibleOrGone

class RecommendChannelAdapter(
    private val onChannelClickListener: (Int) -> (Unit),
    private val onSubscribeClickListener: (Int, Boolean) -> (Unit)
) : RecyclerView.Adapter<RecommendChannelViewHolder>() {

    var channelList = listOf<Channel>()
    var subscribingList = listOf<Channel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendChannelViewHolder {
        val binding = ItemRecommendedChannelBinding.inflate(parent.getInflater(), parent, false)
        return RecommendChannelViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecommendChannelViewHolder, position: Int) {
        val channel = channelList[position]
        holder.render(channel, subscribingList, onChannelClickListener, onSubscribeClickListener)
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

    private val container = binding.layoutChannelDataContainer
    private val subscribeButton = binding.buttonSubscribe

    fun render(
        channel: Channel,
        subscribingList: List<Channel>,
        onChannelClickListener: (Int) -> (Unit),
        onSubscribeClickListener: (Int, Boolean) -> (Unit)
    ) {
        channel.image?.let { url -> imageView.setImage(url) }
        channelNameTag.channelNameText.text = channel.name
        officialIcon.visibleOrGone(channel.isOfficial)
        channelDetail.text = channel.description

        subscribeButton.isSelected = subscribingList.find { it.id == channel.id }?.let{ true } ?: false

        container.setOnClickListener { onChannelClickListener.invoke(channel.id) }
        subscribeButton.setOnClickListener { onSubscribeClickListener.invoke(channel.id, subscribeButton.isSelected) }
    }


}