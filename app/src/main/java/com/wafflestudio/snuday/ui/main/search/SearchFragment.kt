package com.wafflestudio.snuday.ui.main.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.wafflestudio.snuday.databinding.FragmentNotificationBinding
import com.wafflestudio.snuday.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels()

    private lateinit var searchChannelAdapter: SearchChannelAdapter
    private lateinit var searchChannelLayoutManager: LinearLayoutManager

    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchChannelAdapter = SearchChannelAdapter(requireContext())
        searchChannelLayoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewChannels.adapter = searchChannelAdapter
        binding.recyclerViewChannels.layoutManager = searchChannelLayoutManager

        viewModel.getChannels()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                val channels = response.results
                searchChannelAdapter.channelList = channels.toMutableList()
                searchChannelAdapter.notifyDataSetChanged()
            }, {
                Timber.d(it)
            }).also { compositeDisposable.add(it) }

    }
}