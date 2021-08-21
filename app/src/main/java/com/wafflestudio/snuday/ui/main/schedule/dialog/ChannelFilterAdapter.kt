package com.wafflestudio.snuday.ui.main.schedule.dialog

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wafflestudio.snuday.databinding.ItemChannelFilterBinding
import com.wafflestudio.snuday.databinding.ItemChannelFilterNegativeBinding
import com.wafflestudio.snuday.model.Channel
import com.wafflestudio.snuday.utils.getInflater
import com.wafflestudio.snuday.utils.getResource
import com.wafflestudio.snuday.utils.visibleOrGone
import timber.log.Timber

class ChannelFilterAdapter (
    private val onCrossClickListener: (channelId: Int) -> (Unit),
    private val onPlusClickListener: (channelId: Int) -> (Unit),
    private val onColorClickListener: (channelId: Int, position: Int) -> (Unit)
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var subList = listOf<Channel>()
    var filterList = listOf<Channel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            CHANNEL_POS -> {
                val binding = ItemChannelFilterBinding.inflate(parent.getInflater(), parent, false)
                ChannelFilterPosViewHolder(binding, parent.context)
            }
            CHANNEL_NEG -> {
                val binding = ItemChannelFilterNegativeBinding.inflate(parent.getInflater(), parent, false)
                ChannelFilterNegViewHolder(binding, parent.context)
            }
            else -> throw IllegalStateException("viewType must be 0 or 1")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = subList[position]
        when (holder) {
            is ChannelFilterPosViewHolder -> {
                holder.binding.tagChannelName.apply {
                    cardView.setCardBackgroundColor(item.channelColor.getResource(holder.parentContext))
                    channelNameText.text = if (item.isPersonal) "내 일정" else item.name
                }

                holder.binding.buttonDelete.visibleOrGone(!item.isPersonal)

                holder.binding.buttonDelete.setOnClickListener {
                    onCrossClickListener.invoke(item.id)
                }

                holder.binding.buttonColorChange.setOnClickListener {
                    onColorClickListener.invoke(item.id, position)
                }
            }
            is ChannelFilterNegViewHolder -> {
                holder.binding.tagChannelName.apply {
                    cardView.setCardBackgroundColor(item.channelColor.getResource(((holder.parentContext))))
                    channelNameText.text = item.name
                    cardView.alpha = 0.4F
                }
                holder.binding.buttonAdd.setOnClickListener {
                    Timber.d("plus clicked ${item.name}")
                    onPlusClickListener.invoke(item.id)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = subList[position]
        return if (filterList.find { filter -> filter.id == item.id } != null)  CHANNEL_POS
        else CHANNEL_NEG
    }

    override fun getItemCount(): Int = subList.size

    inner class ChannelFilterPosViewHolder(val binding: ItemChannelFilterBinding, val parentContext: Context) : RecyclerView.ViewHolder(binding.root)

    inner class ChannelFilterNegViewHolder(val binding: ItemChannelFilterNegativeBinding, val parentContext: Context) : RecyclerView.ViewHolder(binding.root)

    companion object {
        private const val CHANNEL_POS = 0
        private const val CHANNEL_NEG = 1
    }

}