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
import com.wafflestudio.snuday.databinding.DialogFilterSettingBinding
import com.wafflestudio.snuday.ui.main.schedule.ScheduleViewModel
import com.wafflestudio.snuday.utils.subIoObsMain
import io.reactivex.rxjava3.disposables.CompositeDisposable
import timber.log.Timber

class ChannelFilterDialog : DialogFragment() {

    private lateinit var binding: DialogFilterSettingBinding
    private val vm: ScheduleViewModel by activityViewModels()

    private lateinit var channelFilterAdapter: ChannelFilterAdapter
    private lateinit var channelFilterLayoutManager: LinearLayoutManager

    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogFilterSettingBinding.inflate(layoutInflater)
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

        channelFilterAdapter = ChannelFilterAdapter(
            onCrossClickListener = { channelId ->
                vm.deleteChannelFromFilter(channelId).subscribe({}, { Timber.d(it) }).also { compositeDisposable.add(it) }
            },
            onPlusClickListener = { channelId ->
                vm.addChannelToFilter(channelId).subscribe({}, { Timber.d(it) }).also { compositeDisposable.add(it) }
            },
            onColorClickListener = { channelId, position ->
                val dialog = ColorSettingDialog(channelId)
//                dialog.setOnDialogDismissListener {
//                    Timber.d("CALLASIFJ")
//                    vm.fetchSubscribingChannel().subIoObsMain().subscribe({}, {Timber.d(it)}).also { compositeDisposable.add(it) }
//                }
                dialog.show(childFragmentManager, "color_setting")
            }
        )
        channelFilterLayoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewChannelFilter.apply {
            adapter = channelFilterAdapter
            layoutManager = channelFilterLayoutManager
        }

        vm.subscribingList.subIoObsMain().subscribe({
            Timber.d("channelFilter sublist ${it}")
            channelFilterAdapter.subList = it
            channelFilterAdapter.notifyDataSetChanged()
        }, { Timber.d(it)}).also { compositeDisposable.add(it) }

        vm.filterList.subIoObsMain().subscribe({
            Timber.d("channelFilter filterList ${it}")
            channelFilterAdapter.filterList = it
            channelFilterAdapter.notifyDataSetChanged()
        }, { Timber.d(it) }).also { compositeDisposable.add(it) }

    }

    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable.dispose()
    }
}