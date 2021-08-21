package com.wafflestudio.snuday.ui.main.channel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.wafflestudio.snuday.R
import com.wafflestudio.snuday.databinding.FragmentChannelBinding
import com.wafflestudio.snuday.ui.main.MainFragmentDirections
import com.wafflestudio.snuday.ui.main.channel.dialog.AddChannelDialog
import com.wafflestudio.snuday.ui.main.channel.dialog.ModifyChannelDialog
import com.wafflestudio.snuday.utils.subIoObsMain
import com.wafflestudio.snuday.utils.visibleOrGone
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import timber.log.Timber

@AndroidEntryPoint
class ChannelFragment : Fragment() {

    private lateinit var binding: FragmentChannelBinding
    private val vm: ChannelViewModel by viewModels()

    private lateinit var subscribingChannelAdapter: SubscribingChannelAdapter
    private lateinit var subscribingChannelLayoutManager: LinearLayoutManager

    private lateinit var managingChannelAdapter: ManagingChannelAdapter
    private lateinit var managingChannelLayoutManager: LinearLayoutManager

    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChannelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        vm.loadSubscribingChannel().subIoObsMain().subscribe({}, {
            Timber.d(it)
        }).also { compositeDisposable.add(it) }

        vm.loadManagingChannel().subIoObsMain().subscribe({}, {
            Timber.d(it)
        }).also { compositeDisposable.add(it) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribingChannelAdapter = SubscribingChannelAdapter(
            channelOnClickListener = { channelId ->
                val action = MainFragmentDirections.actionMainFragmentToChannelDetailFragment(channelId)
                findNavController().navigate(action)
            },
            subscribeButtonOnClickListener = { channelId ->
                vm.unsubscribeChannel(channelId)
                    .subIoObsMain()
                    .subscribe({
                        refreshSubscribingChannel()
                }, {
                    Timber.d(it)
                }).also { compositeDisposable.add(it) }
            }
        )
        subscribingChannelLayoutManager = LinearLayoutManager(requireContext())

        managingChannelAdapter = ManagingChannelAdapter(
            channelOnClickListener =  { channelId ->
                val action = MainFragmentDirections.actionMainFragmentToChannelDetailFragment(channelId)
                findNavController().navigate(action)
            },
            editButtonOnClickListener = { channel ->
                val dialog = ModifyChannelDialog(channel)
                dialog.setOnDialogDismissListener {
                    vm.loadManagingChannel().subIoObsMain().subscribe({}, {Timber.d(it)}).also { compositeDisposable.add(it) }
                }
                dialog.show(childFragmentManager, "modify_channel")
            },
            awaiterButtonOnClickListener = { channel ->
                val action = MainFragmentDirections.actionMainFragmentToAwaiterFragment(channel.id)
                findNavController().navigate(action)
            }
        )
        managingChannelLayoutManager = LinearLayoutManager(requireContext())

        binding.recyclerViewSubscribingChannel.apply {
            adapter = subscribingChannelAdapter
            layoutManager = subscribingChannelLayoutManager
        }

        binding.recyclerViewManagingChannel.apply {
            adapter = managingChannelAdapter
            layoutManager = managingChannelLayoutManager
        }

        binding.buttonSubscribingChannelList.setOnClickListener {
            vm.setChannelList(ChannelListInfo.SUBSCRIBING)
            vm.loadSubscribingChannel().subIoObsMain().subscribe({}, {
                Timber.d(it)
            }).also { compositeDisposable.add(it) }
        }
        binding.buttonManagingChannelList.setOnClickListener {
            vm.setChannelList(ChannelListInfo.MANAGING)
            vm.loadManagingChannel().subIoObsMain().subscribe({}, {
                Timber.d(it)
            }).also { compositeDisposable.add(it) }
        }

        binding.buttonAddChannelFloating.setOnClickListener {
            val dialog = AddChannelDialog()
            dialog.setOnDialogDismissListener {
                vm.loadManagingChannel().subIoObsMain().subscribe({}, { Timber.d(it) }).also { compositeDisposable.add(it) }
            }
            dialog.show(childFragmentManager, "add_channel")
        }

        vm.whichChannelList().subscribe { channelListInfo ->
            renderChannelList(channelListInfo)
        }.also { compositeDisposable.add(it) }

        vm.observeSubscribingChannel().observeOn(AndroidSchedulers.mainThread()).subscribe {
            subscribingChannelAdapter.channelList = it
            subscribingChannelAdapter.notifyDataSetChanged()
            subscribingChannelLayoutManager.scrollToPosition(0)
        }.also { compositeDisposable.add(it) }

        vm.observeManagingChannel().observeOn(AndroidSchedulers.mainThread()).subscribe ({
            managingChannelAdapter.channelList = it.toMutableList()
            it.forEach { channel ->
                vm.getAwaiter(channel.id).subIoObsMain().subscribe({ awaiterList ->
                    managingChannelAdapter.channelList.find { remain -> remain.id == channel.id }?.let { it.awaiter = awaiterList }
                    managingChannelAdapter.notifyDataSetChanged()
                }, { Timber.d(it) }).also { compositeDisposable.add(it) }
            }

            managingChannelAdapter.notifyDataSetChanged()
            managingChannelLayoutManager.scrollToPosition(0)
        }, { Timber.d(it) }).also { compositeDisposable.add(it) }
    }

    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable.dispose()
    }

    private fun renderChannelList(channelListInfo: ChannelListInfo) {
        when(channelListInfo) {
            ChannelListInfo.SUBSCRIBING -> {
                binding.buttonSubscribingChannelList.isSelected = true
                binding.buttonManagingChannelList.isSelected = false
                binding.recyclerViewSubscribingChannel.visibleOrGone(true)
                binding.recyclerViewManagingChannel.visibleOrGone(false)
                binding.buttonAddChannelFloating.visibleOrGone(false)
                binding.lineShowChannelSelecting.x = resources.getDimension(R.dimen.channel_list_subscribing_start_x)
            }
            ChannelListInfo.MANAGING -> {
                binding.buttonSubscribingChannelList.isSelected = false
                binding.buttonManagingChannelList.isSelected = true
                binding.recyclerViewSubscribingChannel.visibleOrGone(false)
                binding.recyclerViewManagingChannel.visibleOrGone(true)
                binding.buttonAddChannelFloating.visibleOrGone(true)
                binding.lineShowChannelSelecting.x = resources.getDimension(R.dimen.channel_list_managing_start_x)
            }
        }
    }

    private fun refreshSubscribingChannel() {
        vm.loadSubscribingChannel().subIoObsMain().subscribe({}, {
            Timber.d(it)
        }).also { compositeDisposable.add(it) }
    }

    enum class ChannelListInfo {
        SUBSCRIBING,
        MANAGING
    }
}