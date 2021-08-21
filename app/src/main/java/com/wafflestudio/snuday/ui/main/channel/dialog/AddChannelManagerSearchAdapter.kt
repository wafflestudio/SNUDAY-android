package com.wafflestudio.snuday.ui.main.channel.dialog

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wafflestudio.snuday.databinding.ItemManagerSearchBinding
import com.wafflestudio.snuday.model.User
import com.wafflestudio.snuday.utils.getInflater

class AddChannelManagerSearchAdapter (
    private val onManagerClickListener: (user: User) -> (Unit)
) : RecyclerView.Adapter<AddChannelManagerSearchAdapter.AddChannelManagerSearchViewHolder>() {

    var searchUser = listOf<User>()

    inner class AddChannelManagerSearchViewHolder(val binding: ItemManagerSearchBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AddChannelManagerSearchViewHolder {
        val binding = ItemManagerSearchBinding.inflate(parent.getInflater(), parent, false)
        return AddChannelManagerSearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddChannelManagerSearchViewHolder, position: Int) {
        val item = searchUser[position]
        holder.binding.textManager.text = item.username
        holder.binding.root.setOnClickListener {
            onManagerClickListener.invoke(item)
        }

    }

    override fun getItemCount() = searchUser.size


}