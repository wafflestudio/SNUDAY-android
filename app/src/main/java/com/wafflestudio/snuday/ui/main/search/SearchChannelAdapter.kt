package com.wafflestudio.snuday.ui.main.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wafflestudio.snuday.databinding.FragmentSearchBinding
import com.wafflestudio.snuday.databinding.ItemSearchBinding
import com.wafflestudio.snuday.model.ChannelDto

class SearchChannelAdapter(mContext: Context) : RecyclerView.Adapter<SearchChannelViewHolder>() {

    var channelList = mutableListOf<ChannelDto>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchChannelViewHolder {
        val binding = ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchChannelViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchChannelViewHolder, position: Int) {
        val data = channelList[position]
        holder.render(data)


    }

    override fun getItemCount(): Int = channelList.size
}

class SearchChannelViewHolder(binding: ItemSearchBinding) : RecyclerView.ViewHolder(binding.root) {
    private val channelNameView = binding.tagItemDetail.textChannelName
    private val channelDetailView = binding.textChannelDetail
    private val verificationImageView = binding.imageVerified


    fun render(data: ChannelDto) {
        channelNameView.text = data.name
        channelDetailView.text = data.description

        if (data.isOfficial) {
            verificationImageView.visibility = View.VISIBLE
        } else {
            verificationImageView.visibility = View.GONE
        }
    }

}