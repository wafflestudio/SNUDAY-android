package com.wafflestudio.snuday.view

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
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
import com.wafflestudio.snuday.repository.ChannelColorManager
import com.wafflestudio.snuday.utils.getInflater
import com.wafflestudio.snuday.utils.getResource
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject
import kotlin.math.absoluteValue

class CustomCalendarView : ConstraintLayout {

    var binding: ViewCalendarCustomBinding
    private val today = LocalDate.now()
    lateinit var focusingDate: LocalDate
    private lateinit var calendarAdapter: CustomCalendarWeekAdapter
    private lateinit var calendarLayoutManager: LinearLayoutManager

    private var onDayClickListener: OnDayClickListener? = null

    constructor(context: Context) : super(context) {
        binding = ViewCalendarCustomBinding.inflate(LayoutInflater.from(context), this)
        init(null)
    }

    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        binding = ViewCalendarCustomBinding.inflate(LayoutInflater.from(context), this)
        init(attributeSet)
    }

    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    ) {
        binding = ViewCalendarCustomBinding.inflate(LayoutInflater.from(context), this)
        init(attributeSet)
    }

    fun init(attr: AttributeSet?) {

        calendarAdapter = CustomCalendarWeekAdapter()
        calendarLayoutManager = LinearLayoutManager(context)

        binding.calendarRecyclerView.apply {
            adapter = calendarAdapter
            layoutManager = calendarLayoutManager
        }
    }

    fun setOnDayClickListener(listener: OnDayClickListener) {
        this.onDayClickListener = listener
    }

    fun setDate(date: LocalDate) {
        Timber.d("setData Start")
        focusingDate = date
        updateDate()
    }

    private fun updateDate() {
        val week = mutableListOf<Pair<LocalDate, LocalDate>>()

        val startDate = LocalDate.of(focusingDate.year, focusingDate.monthValue, 1)
        Timber.d("startDate ${startDate}")
        Timber.d("minus value : ${startDate.dayOfWeek.value}")
        val startDateMonday = startDate.minusDays((startDate.dayOfWeek.value).toLong() % 7)
        Timber.d("startDateMonday ${startDateMonday}")
        var iteratorDate = startDateMonday
        val targetDate = LocalDate.of(focusingDate.year, focusingDate.monthValue, 1).plusMonths(1)
        while (iteratorDate < targetDate) {
            week.add(Pair(iteratorDate, iteratorDate.plusWeeks(1).minusDays(1)))
            iteratorDate = iteratorDate.plusWeeks(1)
        }

        calendarAdapter.week = week
        calendarAdapter.notifyDataSetChanged()
    }

    fun submitEventList(events: List<Event>) {
        // startMarkDate : 이전 달 1일
        // endMarkDate : 다음 달 말일
        val startMarkDate = LocalDate.of(focusingDate.year, focusingDate.month, 1).minusMonths(1)
        val endMarkDate = LocalDate.of(focusingDate.year, focusingDate.month, 1).plusMonths(2).minusDays(1)

        val filteredEvent = events.filter { event ->
            return@filter event.startDate > startMarkDate && event.dueDate < endMarkDate
        }

        calendarAdapter.events = filteredEvent
        calendarAdapter.notifyDataSetChanged()
    }

    private inner class CustomCalendarWeekAdapter : RecyclerView.Adapter<CustomCalendarWeekViewHolder>() {

        var events = listOf<Event>()
        var week = listOf<Pair<LocalDate, LocalDate>>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomCalendarWeekViewHolder {
            val binding = ItemCalendarWeekBinding.inflate(parent.getInflater(), parent, false)
            return CustomCalendarWeekViewHolder(binding, parent.measuredWidth)
        }

        override fun onBindViewHolder(holder: CustomCalendarWeekViewHolder, position: Int) {
            holder.render(week[position], events)
        }

        override fun getItemCount() = week.size
    }

    private inner class CustomCalendarWeekViewHolder(
        binding: ItemCalendarWeekBinding,
        private val parentWidth: Int
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

        private val eventContainer = binding.layoutEventContainer

        private var eventMap: Array<Array<Boolean>> = Array(5) {Array(7) { false } }

        fun render(weekData: Pair<LocalDate, LocalDate>, eventData: List<Event>) {

            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val widthUse = wm.defaultDisplay.width


            var iteratorDate = weekData.first
            val startDate = weekData.first
            val endDate = weekData.second

            eventContainer.removeAllViews()
            eventMap = Array(5) {Array(7) { false } }

            days.forEachIndexed { idx, day ->
                day.apply {
                    dayText.text = iteratorDate.dayOfMonth.toString()
                    dayText.typeface = ResourcesCompat.getFont(context, R.font.sf_pro_regular)
                    dayText.setBackgroundResource(R.color.transparent)

                    val currentDate = iteratorDate
                    this.root.setOnClickListener {
                        onDayClickListener?.onClick(currentDate)
                    }

                    if (iteratorDate.monthValue != focusingDate.monthValue) {
                        when (idx) {
                            0 -> dayText.setTextColor(ContextCompat.getColor(context, R.color.calendar_sunday_red_light))
                            6 -> dayText.setTextColor(ContextCompat.getColor(context, R.color.calendar_saturday_blue_light))
                            else -> dayText.setTextColor(ContextCompat.getColor(context, R.color.calendar_weekday_gray_light))
                        }
                    } else {
                        when (idx) {
                            0 -> dayText.setTextColor(ContextCompat.getColor(context, R.color.calendar_sunday_red))
                            6 -> dayText.setTextColor(ContextCompat.getColor(context, R.color.calendar_saturday_blue))
                            else -> dayText.setTextColor(ContextCompat.getColor(context, R.color.calendar_weekday_gray))
                        }

                        if (iteratorDate == today) {
                            dayText.setTextColor(ContextCompat.getColor(context, R.color.white))
                            dayText.typeface = ResourcesCompat.getFont(context, R.font.sf_pro_bold)
                            dayText.setBackgroundResource(R.drawable.frame_calendar_today)
                        }
                    }
                }
                iteratorDate = iteratorDate.plusDays(1)
            }

            eventData.forEach { event ->
                if ( event.startDate > endDate ) return@forEach
                if ( event.dueDate < startDate ) return@forEach

                var roundSet
                    = if ( event.startDate >= startDate && event.dueDate <= endDate ) ALL_ROUND
                    else if (event.startDate < startDate && event.dueDate <= endDate) RIGHT_ROUND
                    else if (event.startDate >= startDate && event.dueDate > endDate) LEFT_ROUND
                    else NO_ROUND

                val startDayOfWeek = if (event.startDate < startDate) 0 else ( event.startDate.dayOfWeek.value % 7 )
                val endDayOfWeek = if (event.dueDate > endDate) 6 else ( event.dueDate.dayOfWeek.value % 7 )


                eventLoop@ for (i in 0..4) {
                    for (j in startDayOfWeek..endDayOfWeek) {
                        if (eventMap[i][j]) continue@eventLoop
                    }

                    for (j in startDayOfWeek..endDayOfWeek) {
                        eventMap[i][j] = true
                    }

                    val eventView = EventView(context)

                    eventView.apply {
                        this.text = event.title
                        val grd = when (roundSet) {
                            ALL_ROUND -> ContextCompat.getDrawable(context, R.drawable.frame_event_all_round)
                            RIGHT_ROUND -> ContextCompat.getDrawable(context, R.drawable.frame_event_right_round)
                            LEFT_ROUND -> ContextCompat.getDrawable(context, R.drawable.frame_event_left_round)
                            NO_ROUND -> ContextCompat.getDrawable(context, R.drawable.frame_event_no_round)
                            else -> ContextCompat.getDrawable(context, R.drawable.frame_event_all_round)
                        } as GradientDrawable

                        grd.setColor(event.channelColor.getResource(context))

                        this.background = grd
                    }

                    Timber.d(parentWidth.toString())

                    val par = FrameLayout.LayoutParams(
                        widthUse * (endDayOfWeek - startDayOfWeek + 1) / 7,
                        context.resources.getDimensionPixelSize(R.dimen.event_height)
                    ).apply {
                        this.marginStart = widthUse * startDayOfWeek / 7
                        this.topMargin = getLineSize(i)
                    }
                    Timber.d("eventView data ${par.marginStart} , ${par.width}")
                    eventView.layoutParams = par
                    Timber.d("rendering $startDate - $endDate : ${event.title}")

                    eventContainer.addView(eventView)
                    break@eventLoop
                }
            }
        }
    }

    private fun getLineSize(line: Int): Int {
        return when (line) {
            0       -> resources.getDimensionPixelSize(R.dimen.first_line_event_margin)
            1       -> resources.getDimensionPixelSize(R.dimen.second_line_event_margin)
            2       -> resources.getDimensionPixelSize(R.dimen.third_line_event_margin)
            3       -> resources.getDimensionPixelSize(R.dimen.fourth_line_event_margin)
            4       -> resources.getDimensionPixelSize(R.dimen.fifth_line_event_margin)
            else    -> resources.getDimensionPixelSize(R.dimen.first_line_event_margin)
        }
    }

    interface OnDayClickListener {
        fun onClick(date: LocalDate)
    }

    companion object {
        private const val ALL_ROUND = 0
        private const val RIGHT_ROUND = 1
        private const val LEFT_ROUND = 2
        private const val NO_ROUND = 3
    }

}