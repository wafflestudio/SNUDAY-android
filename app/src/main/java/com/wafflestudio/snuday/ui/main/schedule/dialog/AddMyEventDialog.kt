package com.wafflestudio.snuday.ui.main.schedule.dialog

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.wafflestudio.snuday.R
import com.wafflestudio.snuday.databinding.DialogAddMyEventBinding
import com.wafflestudio.snuday.ui.main.schedule.ScheduleViewModel
import com.wafflestudio.snuday.utils.*
import io.reactivex.rxjava3.disposables.CompositeDisposable
import timber.log.Timber
import java.time.LocalDateTime

class AddMyEventDialog(dayOffset: LocalDateTime = LocalDateTime.now()) : DialogFragment() {

    private lateinit var binding: DialogAddMyEventBinding
    private val vm: ScheduleViewModel by activityViewModels()

    private val compositeDisposable = CompositeDisposable()

    private var onDialogDismissListener: (() -> (Unit))? = null

    private var startDateTime: LocalDateTime = dayOffset.withHour(9).withMinute(0)
    set(value) {
        binding.textStartDate.text = value.toPrettyStringDateWithOutYear()
        binding.textStartTime.text = " ${value.toPrettyStringTimeWithNoonData()}"
        field = value
    }
    private var dueDateTime: LocalDateTime = dayOffset.withHour(18).withMinute(0)
    set(value) {
        binding.textDueDate.text = value.toPrettyStringDateWithOutYear()
        binding.textDueTime.text = " ${value.toPrettyStringTimeWithNoonData()}"
        field = value
    }

    private var hasTime: Boolean = true
    set(value) {
        binding.textStartTime.visibleOrGone(value)
        binding.textDueTime.visibleOrGone(value)
        field = value
    }
    
    private val onStartDatePickerListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
        startDateTime = startDateTime.withYear(year).withMonth(month + 1).withDayOfMonth(day)
    }

    private val onStartTimePickerListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
        startDateTime = startDateTime.withHour(hour).withMinute(minute)
    }

    private val onDueDatePickerListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
        dueDateTime = dueDateTime.withYear(year).withMonth(month + 1).withDayOfMonth(day)
    }

    private val onDueTimePickerListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
        dueDateTime = dueDateTime.withHour(hour).withMinute(minute)
    }

    fun setDismissListener(listener: () -> (Unit)) {
        this.onDialogDismissListener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogAddMyEventBinding.inflate(layoutInflater)
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

        vm.personalChannelId?.let {
            vm.getChannelColorById(it).subIoObsMain().subscribe {
                val grd = binding.root.background as GradientDrawable
                grd.setStroke(1, it.getResource(requireContext()))
                binding.root.background = grd
            }
        }

        startDateTime = startDateTime
        dueDateTime = dueDateTime
        hasTime = hasTime

        binding.textStartDate.setOnClickListener {
            val dialog = DatePickerDialog(requireContext(), R.style.PickerDialogTheme, onStartDatePickerListener, startDateTime.year, startDateTime.monthValue - 1, startDateTime.dayOfMonth)
            dialog.show()
        }

        binding.textStartTime.setOnClickListener {
            val dialog = TimePickerDialog(requireContext(), R.style.PickerDialogTheme, onStartTimePickerListener, startDateTime.hour, startDateTime.minute, false)
            dialog.show()
        }

        binding.textDueDate.setOnClickListener {
            val dialog = DatePickerDialog(requireContext(), R.style.PickerDialogTheme, onDueDatePickerListener, dueDateTime.year, dueDateTime.monthValue - 1, dueDateTime.dayOfMonth)
            dialog.show()
        }

        binding.textDueTime.setOnClickListener {
            val dialog = TimePickerDialog(requireContext(), R.style.PickerDialogTheme, onDueTimePickerListener, dueDateTime.hour, dueDateTime.minute, false)
            dialog.show()
        }

        binding.switchAllDay.setOnCheckedChangeListener { _, b -> hasTime = !b }

        binding.buttonClose.setOnClickListener {
            dismiss()
        }

        binding.buttonSave.setOnClickListener {
            val title = binding.editTextTitle.text.toString()
            val memo = binding.editTextMemo.text.toString()

            if (title.isBlank()) {
                showToast("제목을 채워주세요.")
                return@setOnClickListener
            }

            if (memo.isBlank()) {
                showToast("메모를 채워주세요.")
                return@setOnClickListener
            }

            if (hasTime) {
                if (startDateTime > dueDateTime) {
                    showToast("시작일은 종료일보다 늦을 수 없습니다.")
                }
            } else {
                if (startDateTime.toLocalDate() > dueDateTime.toLocalDate()) {
                    showToast("시작일은 종료일보다 늦을 수 없습니다.")
                }
            }

            vm.postEvent(title, hasTime, startDateTime, dueDateTime, memo).subIoObsMain().subscribe(
                { event ->
                    Timber.d(event.toString())
                    dismiss()
                }, { throwable ->
                    Timber.d(throwable)
                    showToast("네트워크가 불안정합니다.")
                }).also { disposable ->  compositeDisposable.add(disposable) }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    override fun onDismiss(dialog: DialogInterface) {
        onDialogDismissListener?.invoke()
        super.onDismiss(dialog)
    }

}