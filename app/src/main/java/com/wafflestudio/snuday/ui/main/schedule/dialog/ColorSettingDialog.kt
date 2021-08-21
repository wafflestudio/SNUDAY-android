package com.wafflestudio.snuday.ui.main.schedule.dialog

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.wafflestudio.snuday.R
import com.wafflestudio.snuday.databinding.DialogColorSettingBinding
import com.wafflestudio.snuday.databinding.ItemColorSettingBinding
import com.wafflestudio.snuday.ui.main.schedule.ScheduleViewModel
import com.wafflestudio.snuday.utils.subIoObsMain
import com.wafflestudio.snuday.utils.visibleOrGone
import io.reactivex.rxjava3.disposables.CompositeDisposable
import timber.log.Timber

class ColorSettingDialog(private val channelId: Int) : DialogFragment() {

    private lateinit var binding: DialogColorSettingBinding
    private lateinit var colorLayoutList: List<ItemColorSettingBinding>

    private val vm: ScheduleViewModel by activityViewModels()

    private var onDialogDismissListener: (() -> (Unit))? = null
    fun setOnDialogDismissListener(listener: () -> (Unit)) {
        onDialogDismissListener = listener
    }

    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogColorSettingBinding.inflate(layoutInflater)
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

        colorLayoutList = listOf(
            binding.layoutColor1,
            binding.layoutColor2,
            binding.layoutColor3,
            binding.layoutColor4,
            binding.layoutColor5,
            binding.layoutColor6,
            binding.layoutColor7,
            binding.layoutColor8,
            binding.layoutColor9
        )

        colorLayoutList.forEachIndexed { idx, layoutColor ->
            layoutColor.iconShowingColor.setBackgroundColor( when(idx) {
                0 -> ContextCompat.getColor(requireContext(), R.color.snutt_color_1)
                1 -> ContextCompat.getColor(requireContext(), R.color.snutt_color_2)
                2 -> ContextCompat.getColor(requireContext(), R.color.snutt_color_3)
                3 -> ContextCompat.getColor(requireContext(), R.color.snutt_color_4)
                4 -> ContextCompat.getColor(requireContext(), R.color.snutt_color_5)
                5 -> ContextCompat.getColor(requireContext(), R.color.snutt_color_6)
                6 -> ContextCompat.getColor(requireContext(), R.color.snutt_color_7)
                7 -> ContextCompat.getColor(requireContext(), R.color.snutt_color_8)
                8 -> ContextCompat.getColor(requireContext(), R.color.snutt_color_9)
                else -> ContextCompat.getColor(requireContext(), R.color.snutt_color_1)
            })

            layoutColor.textColorName.text = when(idx) {
                0 -> getString(R.string.color_name_snutt_1)
                1 -> getString(R.string.color_name_snutt_2)
                2 -> getString(R.string.color_name_snutt_3)
                3 -> getString(R.string.color_name_snutt_4)
                4 -> getString(R.string.color_name_snutt_5)
                5 -> getString(R.string.color_name_snutt_6)
                6 -> getString(R.string.color_name_snutt_7)
                7 -> getString(R.string.color_name_snutt_8)
                8 -> getString(R.string.color_name_snutt_9)
                else -> getString(R.string.color_name_snutt_1)
            }

            layoutColor.root.setOnClickListener {
                vm.setChannelColor(channelId, idx).subIoObsMain().subscribe({}, {Timber.d(it)}).also { compositeDisposable.add(it) }
            }
        }

        vm.getChannelColorById(channelId).subIoObsMain().subscribe({ channelColor ->
             colorLayoutList.forEachIndexed { idx, layoutColor ->
                 if (channelColor != null) {
                     layoutColor.iconCheck.visibleOrGone(idx == channelColor.colorCode)
                 }
             }
        }, {
            Timber.d(it)
        }).also { compositeDisposable.add(it) }

    }

    override fun onDismiss(dialog: DialogInterface) {
        onDialogDismissListener?.invoke()

        super.onDismiss(dialog)
    }

}