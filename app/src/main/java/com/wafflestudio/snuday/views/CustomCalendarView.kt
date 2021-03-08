package com.wafflestudio.snuday.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.DrawableWrapper
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Space
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.doOnLayout
import androidx.core.view.marginTop
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wafflestudio.snuday.R
import com.wafflestudio.snuday.databinding.ItemWeekBinding
import com.wafflestudio.snuday.databinding.ViewCalendarCustomBinding
import com.wafflestudio.snuday.models.EventDto
import kotlinx.android.synthetic.main.item_day.view.*
import kotlinx.android.synthetic.main.item_week.view.*
import timber.log.Timber
import java.util.*

class CustomCalendarView : ConstraintLayout {

    private var binding: ViewCalendarCustomBinding
    private lateinit var date: Date
    private lateinit var calendarAdapter: CustomCalendarAdapter
    private lateinit var calendarLayoutManager: LinearLayoutManager

    constructor(context: Context) : super(context) {
        binding = ViewCalendarCustomBinding.inflate(LayoutInflater.from(context), this)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        binding = ViewCalendarCustomBinding.inflate(LayoutInflater.from(context), this)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        binding = ViewCalendarCustomBinding.inflate(LayoutInflater.from(context), this)
    }

    fun setDatesToCalendar(dateData: Date) {

        Timber.d("custom calendar width ${this.width}")

        this.date = dateData

        calendarAdapter = CustomCalendarAdapter()
        calendarLayoutManager = LinearLayoutManager(context)
        binding.recyclerViewCalendar.adapter = calendarAdapter
        binding.recyclerViewCalendar.layoutManager = calendarLayoutManager

        calendarAdapter.date = this.date

        val c = Calendar.getInstance()
        c.time = date

        c.add(Calendar.DATE, 1 - c.get(Calendar.DATE))
        val startDayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1
        val endDay = c.getActualMaximum(Calendar.DATE)

        c.add(Calendar.MONTH, -1)
        val beforeMonthEndDay = c.getActualMaximum(Calendar.DATE)


        var dayCount = 1
        val startArray = Array(7) {
            if (it < startDayOfWeek) beforeMonthEndDay - startDayOfWeek + it
            else {
                return@Array dayCount++
            }
        }

        calendarAdapter.dates.add(startArray)

        for (i in dayCount..endDay step 7) {
            if (i + 6 <= endDay) {
                calendarAdapter.dates.add(Array(7) { it + i })
            } else {
                val dayOffset = endDay - i
                val tmpArray = Array(7) {
                    if (i + it <= endDay) i + it
                    else it - dayOffset
                }
                calendarAdapter.dates.add(tmpArray)
            }
        }
        calendarAdapter.notifyDataSetChanged()
    }

    fun bindEvents(events: List<EventDto>) {
        calendarAdapter.events = events
        calendarAdapter.notifyDataSetChanged()
    }

    class CustomCalendarAdapter : RecyclerView.Adapter<CustomCalendarViewHolder>() {

        val dates: MutableList<Array<Int>> = mutableListOf()
        var events: List<EventDto> = emptyList()
        lateinit var date: Date

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): CustomCalendarViewHolder {
            Timber.d("CreateViewHolder ${parent.measuredWidth}")
            return LayoutInflater.from(parent.context)
                .inflate(R.layout.item_week, parent, false)
                .let { CustomCalendarViewHolder(it, parent.measuredWidth) }
        }

        override fun getItemCount() = dates.size

        override fun onBindViewHolder(holder: CustomCalendarViewHolder, position: Int) {
            val weekData = dates[position]
            val weekInfo =
                when (position) {
                    0 -> WEEK_FIRST
                    dates.size - 1 -> WEEK_LAST
                    else -> WEEK_ORDINARY
                }

            holder.render(weekData, weekInfo, date, events)
        }
    }

    class CustomCalendarViewHolder(view: View, parentWidth: Int) : RecyclerView.ViewHolder(view) {
        private val context = view.context
        private val layout = view.layout_week
        private val eventLayout = view.event_container
        private val parentWidth = parentWidth / 7

        private val days: List<View> = listOf(
            view.day_1,
            view.day_2,
            view.day_3,
            view.day_4,
            view.day_5,
            view.day_6,
            view.day_7
        )

        init {

            layout.measure(MeasureSpec.EXACTLY, MeasureSpec.UNSPECIFIED)
            Timber.d("${layout.measuredWidth}")

            days.forEach {
                it.setOnClickListener {

                }
            }
        }

        private val eventMap: Array<Array<Boolean>> = Array(5) {Array(7) { false } }

        fun render(weekData: Array<Int>, weekInfo: Int, date: Date, events: List<EventDto>) {

            Timber.d("---------- render start ----------")

            val startDay = weekData[0]
            val endDay = weekData[6]

            // set color of sunday and saturday
            days[0].text_day.setTextColor(ContextCompat.getColor(context, R.color.sunday_red))
            days[6].text_day.setTextColor(ContextCompat.getColor(context, R.color.saturday_blue))

            when (weekInfo) {
                WEEK_FIRST -> for (i in 0 until 7) {
                    if (weekData[i] > 7) {
                        when (i) {
                            0 -> days[i].text_day.setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.sunday_red_opaque
                                )
                            )
                            6 -> days[i].text_day.setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.saturday_blue_opaque
                                )
                            )
                            else -> days[i].text_day.setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.date_black_opaque
                                )
                            )
                        }
                    }
                }
                WEEK_LAST -> for (i in 0 until 7) {
                    if (weekData[i] < 7) {
                        when (i) {
                            0 -> days[i].text_day.setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.sunday_red_opaque
                                )
                            )
                            6 -> days[i].text_day.setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.saturday_blue_opaque
                                )
                            )
                            else -> days[i].text_day.setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.date_black_opaque
                                )
                            )
                        }
                    }
                }
            }

            val today = Calendar.getInstance()
            val targetDate = Calendar.getInstance()
            targetDate.time = date

            if (targetDate.get(Calendar.YEAR) == today.get(Calendar.YEAR)
                && targetDate.get(Calendar.MONTH) == today.get(Calendar.MONTH)
            ) {
                for (i in 0 until 7) {
                    if (weekData[i] == today.get(Calendar.DATE)) {
                        days[i].text_day.setBackgroundResource(R.drawable.today_background)
                        days[i].text_day.setTextColor(ContextCompat.getColor(context, R.color.white))
                    }
                }
            }

            for (i in 0 until 7) {
                days[i].text_day.text = weekData[i].toString()
            }

            val startDate = Calendar.getInstance()
            startDate.time = date
            startDate.set(Calendar.DATE, startDay)
            if(weekInfo == WEEK_FIRST && startDay > 7) startDate.add(Calendar.MONTH, -1)

            val endDate = Calendar.getInstance()
            endDate.time = date
            endDate.set(Calendar.DATE, endDay)
            if(weekInfo == WEEK_LAST && endDay < 7) endDate.add(Calendar.MONTH, 1)


            events.forEach {
                val eventStartDate = it.startDate
                val eventEndDate = it.dueDate
                val eventStartCalendar = Calendar.getInstance()
                val eventEndCalendar = Calendar.getInstance()
                eventStartCalendar.time = eventStartDate
                eventEndCalendar.time = eventEndDate

                if(eventStartDate.compareTo(endDate.time) > 0) return@forEach
                if(eventEndDate.compareTo(startDate.time) < 0) return@forEach

                val startDayOfWeek =
                    if (eventStartDate.compareTo(startDate.time) >= 0) eventStartCalendar.get(Calendar.DAY_OF_WEEK) - 1
                    else 0
                val endDayOfWeek =
                    if (eventEndDate.compareTo(endDate.time) <= 0) eventEndCalendar.get(Calendar.DAY_OF_WEEK) - 1
                    else 6




                Timber.d("${it.contents} : start = ${startDayOfWeek}, end = ${endDayOfWeek}")



                eventLoop@ for (i in 0..4) {
                    for (j in startDayOfWeek..endDayOfWeek) {
                        if (eventMap[i][j]) continue@eventLoop
                    }

                    for (j in startDayOfWeek..endDayOfWeek) {
                        eventMap[i][j] = true
                    }

                    val eventView = EventView(context)

                    eventView.apply {
                        this.text = it.contents
                        this.background = ContextCompat.getDrawable(context, R.drawable.event_round)
                        this.startDayOfWeek = startDayOfWeek
                        this.endDayOfWeek = endDayOfWeek
                        this.line = i
                    }

                    val par = FrameLayout.LayoutParams(
                        parentWidth * (eventView.endDayOfWeek - eventView.startDayOfWeek + 1),
                        context.resources.getDimensionPixelSize(R.dimen.event_height)
                    )

                    par.apply {
                        this.marginStart = parentWidth * eventView.startDayOfWeek
                        this.topMargin = getLineSize(eventView.line)
                    }

                    Timber.d("${eventView.text} : start = ${par.marginStart}")

                    eventView.layoutParams = par

                    eventLayout.addView(eventView)

//                    val eventCustomView = CustomEventView(context)
//                    eventCustomView.apply {
//                        this.text = it.contents
//                        this.startDayOfWeek = startDayOfWeek
//                        this.endDayOfWeek = endDayOfWeek
//                        this.line = i
//                    }
//                    layout.addView(eventCustomView)

                    break@eventLoop
                }
            }


            Timber.d("----------  render end  ----------")

        }

        private fun getLineSize(line: Int): Int {
            return when (line) {
                0       -> layout.resources.getDimensionPixelSize(R.dimen.first_line_event_margin)
                1       -> layout.resources.getDimensionPixelSize(R.dimen.second_line_event_margin)
                2       -> layout.resources.getDimensionPixelSize(R.dimen.third_line_event_margin)
                3       -> layout.resources.getDimensionPixelSize(R.dimen.fourth_line_event_margin)
                4       -> layout.resources.getDimensionPixelSize(R.dimen.fifth_line_event_margin)
                else    -> layout.resources.getDimensionPixelSize(R.dimen.first_line_event_margin)
            }
        }
    }



    companion object {
        private const val WEEK_FIRST = 0
        private const val WEEK_ORDINARY = 1
        private const val WEEK_LAST = 2
    }
}


