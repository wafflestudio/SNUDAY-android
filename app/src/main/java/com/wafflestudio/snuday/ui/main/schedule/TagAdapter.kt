package com.wafflestudio.snuday.ui.main.schedule

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wafflestudio.snuday.R
import kotlinx.android.synthetic.main.item_tag.view.*

class TagAdapter() : RecyclerView.Adapter<TagViewHolder>() {

    var tags: MutableList<String> = mutableListOf("본부공개", "컴퓨터공학부", "와플스튜디오")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder =
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tag, parent, false)
            .let { TagViewHolder(it) }

    override fun getItemCount() = tags.size

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        val tag = tags[position]
        holder.render(tag)

        holder.itemView.setOnClickListener {
            tags[position] = "aa"
            super.notifyDataSetChanged()
        }
    }
}

class TagViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val tagName: TextView = view.text_tag

    fun render(tag: String) {
        val displayText = "# $tag"
        tagName.text = displayText
    }
}
