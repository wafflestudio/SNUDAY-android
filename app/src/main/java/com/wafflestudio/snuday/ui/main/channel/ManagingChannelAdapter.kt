package com.wafflestudio.snuday.ui.main.channel

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wafflestudio.snuday.databinding.ItemChannelManagingChannelBinding
import com.wafflestudio.snuday.model.Channel
import com.wafflestudio.snuday.utils.getInflater
import com.wafflestudio.snuday.utils.setImage
import com.wafflestudio.snuday.utils.visibleOrGone

class ManagingChannelAdapter(
    private val channelOnClickListener: (Int) -> (Unit),
    private val editButtonOnClickListener: (Int) -> (Unit)
) : RecyclerView.Adapter<ManagingChannelViewHolder>() {

    var channelList = listOf<Channel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManagingChannelViewHolder {
        val binding = ItemChannelManagingChannelBinding.inflate(parent.getInflater(), parent, false)
        return ManagingChannelViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ManagingChannelViewHolder, position: Int) {
        holder.run {
            render(channelList[position])
            setChannelOnClickListener(channelOnClickListener)
            setEditButtonOnClickListener(editButtonOnClickListener)
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

    fun setEditButtonOnClickListener(listener: (Int) -> Unit) {
        binding.buttonEdit.setOnClickListener {
            listener.invoke(channelId)
        }
    }
}