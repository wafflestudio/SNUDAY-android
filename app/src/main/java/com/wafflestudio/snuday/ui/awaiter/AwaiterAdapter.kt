package com.wafflestudio.snuday.ui.awaiter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wafflestudio.snuday.databinding.ItemAwaiterBinding
import com.wafflestudio.snuday.model.User
import com.wafflestudio.snuday.utils.getInflater

class AwaiterAdapter(
    private val onAcceptClickListener: (Int) -> (Unit),
    private val onRejectClickListener: (Int) -> (Unit)
) : RecyclerView.Adapter<AwaiterAdapter.AwaiterViewHolder>() {

    var awaiterList = listOf<User>()

    inner class AwaiterViewHolder(val binding: ItemAwaiterBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AwaiterViewHolder {
        val binding = ItemAwaiterBinding.inflate(parent.getInflater(), parent, false)
        return AwaiterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AwaiterViewHolder, position: Int) {
        val item = awaiterList[position]
        holder.binding.textUsername.text = item.username
        holder.binding.buttonAccept.setOnClickListener {
            onAcceptClickListener.invoke(item.id)
        }
        holder.binding.buttonReject.setOnClickListener {
            onRejectClickListener.invoke(item.id)
        }
    }

    override fun getItemCount() = awaiterList.size


}