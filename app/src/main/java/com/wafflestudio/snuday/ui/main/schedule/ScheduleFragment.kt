package com.wafflestudio.snuday.ui.main.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.wafflestudio.snuday.databinding.FragmentScheduleBinding
import com.wafflestudio.snuday.ui.main.schedule.dialog.AddMyEventDialog
import com.wafflestudio.snuday.ui.main.schedule.dialog.ChannelFilterDialog
import com.wafflestudio.snuday.ui.main.schedule.dialog.DayEventDialog
import com.wafflestudio.snuday.utils.subIoObsMain
import com.wafflestudio.snuday.view.CustomCalendarView
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import timber.log.Timber
import java.time.LocalDate
import java.time.LocalDateTime

@AndroidEntryPoint
class ScheduleFragment : Fragment() {

    private lateinit var binding: FragmentScheduleBinding
    private val vm: ScheduleViewModel by activityViewModels()
    private lateinit var calendarViewPagerAdapter: CalendarViewPagerAdapter
    private lateinit var channelTagAdapter: ChannelTagAdapter
    private lateinit var channelTagLayoutManager: LinearLayoutManager

    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vm.fetchPersonalChannelId().subIoObsMain().subscribe({
            Timber.d("channelIdGet")
        }, {
            Timber.d(it)
        }).also { compositeDisposable.add(it) }

        vm.checkAllSubChannelHaveColor().subIoObsMain()
            .subscribe({
                Timber.d("checked color")
                    it.forEach { channel ->
                        Timber.d("adding~~ ${channel.id}")
                        vm.addChannelColor(channel.id).subIoObsMain()
                            .subscribe({
                                    Timber.d("ADDED channelColor ${channel.id}")
                                }, { error ->
                                    Timber.d(error)
                                })
                    }
                }, {
                    Timber.d(it)
                }).also { compositeDisposable.add(it) }

        vm.fetchSubscribingChannel().subIoObsMain().doOnError { Timber.d(it) }
            .subscribe().also { compositeDisposable.add(it) }

        binding = FragmentScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.yearText.text = LocalDate.now().year.toString() + "년"
        binding.monthText.text = LocalDate.now().monthValue.toString() + "월"

        calendarViewPagerAdapter = CalendarViewPagerAdapter(
            object : CustomCalendarView.OnDayClickListener {
                override fun onClick(date: LocalDate) {
                    val dialog = DayEventDialog(date)
                    dialog.show(childFragmentManager, "day_event")
                }
            }
        )

        binding.vpCalendar.adapter = calendarViewPagerAdapter
        binding.vpCalendar.setCurrentItem(CalendarViewPagerAdapter.START_POSITION, false)
        binding.vpCalendar.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    val offset = position - (Int.MAX_VALUE / 2)
                    val calendarDate = LocalDate.now().plusMonths(offset.toLong())
                    binding.yearText.text = calendarDate.year.toString() + "년"
                    binding.monthText.text = calendarDate.monthValue.toString() + "월"
                }
            }
        )
//        binding.vpCalendar.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//            override fun onPageScrolled(
//                position: Int,
//                positionOffset: Float,
//                positionOffsetPixels: Int
//            ) {
//                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
//                vm.fetchEvent().subIoObsMain().subscribe({}, {Timber.d(it)}).also { compositeDisposable.add(it) }
//            }
//        })

        channelTagAdapter = ChannelTagAdapter()
        channelTagLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.channelTagRecyclerView.apply {
            adapter = channelTagAdapter
            layoutManager = channelTagLayoutManager
        }

        vm.filterList.subIoObsMain()
            .subscribe({
                Timber.d("filterList ${it}")
                channelTagAdapter.channelList = it
                channelTagAdapter.notifyDataSetChanged()
            }, { Timber.d(it) }).also { compositeDisposable.add(it) }

        vm.eventList.subIoObsMain().subscribe ({
            calendarViewPagerAdapter.eventList = it
            calendarViewPagerAdapter.notifyDataSetChanged()
        }, { Timber.d(it) }).also { compositeDisposable.add(it) }

        binding.buttonAddMyEvent.setOnClickListener {

            val dialog = AddMyEventDialog()
            dialog.setDismissListener {
                vm.fetchEvent()
                .subIoObsMain()
                .subscribe({Timber.d("fetch Success")}, {Timber.d(it)})
                .also { compositeDisposable.add(it) }
            }
            dialog.show(childFragmentManager, "add_my_event")
        }

        binding.buttonFilterSetting.setOnClickListener {
            val dialog = ChannelFilterDialog()
            dialog.show(childFragmentManager, "channel_filer")
        }
    }

    override fun onResume() {
        Timber.d("ScheduleFragment onResume()")

        vm.fetchEvent()
            .subIoObsMain()
            .subscribe({Timber.d("fetch Success")}, {Timber.d(it)})
            .also { compositeDisposable.add(it) }

        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable.dispose()
    }

}