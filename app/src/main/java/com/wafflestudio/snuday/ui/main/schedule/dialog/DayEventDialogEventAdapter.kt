package com.wafflestudio.snuday.ui.main.schedule.dialog

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wafflestudio.snuday.databinding.ItemEventInDayBinding
import com.wafflestudio.snuday.model.Event
import com.wafflestudio.snuday.utils.getInflater
import com.wafflestudio.snuday.utils.getResource
import com.wafflestudio.snuday.utils.toPrettyString
import com.wafflestudio.snuday.utils.toPrettyStringDateWithOutYear

class DayEventDialogEventAdapter(private val onEventClickListener: OnEventClickListener) :
    RecyclerView.Adapter<DayEventDialogEventAdapter.EventViewHolder>() {

    var eventList = listOf<Event>()

    inner class EventViewHolder(val binding: ItemEventInDayBinding, val context: Context) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemEventInDayBinding.inflate(parent.getInflater(), parent, false)
        return EventViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = eventList[position]
        holder.binding.apply {
            val grd = iconChannelColor.background as GradientDrawable
            grd.setColor(event.channelColor.getResource(holder.context))
            iconChannelColor.background = grd
            textTitle.text = event.title
            val dateText = event.startDate.toPrettyStringDateWithOutYear() + " " +
                (if (event.hasTime) event.startTime?.toPrettyString() + " " else "") +
                " ~ " + event.dueDate.toPrettyStringDateWithOutYear() + " " +
                (if (event.hasTime) event.dueTime?.toPrettyString() + " " else "")
            textDate.text = dateText
            root.setOnClickListener {
                onEventClickListener.onClick(event)
            }
        }
    }

    override fun getItemCount() = eventList.size

    interface OnEventClickListener {
        fun onClick(event: Event)
    }
}