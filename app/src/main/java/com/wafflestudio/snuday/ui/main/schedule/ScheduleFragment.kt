package com.wafflestudio.snuday.ui.main.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.wafflestudio.snuday.databinding.FragmentScheduleBinding
import com.wafflestudio.snuday.utils.*
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import java.util.*

@AndroidEntryPoint
class ScheduleFragment : Fragment() {

    private lateinit var binding: FragmentScheduleBinding
    private val viewModel: ScheduleViewModel by viewModels()

    private lateinit var tagAdapter: TagAdapter
    private lateinit var tagLayoutManager: LinearLayoutManager

    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScheduleBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Timber.d("created")

        // Tag RecyclerView
        tagAdapter = TagAdapter()
        tagLayoutManager =
            LinearLayoutManager(this.requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewTags.adapter = tagAdapter
        binding.recyclerViewTags.layoutManager = tagLayoutManager

        val currentDate = Calendar.getInstance()

        binding.customCalendarView.setDatesToCalendar(currentDate.time)
        binding.textYearShow.text = currentDate.getYearText()
        binding.textMonthShow.text = currentDate.getMonthText(requireContext())

        viewModel.getEvents()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                viewModel.events = response
                binding.customCalendarView.bindEvents(viewModel.events)
            }, {
                Timber.d(it)
            }).also { compositeDisposable.add(it) }

        viewModel.getSubscribingChannels()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                viewModel.channels = response.results
                tagAdapter.channels = viewModel.channels.toMutableList()
                tagAdapter.notifyDataSetChanged()
            }, {
                Timber.d(it)
            }).also { compositeDisposable.add(it) }

    }

    override fun onDestroy() {
        Timber.d("destroy")
        super.onDestroy()
        compositeDisposable.dispose()
    }

}
