package com.wafflestudio.snuday.views

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wafflestudio.snuday.R
import com.wafflestudio.snuday.databinding.ViewCalendarCustomBinding
import com.wafflestudio.snuday.models.EventDto
import kotlinx.android.synthetic.main.item_day.view.*
import kotlinx.android.synthetic.main.item_day.view.text_day
import kotlinx.android.synthetic.main.item_day_sunday.view.*
import kotlinx.android.synthetic.main.item_week.view.*
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class CustomCalendarView : ConstraintLayout {

    private var binding: ViewCalendarCustomBinding
    private lateinit var date: Date
    private var year: Int = 2000
    private var month: Int = 1
    private var day: Int = 1
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

    constructor(context: Context, date: Date) : super(context) {
        binding = ViewCalendarCustomBinding.inflate(LayoutInflater.from(context), this)
    }

    fun setDatesToCalendar(dateData: Date) {

        this.date = dateData
        Log.d("calendar", this.date.toString())

        calendarAdapter = CustomCalendarAdapter()
        calendarLayoutManager = LinearLayoutManager(context)
        binding.recyclerViewCalendar.adapter = calendarAdapter
        binding.recyclerViewCalendar.layoutManager = calendarLayoutManager

        year = SimpleDateFormat("yyyy").format(date).toInt()
        month = SimpleDateFormat("MM").format(date).toInt()
        day = SimpleDateFormat("dd").format(date).toInt()

        Log.d("calendar", "year, month, day" + year.toString() + " " + month.toString() + " " + day.toString())

        val c = Calendar.getInstance()
        c.time = date
        c.add(Calendar.DATE, 1 - day)
        Log.d("calendar", c.toString())
        var startDayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1
        val endDay = c.getActualMaximum(Calendar.DATE)

        Log.d("calendar","startDayOfWeek " + startDayOfWeek.toString() + "endDay " + endDay.toString())

        c.add(Calendar.MONTH, -1)
        val beforeMonthEndDay = c.getActualMaximum(Calendar.DATE)

        val startArray = arrayOf(0, 0, 0, 0, 0, 0, 0)
        var dayCount = 1

        for (i in 0 until 7) {
            if (i < startDayOfWeek) {
                startArray[i] = beforeMonthEndDay - startDayOfWeek + i
            } else {
                startArray[i] = dayCount
                dayCount += 1
            }
        }

        calendarAdapter.dates.add(startArray)

        for (i in dayCount .. endDay step 7) {
            if (i + 6 <= endDay) {
                calendarAdapter.dates.add(arrayOf(
                    i,
                    i + 1,
                    i + 2,
                    i + 3,
                    i + 4,
                    i + 5,
                    i + 6
                ))
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

    }

    class CustomCalendarAdapter : RecyclerView.Adapter<CustomCalendarViewHolder>() {

        val dates: MutableList<Array<Int>> = mutableListOf()

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): CustomCalendarViewHolder =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_week, parent, false)
                .let { CustomCalendarViewHolder(it) }

        override fun getItemCount() = dates.size

        override fun onBindViewHolder(holder: CustomCalendarViewHolder, position: Int) {
            val weekData = dates[position]
            val weekInfo =
                when(position) {
                    0               -> WEEK_FIRST
                    dates.size - 1  -> WEEK_LAST
                    else            -> WEEK_ORDINARY
                }
            holder.render(weekData, weekInfo)
        }
    }

    class CustomCalendarViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val context = view.context

        private val days: List<View> = listOf(
            view.day_1,
            view.day_2,
            view.day_3,
            view.day_4,
            view.day_5,
            view.day_6,
            view.day_7
            )

        fun render(weekData: Array<Int>, weekInfo: Int) {
            days[0].text_day.setTextColor(ContextCompat.getColor(context, R.color.sunday_red))
            days[6].text_day.setTextColor(ContextCompat.getColor(context, R.color.saturday_blue))


            when (weekInfo) {
                WEEK_FIRST -> for (i in 0 until 7) {
                    if (weekData[i] > 7) {
                        when(i) {
                            0 -> days[i].text_day.setTextColor(ContextCompat.getColor(context, R.color.sunday_red_opaque))
                            6 -> days[i].text_day.setTextColor(ContextCompat.getColor(context, R.color.saturday_blue_opaque))
                            else -> days[i].text_day.setTextColor(ContextCompat.getColor(context, R.color.date_black_opaque))
                        }
                    }
                }
                WEEK_LAST -> for (i in 0 until 7) {
                    if (weekData[i] < 7) {
                        when(i) {
                            0 -> days[i].text_day.setTextColor(ContextCompat.getColor(context, R.color.sunday_red_opaque))
                            6 -> days[i].text_day.setTextColor(ContextCompat.getColor(context, R.color.saturday_blue_opaque))
                            else -> days[i].text_day.setTextColor(ContextCompat.getColor(context, R.color.date_black_opaque))
                        }
                    }
                }
            }


            for (i in 0 until 7) {
                days[i].text_day.text = weekData[i].toString()
            }


        }
    }

    companion object {
        private const val WEEK_FIRST = 0
        private const val WEEK_ORDINARY = 1
        private const val WEEK_LAST = 2
    }
}


