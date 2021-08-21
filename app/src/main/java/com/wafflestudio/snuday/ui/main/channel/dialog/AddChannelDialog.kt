package com.wafflestudio.snuday.ui.main.channel.dialog

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.wafflestudio.snuday.R
import com.wafflestudio.snuday.databinding.DialogAddChannelBinding
import com.wafflestudio.snuday.ui.main.channel.ChannelViewModel
import com.wafflestudio.snuday.utils.showToast
import com.wafflestudio.snuday.utils.subIoObsMain
import com.wafflestudio.snuday.utils.visibleOrGone
import io.reactivex.rxjava3.disposables.CompositeDisposable
import timber.log.Timber

class AddChannelDialog : DialogFragment() {

    private lateinit var binding: DialogAddChannelBinding
    private val vm: ChannelViewModel by activityViewModels()

    private lateinit var channelManagerSearchAdapter: AddChannelManagerSearchAdapter
    private lateinit var channelManagerSearchLayoutManager: LinearLayoutManager

    private lateinit var channelManagerAdapter: AddChannelManagerAdapter
    private lateinit var channelManagerLayoutManager: LinearLayoutManager

    private var onDialogDismissListener: (() -> (Unit))? = null
    fun setOnDialogDismissListener(listener: () -> (Unit)) {
        onDialogDismissListener = listener
    }

    private val compositeDisposable = CompositeDisposable()

    private var isPrivate = false
    set(value) {
        binding.icPrivate.isSelected = value
        binding.textPrivate.text =
            if (value) {
                getString(R.string.channel_private_channel)
            } else {
                getString(R.string.channel_public_channel)
            }
        field = value
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogAddChannelBinding.inflate(layoutInflater)
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

        channelManagerSearchAdapter = AddChannelManagerSearchAdapter { user ->
            channelManagerAdapter.userList.find { it == user }?.let {
                showToast("이미 추가된 사용자입니다.")
            } ?: run {
                channelManagerAdapter.userList.add(user)
                channelManagerAdapter.notifyDataSetChanged()
                channelManagerSearchAdapter.searchUser = listOf()
                channelManagerSearchAdapter.notifyDataSetChanged()
            }
            binding.editTextManager.setText("")
        }
        channelManagerSearchLayoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewManagerSearch.apply {
            adapter = channelManagerSearchAdapter
            layoutManager = channelManagerSearchLayoutManager
        }

        channelManagerAdapter = AddChannelManagerAdapter()
        channelManagerLayoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewManager.apply {
            adapter = channelManagerAdapter
            layoutManager = channelManagerLayoutManager
        }

        vm.getMyData().subIoObsMain().subscribe({
            channelManagerAdapter.userList.add(it)
            channelManagerAdapter.notifyDataSetChanged()
        }, {
            Timber.d(it)
        }).also { compositeDisposable.add(it) }

        binding.switchPrivate.setOnCheckedChangeListener{ _, b -> isPrivate = !b}

        binding.editTextManager.addTextChangedListener {

            binding.recyclerViewManagerSearch.visibleOrGone(
                it.toString().length >= 2 &&
                    channelManagerSearchAdapter.searchUser.isNotEmpty()
            )
            if (it.toString().length >= 2) {
                vm.searchUser(it.toString()).subIoObsMain().subscribe({
                    channelManagerSearchAdapter.searchUser = it
                    channelManagerSearchAdapter.notifyDataSetChanged()
                }, {
                    channelManagerSearchAdapter.searchUser = listOf()
                    channelManagerSearchAdapter.notifyDataSetChanged()
                    Timber.d(it)
                }).also { compositeDisposable.add(it) }
            } else {
                channelManagerSearchAdapter.searchUser = listOf()
                channelManagerSearchAdapter.notifyDataSetChanged()
            }
        }
        binding.buttonClose.setOnClickListener {
            dismiss()
        }

        binding.buttonSave.setOnClickListener {
            vm.postChannel(binding.editTextTitle.text.toString(),
                binding.editTextChannelDescription.text.toString(),
                isPrivate,
                channelManagerAdapter.userList.let { userList ->
                    val returnList = mutableListOf<String>()
                    userList.forEach { returnList.add(it.username) }
                    returnList.toList()
                }
            ).subIoObsMain().subscribe({ dismiss() }, {
                Timber.d(it)
            }).also { compositeDisposable.add(it) }
        }

    }

    override fun onDismiss(dialog: DialogInterface) {
        onDialogDismissListener?.invoke()

        super.onDismiss(dialog)
    }

    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable.dispose()
    }

}