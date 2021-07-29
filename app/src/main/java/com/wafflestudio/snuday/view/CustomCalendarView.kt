package com.wafflestudio.snuday.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wafflestudio.snuday.R
import com.wafflestudio.snuday.databinding.ItemCalendarWeekBinding
import com.wafflestudio.snuday.databinding.ViewCalendarCustomBinding
import com.wafflestudio.snuday.model.Event
import com.wafflestudio.snuday.utils.getInflater
import timber.log.Timber
import java.time.LocalDate
import kotlin.math.absoluteValue

class CustomCalendarView : ConstraintLayout {

    private var binding: ViewCalendarCustomBinding
    private val today = LocalDate.now()
    private var clickEnabled = true
    private var showToday = true
    private var maxWeekCount = 6
    private var fullCalendar = true
    private lateinit var focusingDate: LocalDate
    private lateinit var calendarAdapter: CustomCalendarWeekAdapter
    private lateinit var calendarLayoutManager: LinearLayoutManager

    constructor(context: Context) : super(context) {
        binding = ViewCalendarCustomBinding.inflate(LayoutInflater.from(context), this)
        init(null)
        Timber.d("1")
    }

    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        binding = ViewCalendarCustomBinding.inflate(LayoutInflater.from(context), this)
        init(attributeSet)
        Timber.d("2")
    }

    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    ) {
        binding = ViewCalendarCustomBinding.inflate(LayoutInflater.from(context), this)
        init(attributeSet)
        Timber.d("3")
    }

    fun init(attr: AttributeSet?) {

        calendarAdapter = CustomCalendarWeekAdapter()
        calendarLayoutManager = LinearLayoutManager(context)

        binding.calendarRecyclerView.apply {
            adapter = calendarAdapter
            layoutManager = calendarLayoutManager
        }

        context.theme.obtainStyledAttributes(
            attr,
            R.styleable.CustomCalendarView,
            0,
            0
        ).apply {
            try {
                clickEnabled = getBoolean(R.styleable.CustomCalendarView_clickEnabled,  true)
                showToday = getBoolean(R.styleable.CustomCalendarView_showToday, true)
                maxWeekCount = getInteger(R.styleable.CustomCalendarView_maxWeekCount, 6)
                fullCalendar = getBoolean(R.styleable.CustomCalendarView_fullCalendar, true)
            } finally {
                recycle()
            }
        }
    }

    fun setDate(date: LocalDate) {
        Timber.d("setData Start")
        focusingDate = date
        updateDateData()
    }

    // 이번 달이 아닌 날짜들은 음수로 표기하여 구분합니다.
    private fun updateDateData() {
        Timber.d("updateData Start")
        val year = focusingDate.year
        val month = focusingDate.monthValue

        val weeks = mutableListOf<List<Int>>()

        val startDate = LocalDate.of(year, month, 1)
        var targetDate = LocalDate.of(year, month, 1)

        val firstWeek = mutableListOf<Int>()
        for (i in 1 .. 7) {
            val diff = startDate.dayOfWeek.value % 7 - i + 1
            if (diff > 0) {
                val tempDate = startDate.minusDays(diff.toLong())
                firstWeek.add(-tempDate.dayOfMonth)
            } else {
                firstWeek.add(targetDate.dayOfMonth)
                targetDate = targetDate.plusDays(1)
            }
        }
        weeks.add(firstWeek)

        while (targetDate.monthValue == month) {
            val week = mutableListOf<Int>()
            for (i in 0 until 7) {
                if (targetDate.monthValue != month) {
                    week.add(-targetDate.dayOfMonth)
                    targetDate = targetDate.plusDays(1)
                } else {
                    week.add(targetDate.dayOfMonth)
                    targetDate = targetDate.plusDays(1)
                }
            }
            weeks.add(week)
        }

        Timber.d("weeks \n ${weeks}")

        calendarAdapter.weeks = weeks
        calendarAdapter.notifyDataSetChanged()
    }

    fun submitEventList(events: List<Event>) {
        calendarAdapter.events = events
        calendarAdapter.notifyDataSetChanged()
    }

    private inner class CustomCalendarWeekAdapter : RecyclerView.Adapter<CustomCalendarWeekViewHolder>() {

        var weeks = listOf<List<Int>>()
        var events = listOf<Event>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomCalendarWeekViewHolder {
            val binding = ItemCalendarWeekBinding.inflate(parent.getInflater(), parent, false)
            return CustomCalendarWeekViewHolder(binding, parent.measuredWidth)
        }

        override fun onBindViewHolder(holder: CustomCalendarWeekViewHolder, position: Int) {
            holder.render(weeks[position])
        }

        override fun getItemCount() = weeks.size
    }

    private inner class CustomCalendarWeekViewHolder(
        binding: ItemCalendarWeekBinding,
        parentWidth: Int
    ) : RecyclerView.ViewHolder(binding.root) {

        private val days = listOf(
            binding.day1,
            binding.day2,
            binding.day3,
            binding.day4,
            binding.day5,
            binding.day6,
            binding.day7
        )

        fun render(weekData: List<Int>) {
            Timber.d("rendering ${weekData}")
            weekData.forEachIndexed { idx, day ->
                days[idx].apply {
                    dayText.text = day.absoluteValue.toString()

                    if (day < 0) {
                        when (idx) {
                            0 -> dayText.setTextColor(ContextCompat.getColor(context, R.color.calendar_sunday_red_light))
                            7 -> dayText.setTextColor(ContextCompat.getColor(context, R.color.calendar_saturday_blue_light))
                            else -> dayText.setTextColor(ContextCompat.getColor(context, R.color.calendar_weekday_gray_light))
                        }
                    } else {
                        when (idx) {
                            0 -> dayText.setTextColor(ContextCompat.getColor(context, R.color.calendar_sunday_red))
                            7 -> dayText.setTextColor(ContextCompat.getColor(context, R.color.calendar_saturday_blue))
                            else -> dayText.setTextColor(ContextCompat.getColor(context, R.color.calendar_weekday_gray))
                        }

                        if (today.dayOfMonth == day) {
                            dayText.setTextColor(ContextCompat.getColor(context, R.color.white))
                            dayText.typeface = ResourcesCompat.getFont(context, R.font.sf_pro_bold)
                            dayText.setBackgroundResource(R.drawable.frame_calendar_today)
                        }
                    }
                }
            }
        }

    }

}