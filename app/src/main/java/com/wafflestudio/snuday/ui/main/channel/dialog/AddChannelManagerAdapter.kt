package com.wafflestudio.snuday.ui.main.channel.dialog

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wafflestudio.snuday.databinding.ItemManagerBinding
import com.wafflestudio.snuday.model.User
import com.wafflestudio.snuday.utils.getInflater
import com.wafflestudio.snuday.utils.visibleOrGone

class AddChannelManagerAdapter : RecyclerView.Adapter<AddChannelManagerAdapter.AddChannelManagerViewHolder>() {

    var userList = mutableListOf<User>()

    inner class AddChannelManagerViewHolder(val binding: ItemManagerBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddChannelManagerViewHolder {
        val binding = ItemManagerBinding.inflate(parent.getInflater(), parent, false)
        return AddChannelManagerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddChannelManagerViewHolder, position: Int) {
        val item = userList[position]
        holder.binding.apply {
            if (position == 0) {
                textManager.text = "나"
                buttonDelete.visibleOrGone(false)
            } else {
                textManager.text = item.username
                buttonDelete.setOnClickListener {
                    userList.removeAt(position)
                    this@AddChannelManagerAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun getItemCount() = userList.size


}