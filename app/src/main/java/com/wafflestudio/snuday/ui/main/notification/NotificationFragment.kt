package com.wafflestudio.snuday.ui.main.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.wafflestudio.snuday.databinding.FragmentNotificationBinding
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.internal.notify
import timber.log.Timber

@AndroidEntryPoint
class NotificationFragment : Fragment() {

    private lateinit var binding: FragmentNotificationBinding
    private val viewModel: NotificationViewModel by viewModels()

    private lateinit var notificationAdapter: NotificationAdapter
    private lateinit var notificationLayoutManager: LinearLayoutManager

    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotificationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notificationAdapter = NotificationAdapter()
        notificationLayoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewNotification.adapter = notificationAdapter
        binding.recyclerViewNotification.layoutManager = notificationLayoutManager

        viewModel.getNotice()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                notificationAdapter.notificationList = response.results.toMutableList()
                notificationAdapter.notifyDataSetChanged()
            }, {
                Timber.d(it)
            }).also { compositeDisposable.add(it) }

    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }
}