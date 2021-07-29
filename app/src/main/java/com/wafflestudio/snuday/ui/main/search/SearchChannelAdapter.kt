package com.wafflestudio.snuday.ui.main.search

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wafflestudio.snuday.databinding.ItemSearchedChannelBinding
import com.wafflestudio.snuday.model.Channel
import com.wafflestudio.snuday.utils.getInflater
import com.wafflestudio.snuday.utils.setImage
import com.wafflestudio.snuday.utils.visibleOrGone

class SearchChannelAdapter : RecyclerView.Adapter<SearchChannelViewHolder>() {

    var channelList = listOf<Channel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchChannelViewHolder {
        val binding = ItemSearchedChannelBinding.inflate(parent.getInflater(), parent, false)
        return SearchChannelViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchChannelViewHolder, position: Int) {
        val channel = channelList[position]
        holder.render(channel)
    }

    override fun getItemCount(): Int = channelList.size
}

class SearchChannelViewHolder(binding: ItemSearchedChannelBinding) : RecyclerView.ViewHolder(binding.root) {

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