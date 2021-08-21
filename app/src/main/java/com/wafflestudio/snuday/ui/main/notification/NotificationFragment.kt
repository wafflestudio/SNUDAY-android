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
import com.wafflestudio.snuday.R
import com.wafflestudio.snuday.databinding.FragmentNotificationBinding
import com.wafflestudio.snuday.ui.main.MainFragmentDirections
import com.wafflestudio.snuday.ui.main.search.SearchFilter
import com.wafflestudio.snuday.utils.showToast
import com.wafflestudio.snuday.utils.subIoObsMain
import com.wafflestudio.snuday.utils.visibleOrGone
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import retrofit2.HttpException
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
                val action = MainFragmentDirections.actionMainFragmentToNoticeDetailFragment(channelName, channelId, noticeId)
                findNavController().navigate(action)
            }
        )
        notificationLayoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewNotification.apply {
            adapter = notificationAdapter
            layoutManager = notificationLayoutManager
        }

        binding.backgroundFilter.setOnClickListener {
            makeFilterLayoutGone()
        }

        binding.buttonSearchFilter.setOnClickListener {
            binding.backgroundFilter.visibleOrGone(true)
            binding.listNotificationFilter.visibleOrGone(true)
        }

        binding.textNotificationFilterAll.setOnClickListener {
            makeFilterLayoutGone()
            vm.setFilter(NoticeFilter.ALL)
        }

        binding.textNotificationFilterContents.setOnClickListener {
            makeFilterLayoutGone()
            vm.setFilter(NoticeFilter.CONTENTS)
        }

        binding.textNotificationFilterTitle.setOnClickListener {
            makeFilterLayoutGone()
            vm.setFilter(NoticeFilter.TITLE)
        }

        vm.observeNoticeFliter().subscribe {
            when(it) {
                NoticeFilter.ALL -> binding.textFilter.text = getString(R.string.notification_search_filter_all)
                NoticeFilter.CONTENTS -> binding.textFilter.text = getString(R.string.notification_search_filter_contents)
                NoticeFilter.TITLE -> binding.textFilter.text = getString(R.string.notification_search_filter_title)
            }
        }

        binding.buttonSearch.setOnClickListener {
            vm.searchNotice(binding.editTextSearchQuery.text.toString())
                .subIoObsMain()
                .subscribe({ response ->
                    notificationLayoutManager.scrollToPosition(0)
                }, {
                    val e = it as HttpException
                    if (e.code() == 400) showToast(getString(R.string.search_need_more_word))
                    Timber.e(it)
                }).also { compositeDisposable.add(it) }
        }

        binding.editTextSearchQuery.setOnEditorActionListener { _, _, _ ->
            vm.searchNotice(binding.editTextSearchQuery.text.toString())
                .subIoObsMain()
                .subscribe({ response ->
                    notificationLayoutManager.scrollToPosition(0)
                }, {
                    val e = it as HttpException
                    if (e.code() == 400) showToast(getString(R.string.search_need_more_word))
                    Timber.e(it)
                }).also { compositeDisposable.add(it) }

            true
        }


        binding.recyclerViewNotification.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val totalChannelData = notificationAdapter.itemCount
                val lastVisibleDataPosition = notificationLayoutManager.findLastCompletelyVisibleItemPosition()

                if (lastVisibleDataPosition >= totalChannelData - 1) {
                    if (vm.checkLoadable()) {
                        vm.loadNextNotice().subIoObsMain()
                            .subscribe({},{Timber.e(it)})
                            .also { compositeDisposable.add(it) }
                    }
                }

            }
        })

        vm.fetchChannelData().subIoObsMain().subscribe({}, { Timber.d(it) }).also { compositeDisposable.add(it) }
        if (!vm.alreadyCreated) {
            vm.alreadyCreated = true
            vm.fetchNotice().subIoObsMain().subscribe({}, { Timber.d(it) }).also { compositeDisposable.add(it) }
        }


        vm.makeNoticeDataList().subIoObsMain().subscribe({ noticeDataList ->
            notificationAdapter.noticeList = noticeDataList
            notificationAdapter.notifyDataSetChanged()
        }, {
            Timber.d(it)
        }).also { compositeDisposable.add(it) }
    }

    override fun onDestroy() {
        compositeDisposable.dispose()

        super.onDestroy()
    }

    private fun makeFilterLayoutGone() {
        binding.backgroundFilter.visibleOrGone(false)
        binding.listNotificationFilter.visibleOrGone(false)
    }
}