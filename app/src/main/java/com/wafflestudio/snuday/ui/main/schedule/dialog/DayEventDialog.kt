package com.wafflestudio.snuday.ui.main.schedule.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.wafflestudio.snuday.R
import com.wafflestudio.snuday.databinding.DialogDayEventsBinding
import com.wafflestudio.snuday.model.Event
import com.wafflestudio.snuday.ui.main.schedule.ScheduleViewModel
import com.wafflestudio.snuday.utils.subIoObsMain
import com.wafflestudio.snuday.utils.toPrettyStringDate
import com.wafflestudio.snuday.utils.toPrettyStringDateWithOutYear
import io.reactivex.rxjava3.disposables.CompositeDisposable
import timber.log.Timber
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class DayEventDialog(private val selectedDay: LocalDate) : DialogFragment() {

    private lateinit var binding: DialogDayEventsBinding
    private val vm: ScheduleViewModel by activityViewModels()

    private lateinit var eventAdapter: DayEventDialogEventAdapter
    private lateinit var eventLayoutManager: LinearLayoutManager

    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogDayEventsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        params?.width = resources.getDimension(R.dimen.dialog_width).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.textDay.text = selectedDay.dayOfMonth.toString() + "일"
        binding.textDateDetail.text = selectedDay.toPrettyStringDate()

        eventAdapter = DayEventDialogEventAdapter(
            object : DayEventDialogEventAdapter.OnEventClickListener {
                override fun onClick(event: Event) {
                    val dialog = EventDetailDialog(event)
                    dialog.setDismissListener {
                        vm.fetchEvent().subIoObsMain().subscribe({}, {Timber.d(it)}).also { compositeDisposable.add(it) }
                    }
                    dialog.show(childFragmentManager, "event_detail")
                }
            }
        )
        eventLayoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewEvent.apply {
            adapter = eventAdapter
            layoutManager = eventLayoutManager
        }

        binding.buttonAddMyEvent.setOnClickListener {
            val dialog = AddMyEventDialog(LocalDateTime.of(selectedDay, LocalTime.now()))
            dialog.show(childFragmentManager, "add_my_event")
        }

        Timber.d(vm.eventListValue.toString())
        vm.eventList.subIoObsMain().subscribe({ eventList ->
            eventAdapter.eventList = eventList.filter { it.startDate <= selectedDay && it.dueDate >= selectedDay }
            eventAdapter.notifyDataSetChanged()
        }, {Timber.d(it)}).also { compositeDisposable.add(it) }

        val selectedDayEventList = vm.eventListValue.filter { it.startDate <= selectedDay && it.dueDate >= selectedDay }
        eventAdapter.eventList = selectedDayEventList
        eventAdapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable.dispose()
    }
}