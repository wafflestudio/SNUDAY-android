package com.wafflestudio.snuday.ui.main.channel

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wafflestudio.snuday.databinding.ItemChannelManagingChannelBinding
import com.wafflestudio.snuday.model.Channel
import com.wafflestudio.snuday.utils.getInflater
import com.wafflestudio.snuday.utils.setImage
import com.wafflestudio.snuday.utils.visibleOrGone
import timber.log.Timber

class ManagingChannelAdapter(
    private val channelOnClickListener: (Int) -> (Unit),
    private val editButtonOnClickListener: (Channel) -> (Unit),
    private val awaiterButtonOnClickListener: (Channel) -> (Unit)
) : RecyclerView.Adapter<ManagingChannelViewHolder>() {

    var channelList = mutableListOf<Channel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManagingChannelViewHolder {
        val binding = ItemChannelManagingChannelBinding.inflate(parent.getInflater(), parent, false)
        return ManagingChannelViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ManagingChannelViewHolder, position: Int) {
        holder.run {
            render(channelList[position])
            setChannelOnClickListener(channelOnClickListener)
            setEditButtonOnClickListener(editButtonOnClickListener)
            setAwaiterButtonOnClickListener(awaiterButtonOnClickListener)
        }
    }

    override fun getItemCount(): Int = channelList.size
}

class ManagingChannelViewHolder(private val binding: ItemChannelManagingChannelBinding)
    : RecyclerView.ViewHolder(binding.root) {

    private val channelNameTextView = binding.tagChannelName.channelNameText
    private val channelImageView = binding.imageChannel
    private val officialIcon = binding.iconOfficial
    private val privateIcon = binding.iconPrivate
    private val subscriberCountText = binding.textSubscriberCount

    private var channelId: Int = -1
    private var channel: Channel? = null

    fun render(data: Channel) {

        binding.buttonCheckAwaiter.visibleOrGone(false)
        binding.icAwaiterAlarm.visibleOrGone(false)
        channelId = data.id
        channel = data

        Timber.d("${data.name} have awaiter ${data.awaiter?.size}")

        data.awaiter?.let { if(it.isNotEmpty()) {
            binding.buttonCheckAwaiter.visibleOrGone(true)
            binding.icAwaiterAlarm.visibleOrGone(true)
        }
        }

        channelNameTextView.text = data.name
        data.image?.let { url -> channelImageView.setImage(url) }
        officialIcon.visibleOrGone(data.isOfficial)
        privateIcon.isSelected = data.isPrivate
        subscriberCountText.text = " ${data.subCount}"
    }

    fun setChannelOnClickListener(listener: (Int) -> (Unit)) {
        binding.root.setOnClickListener {
            listener.invoke(channelId)
        }
    }

    fun setEditButtonOnClickListener(listener: (Channel) -> Unit) {
        channel?.let { channel ->
            binding.buttonEdit.setOnClickListener {
                listener.invoke(channel)
            }
        }
    }

    fun setAwaiterButtonOnClickListener(listener: (Channel) -> Unit) {
        channel?.let { channel ->
            binding.buttonCheckAwaiter.setOnClickListener {
                listener.invoke(channel)
            }
        }
    }
}