package com.wafflestudio.snuday.ui.main.schedule

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wafflestudio.snuday.databinding.ViewCalendarCustomBinding
import com.wafflestudio.snuday.model.Event
import com.wafflestudio.snuday.view.CustomCalendarView
import java.time.LocalDate

class CalendarViewPagerAdapter(
    private val onDayClickListener: CustomCalendarView.OnDayClickListener
) : RecyclerView.Adapter<CalendarViewPagerAdapter.CalendarViewHolder>() {

    var eventList = listOf<Event>()

    inner class CalendarViewHolder(private val calendarView: CustomCalendarView)
        : RecyclerView.ViewHolder(calendarView.binding.root) {

        fun setCalendar(position: Int) {
            val offset = position - (Int.MAX_VALUE / 2)

            calendarView.setDate(LocalDate.now().plusMonths(offset.toLong()))
            calendarView.setOnDayClickListener(onDayClickListener)
            calendarView.submitEventList(eventList)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val calendarView = CustomCalendarView(parent.context)
        calendarView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        return CalendarViewHolder(calendarView)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.setCalendar(position)
    }

    override fun getItemCount() = Int.MAX_VALUE

    companion object {
        const val START_POSITION = Int.MAX_VALUE / 2
    }
}