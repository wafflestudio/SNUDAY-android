package com.wafflestudio.snuday.ui.main.search

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wafflestudio.snuday.databinding.ItemSearchedChannelBinding
import com.wafflestudio.snuday.model.Channel
import com.wafflestudio.snuday.utils.getInflater
import com.wafflestudio.snuday.utils.setImage
import com.wafflestudio.snuday.utils.visibleOrGone

class SearchChannelAdapter(
    private val onChannelClickListener: (Int, Boolean) -> (Unit),
    private val onSubscribeClickListener: (Int, Boolean, Boolean) -> (Unit)
) : RecyclerView.Adapter<SearchChannelViewHolder>() {

    var channelList = listOf<Channel>()
    var subscribingList = listOf<Channel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchChannelViewHolder {
        val binding = ItemSearchedChannelBinding.inflate(parent.getInflater(), parent, false)
        return SearchChannelViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchChannelViewHolder, position: Int) {
        val channel = channelList[position]
        holder.render(channel, subscribingList, onChannelClickListener, onSubscribeClickListener)
    }

    override fun getItemCount(): Int = channelList.size
}

class SearchChannelViewHolder(binding: ItemSearchedChannelBinding) : RecyclerView.ViewHolder(binding.root) {

    private val imageView = binding.imageChannel
    private val channelNameTag = binding.tagChannelName
    private val officialIcon = binding.iconOfficial
    private val channelDetail = binding.textChannelDetail
    private val container = binding.layoutChannelDataContainer
    private val subscribeButton = binding.buttonSubscribe
    private val privateIcon = binding.iconPrivate

    fun render(
        channel: Channel,
        subscribingList: List<Channel>,
        onChannelClickListener: (Int, Boolean) -> (Unit),
        onSubscribeClickListener: (Int, Boolean, Boolean) -> (Unit)
    ) {
        channel.image?.let { url -> imageView.setImage(url) }
        channelNameTag.channelNameText.text = channel.name
        officialIcon.visibleOrGone(channel.isOfficial)
        channelDetail.text = channel.description
        privateIcon.visibleOrGone(channel.isPrivate)
        privateIcon.isSelected = channel.isPrivate

        subscribeButton.isSelected = subscribingList.find { it.id == channel.id }?.let{ true } ?: false

        container.setOnClickListener { onChannelClickListener.invoke(channel.id, channel.isPrivate) }
        subscribeButton.setOnClickListener { onSubscribeClickListener.invoke(channel.id, subscribeButton.isSelected, channel.isPrivate) }
    }

}