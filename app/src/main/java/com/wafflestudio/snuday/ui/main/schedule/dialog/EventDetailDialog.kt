package com.wafflestudio.snuday.ui.main.schedule.dialog

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.wafflestudio.snuday.R
import com.wafflestudio.snuday.databinding.DialogEventDetailBinding
import com.wafflestudio.snuday.model.Event
import com.wafflestudio.snuday.ui.main.schedule.ScheduleViewModel
import com.wafflestudio.snuday.utils.getResource
import com.wafflestudio.snuday.utils.toPrettyString
import com.wafflestudio.snuday.utils.toPrettyStringDateWithOutYear
import com.wafflestudio.snuday.utils.visibleOrGone

class EventDetailDialog (private val event: Event) : DialogFragment(){

    private lateinit var binding: DialogEventDetailBinding

    private val vm: ScheduleViewModel by activityViewModels()

    private var onDialogDismissListener: (() -> (Unit))? = null

    fun setDismissListener(listener: () -> (Unit)) {
        this.onDialogDismissListener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogEventDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        params?.width = resources.getDimension(R.dimen.dialog_event_detail_width).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.tag.cardView.setCardBackgroundColor(event.channelColor.getResource(requireContext()))
        binding.tag.channelNameText.text = if (event.channelId == vm.personalChannelId) "내 일정" else event.channelName
        binding.textTitle.text = event.title
        binding.textMemo.text = event.memo
        binding.textDate.text = event.startDate.toPrettyStringDateWithOutYear() + " " +
            (if (event.hasTime) event.startTime?.toPrettyString() + " " else "") +
            "\n~ " + event.dueDate.toPrettyStringDateWithOutYear() + " " +
            (if (event.hasTime) event.dueTime?.toPrettyString() + " " else "")

        vm.personalChannelId?.let {
            if (it == event.channelId) {
                binding.buttonModify.visibleOrGone(true)
            } else {
                binding.buttonModify.visibleOrGone(false)
            }
        }

        binding.buttonModify.setOnClickListener {
            val dialog = ModifyMyEventDialog(event)
            dialog.setDismissListener {
                dismiss()
            }
            dialog.show(childFragmentManager, "modify_my_event")
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        onDialogDismissListener?.invoke()

        super.onDismiss(dialog)
    }
}