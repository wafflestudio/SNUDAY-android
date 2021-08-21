package com.wafflestudio.snuday.ui.channel_detail

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wafflestudio.snuday.databinding.ItemChannelDetailNoticeBinding
import com.wafflestudio.snuday.model.Notice
import com.wafflestudio.snuday.utils.getInflater
import com.wafflestudio.snuday.utils.toPrettyStringDateWithOutYear

class ChannelDetailRecentNoticeAdapter(
    private val onNoticeClickListener: (Int) -> (Unit)
) : RecyclerView.Adapter<ChannelDetailRecentNoticeAdapter.ChannelDetailRecentNoticeViewHolder>() {

    var recentNoticeList = listOf<Notice>()

    inner class ChannelDetailRecentNoticeViewHolder(val binding: ItemChannelDetailNoticeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChannelDetailRecentNoticeViewHolder {
        val binding = ItemChannelDetailNoticeBinding.inflate(parent.getInflater(), parent, false)
        return ChannelDetailRecentNoticeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChannelDetailRecentNoticeViewHolder, position: Int) {
        val item = recentNoticeList[position]
        holder.binding.textTitle.text = item.title
        holder.binding.textDate.text = item.createdAt.toPrettyStringDateWithOutYear()

        holder.binding.root.setOnClickListener {
            onNoticeClickListener.invoke(item.id)
        }
    }

    override fun getItemCount() = recentNoticeList.size



}