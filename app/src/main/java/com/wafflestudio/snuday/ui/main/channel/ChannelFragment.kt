package com.wafflestudio.snuday.ui.main.channel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.wafflestudio.snuday.R
import com.wafflestudio.snuday.databinding.FragmentChannelBinding
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable

@AndroidEntryPoint
class ChannelFragment : Fragment() {

    private lateinit var binding: FragmentChannelBinding
    private val vm: ChannelViewModel by viewModels()

    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChannelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSubscribingChannelList.setOnClickListener { vm.setChannelList(ChannelListInfo.SUBSCRIBING) }
        binding.buttonManagingChannelList.setOnClickListener { vm.setChannelList(ChannelListInfo.MANAGING) }

        vm.whichChannelList().subscribe { channelListInfo ->
            when(channelListInfo) {
                ChannelListInfo.SUBSCRIBING -> {
                    binding.buttonSubscribingChannelList.isSelected = true
                    binding.buttonManagingChannelList.isSelected = false
                    binding.lineShowChannelSelecting.x = resources.getDimension(R.dimen.channel_list_subscribing_start_x)
                }
                ChannelListInfo.MANAGING -> {
                    binding.buttonSubscribingChannelList.isSelected = false
                    binding.buttonManagingChannelList.isSelected = true
                    binding.lineShowChannelSelecting.x = resources.getDimension(R.dimen.channel_list_managing_start_x)
                }
            }
        }.also { compositeDisposable.add(it) }
    }

    enum class ChannelListInfo {
        SUBSCRIBING,
        MANAGING
    }
}