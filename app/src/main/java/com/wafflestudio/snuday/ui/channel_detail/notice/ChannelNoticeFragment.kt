package com.wafflestudio.snuday.ui.channel_detail.notice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wafflestudio.snuday.databinding.FragmentChannelNoticeBinding
import com.wafflestudio.snuday.ui.channel_detail.ChannelDetailViewModel
import com.wafflestudio.snuday.utils.subIoObsMain
import com.wafflestudio.snuday.utils.visibleOrGone
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import timber.log.Timber

@AndroidEntryPoint
class ChannelNoticeFragment : Fragment() {

    private lateinit var binding: FragmentChannelNoticeBinding
    private val vm: ChannelDetailViewModel by activityViewModels()
    private val args: ChannelNoticeFragmentArgs by navArgs()

    private lateinit var channelNoticeAdapter: ChannelNoticeAdapter
    private lateinit var channelNoticeLayoutManager: LinearLayoutManager

    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChannelNoticeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        channelNoticeAdapter = ChannelNoticeAdapter {
            val action = ChannelNoticeFragmentDirections.actionChannelNoticeFragmentToChannelNoticeDetailFragment(args.channelName, args.channelId, it)
            findNavController().navigate(action)
        }
        channelNoticeLayoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewNotice.apply {
            adapter = channelNoticeAdapter
            layoutManager = channelNoticeLayoutManager
        }

        binding.buttonBackBlack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.textChannelTitleToolbar.text = args.channelName

        binding.buttonSubscribe.setOnClickListener {
            if (it.isSelected) {
                vm.unsubscribeChannel(args.channelId).subIoObsMain()
                    .subscribe({
                        vm.checkSubscribing(args.channelId).subIoObsMain()
                            .subscribe({ isSubscribing ->
                                binding.buttonSubscribe.isSelected = isSubscribing
                            }, { Timber.d(it)}).also { compositeDisposable.add(it) }
                    }, {Timber.d(it)}).also { compositeDisposable.add(it) }
            } else {
                vm.subscribeChannel(args.channelId).subIoObsMain()
                    .subscribe({
                        vm.checkSubscribing(args.channelId).subIoObsMain()
                            .subscribe({ isSubscribing ->
                                binding.buttonSubscribe.isSelected = isSubscribing
                            }, { Timber.d(it)}).also { compositeDisposable.add(it) }
                    }, {Timber.d(it)}).also { compositeDisposable.add(it) }
            }
        }

        vm.checkSubscribing(args.channelId).subIoObsMain().subscribe({
             binding.buttonSubscribe.isSelected = it
        }, {
            Timber.d(it)
        }).also { compositeDisposable.add(it) }

        vm.checkManaging(args.channelId).subIoObsMain().subscribe({
            binding.buttonAddNotice.visibleOrGone(it)
        }, {
            Timber.d(it)
        }).also { compositeDisposable.add(it) }

        vm.fetchChannelNotice(args.channelId).subIoObsMain().subscribe({}, {
            Timber.d(it)
        }).also { compositeDisposable.add(it) }

        vm.noticeList.subIoObsMain().subscribe({
            channelNoticeAdapter.noticeList = it
            channelNoticeAdapter.notifyDataSetChanged()
        }, { Timber.d(it) }).also { compositeDisposable.add(it) }

        binding.recyclerViewNotice.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val totalNoticeData = channelNoticeAdapter.itemCount
                val lastVisibleDataPosition = channelNoticeLayoutManager.findLastVisibleItemPosition()

                if (lastVisibleDataPosition >= totalNoticeData - 1) {
                    if (vm.checkLoadable()) {
                        vm.loadChannelNotice(args.channelId).subIoObsMain()
                            .subscribe({}, { Timber.d(it)}).also { compositeDisposable.add(it) }
                    }
                }
            }
        })

        binding.buttonAddNotice.setOnClickListener {
            val action = ChannelNoticeFragmentDirections.actionChannelNoticeFragmentToChannelAddNoticeFragment(false, 0, args.channelId)
            findNavController().navigate(action)
        }
    }

}