package com.wafflestudio.snuday.ui.channel_detail.notice

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wafflestudio.snuday.databinding.ItemChannelDetailNoticeBinding
import com.wafflestudio.snuday.model.Notice
import com.wafflestudio.snuday.utils.getInflater
import com.wafflestudio.snuday.utils.toPrettyStringDateWithOutYear

class ChannelNoticeAdapter(
    private val onNoticeClickListener: (Int) -> (Unit)
) : RecyclerView.Adapter<ChannelNoticeAdapter.ChannelNoticeViewHolder>() {

    var noticeList = listOf<Notice>()

    inner class ChannelNoticeViewHolder(val binding: ItemChannelDetailNoticeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelNoticeViewHolder {
        val binding = ItemChannelDetailNoticeBinding.inflate(parent.getInflater(), parent, false)
        return ChannelNoticeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChannelNoticeViewHolder, position: Int) {
        val item = noticeList[position]
        holder.binding.textTitle.text = item.title
        holder.binding.textDate.text = item.createdAt.toPrettyStringDateWithOutYear()

        holder.binding.root.setOnClickListener {
            onNoticeClickListener.invoke(item.id)
        }
    }

    override fun getItemCount() = noticeList.size


}