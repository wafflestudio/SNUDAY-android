package com.wafflestudio.snuday.ui.main.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wafflestudio.snuday.databinding.FragmentNotificationBinding
import com.wafflestudio.snuday.ui.main.MainFragmentDirections
import com.wafflestudio.snuday.utils.subIoObsMain
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import timber.log.Timber
import java.util.*

@AndroidEntryPoint
class NotificationFragment : Fragment() {

    private lateinit var binding: FragmentNotificationBinding
    private val vm: NotificationViewModel by viewModels()

    private val compositeDisposable = CompositeDisposable()

    private lateinit var notificationAdapter: NotificationAdapter
    private lateinit var notificationLayoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notificationAdapter = NotificationAdapter(
            onNoticeClickListener = { channelId, channelName, noticeId ->
                val action = MainFragmentDirections.actionMainFragmentToNoticeDetailActivity(channelName, channelId, noticeId)
                findNavController().navigate(action)
            }
        )
        notificationLayoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewNotification.apply {
            adapter = notificationAdapter
            layoutManager = notificationLayoutManager
        }

        binding.recyclerViewNotification.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val totalChannelData = notificationAdapter.itemCount
                val lastVisibleDataPosition = notificationLayoutManager.findLastCompletelyVisibleItemPosition()

                if (lastVisibleDataPosition >= totalChannelData - 1) {
                    if (!vm.checkLoadable()) {
                        vm.loadNextNotice().subIoObsMain()
                            .subscribe({},{Timber.e(it)})
                            .also { compositeDisposable.add(it) }
                    }
                }

            }
        })

        vm.fetchChannelData().subIoObsMain().subscribe({}, { Timber.d(it) }).also { compositeDisposable.add(it) }
        vm.fetchNotice().subIoObsMain().subscribe({}, { Timber.d(it) }).also { compositeDisposable.add(it) }

        vm.makeNoticeDataList().subIoObsMain().subscribe({ noticeDataList ->
            notificationAdapter.noticeList = noticeDataList
            notificationAdapter.notifyDataSetChanged()
        }, {
            Timber.d(it)
        }).also { compositeDisposable.add(it) }
    }
}