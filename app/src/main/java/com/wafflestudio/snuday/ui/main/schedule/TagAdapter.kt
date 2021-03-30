package com.wafflestudio.snuday.ui.main.schedule

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wafflestudio.snuday.R
import com.wafflestudio.snuday.model.ChannelDto
import kotlinx.android.synthetic.main.item_tag.view.*

class TagAdapter() : RecyclerView.Adapter<TagViewHolder>() {

    var channels: MutableList<ChannelDto> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder =
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tag, parent, false)
            .let { TagViewHolder(it) }

    override fun getItemCount() = channels.size

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        val channel = channels[position]
        holder.render(channel)

    }
}

class TagViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val tagName: TextView = view.text_channel_name

    fun render(channel: ChannelDto) {
        val displayText = "# ${channel.name}"
        tagName.text = displayText
    }
}
