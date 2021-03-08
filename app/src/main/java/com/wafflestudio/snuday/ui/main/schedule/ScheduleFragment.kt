package com.wafflestudio.snuday.ui.main.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.wafflestudio.snuday.R
import com.wafflestudio.snuday.databinding.FragmentScheduleBinding
import com.wafflestudio.snuday.models.EventDto
import com.wafflestudio.snuday.views.CustomCalendarView
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class ScheduleFragment : Fragment() {

    private lateinit var binding: FragmentScheduleBinding
    private val viewModel: ScheduleViewModel by viewModels()

    private lateinit var tagAdapter: TagAdapter
    private lateinit var tagLayoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScheduleBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Tag RecyclerView
        tagAdapter = TagAdapter()
        tagLayoutManager =
            LinearLayoutManager(this.requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewTags.adapter = tagAdapter
        binding.recyclerViewTags.layoutManager = tagLayoutManager

        val cc = Calendar.getInstance()
        cc.add(Calendar.MONTH, -1)

        binding.customCalendarView.setDatesToCalendar(cc.time)
        binding.textYearShow.text = SimpleDateFormat("yyyy").format(cc.time)
        binding.textMonthShow.text = getString(
            when(SimpleDateFormat("MM").format(cc.time).toInt()) {
                1 -> R.string.January
                2 -> R.string.February
                3 -> R.string.March
                4 -> R.string.April
                5 -> R.string.May
                6 -> R.string.June
                7 -> R.string.July
                8 -> R.string.August
                9 -> R.string.September
                10 -> R.string.October
                11 -> R.string.November
                12 -> R.string.December
                else -> R.string.January
            }
        )

        val a = Calendar.getInstance()
        a.set(2021, Calendar.FEBRUARY, 3)
        val b = Calendar.getInstance()
        b.set(2021, Calendar.FEBRUARY, 5)
        val c = Calendar.getInstance()
        c.set(2021, Calendar.FEBRUARY, 9)
        val d = Calendar.getInstance()
        d.set(2021, Calendar.FEBRUARY, 19)
        val e = Calendar.getInstance()
        e.set(2021, Calendar.FEBRUARY, 8)
        val f = Calendar.getInstance()
        f.set(2021, Calendar.FEBRUARY, 10)




        val event1 = EventDto(
            id = 1,
            user_id = 1,
            channel_id = 1,
            contents = "abcdefghijklmnopqrstuvwxyz",
            startDate = a.time,
            dueDate = b.time
        )

        val event2 = EventDto(
            id = 2,
            user_id = 1,
            channel_id = 1,
            contents = "수강신청 하자~",
            startDate = b.time,
            dueDate = d.time
        )

        val event3 = EventDto(
            id = 3,
            user_id = 1,
            channel_id = 1,
            contents = "수강신청~",
            startDate = c.time,
            dueDate = d.time
        )

        val event4 = EventDto(
            id = 4,
            user_id = 1,
            channel_id = 1,
            contents = "와플스튜디오 와플스튜디오 와플스튜디오",
            startDate = e.time,
            dueDate = f.time
        )



        binding.customCalendarView.bindEvents(listOf(event1, event2, event3, event4))
    }

}
