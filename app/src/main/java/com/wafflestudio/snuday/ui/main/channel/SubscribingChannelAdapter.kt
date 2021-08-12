package com.wafflestudio.snuday.ui.main.channel

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wafflestudio.snuday.databinding.ItemChannelSubscribingChannelBinding
import com.wafflestudio.snuday.model.Channel
import com.wafflestudio.snuday.utils.getInflater
import com.wafflestudio.snuday.utils.setImage
import com.wafflestudio.snuday.utils.visibleOrGone

class SubscribingChannelAdapter(
    private val channelOnClickListener: (Int) -> (Unit),
    private val subscribeButtonOnClickListener: (Int) -> (Unit)
) : RecyclerView.Adapter<SubscribingChannelViewHolder>() {

    var channelList = listOf<Channel>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SubscribingChannelViewHolder {
        val binding = ItemChannelSubscribingChannelBinding.inflate(parent.getInflater(), parent, false)
        return SubscribingChannelViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubscribingChannelViewHolder, position: Int) {
        holder.run {
            render(channelList[position])
            setChannelOnClickListener(channelOnClickListener)
            setSubscribeButtonOnClickListener(subscribeButtonOnClickListener)
        }
    }

    override fun getItemCount(): Int = channelList.size
}

class SubscribingChannelViewHolder(private val binding: ItemChannelSubscribingChannelBinding)
    : RecyclerView.ViewHolder(binding.root) {

    private val channelNameTextView = binding.tagChannelName.channelNameText
    private val channelImageView = binding.imageChannel
    private val officialIcon = binding.iconOfficial
    private val privateIcon = binding.iconPrivate
    private val subscriberCountText = binding.textSubscriberCount

    private var channelId: Int = -1

    fun render(data: Channel) {
        channelId = data.id

        channelNameTextView.text = data.name
        data.image?.let { url -> channelImageView.setImage(url) }
        officialIcon.visibleOrGone(data.isOfficial)
        privateIcon.isSelected = data.isPrivate
        subscriberCountText.text = data.subCount.toString()
    }

    fun setChannelOnClickListener(listener: (Int) -> (Unit)) {
        binding.root.setOnClickListener {
            listener.invoke(channelId)
        }
    }

    fun setSubscribeButtonOnClickListener(listener: (Int) -> Unit) {
        binding.buttonSubscribe.setOnClickListener {
            listener.invoke(channelId)
        }
    }
}