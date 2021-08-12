package com.wafflestudio.snuday.ui.main.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wafflestudio.snuday.BuildConfig
import com.wafflestudio.snuday.R
import com.wafflestudio.snuday.databinding.FragmentSearchBinding
import com.wafflestudio.snuday.ui.main.MainFragmentDirections
import com.wafflestudio.snuday.utils.showToast
import com.wafflestudio.snuday.utils.subIoObsMain
import com.wafflestudio.snuday.utils.visibleOrGone
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.HttpException
import timber.log.Timber

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val vm: SearchViewModel by viewModels()
    private val compositeDisposable = CompositeDisposable()

    private lateinit var recommendChannelAdapter: RecommendChannelAdapter
    private lateinit var recommendChannelLayoutManager: LinearLayoutManager

    private lateinit var searchChannelAdapter: SearchChannelAdapter
    private lateinit var searchChannelLayoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recommendChannelAdapter = RecommendChannelAdapter()
        recommendChannelLayoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewRecommendChannel.apply {
            adapter = recommendChannelAdapter
            layoutManager = recommendChannelLayoutManager
        }

        searchChannelAdapter = SearchChannelAdapter()
        searchChannelLayoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewSearchedChannel.apply {
            adapter = searchChannelAdapter
            layoutManager = searchChannelLayoutManager
        }

        binding.backgroundFilter.setOnClickListener {
            makeFilterLayoutGone()
        }

        binding.buttonSearchFilter.setOnClickListener {
            binding.backgroundFilter.visibleOrGone(true)
            binding.listSearchFilter.visibleOrGone(true)
        }

        binding.searchButton.setOnClickListener {

            if (BuildConfig.DEBUG) {
                if (binding.editTextSearchQuery.text.toString().equals("test channel")) {
                    val action = MainFragmentDirections.actionMainFragmentToChannelDetailActivity(0)
                    findNavController().navigate(action)
                }
            }

            if (binding.editTextSearchQuery.text.toString().length < 2) {
                showToast(getString(R.string.search_need_more_word))
                return@setOnClickListener
            }

            binding.recommendLayout.visibleOrGone(false)
            binding.searchResultLayout.visibleOrGone(true)
            vm.searchChannel(binding.editTextSearchQuery.text.toString())
                .subIoObsMain()
                .subscribe({
                    searchChannelLayoutManager.scrollToPosition(0)
                }, {
                    try {
                        val exception = it as HttpException
                        if (exception.code() == 400) {
                            showToast(getString(R.string.search_no_result))
                        }
                    } catch (error: ClassCastException) {
                        Timber.e("Error not HttpException")
                    }

                    Timber.d(it)
            }).also { compositeDisposable.add(it) }
        }

        binding.editTextSearchQuery.setOnEditorActionListener { _, _, _ ->

            binding.recommendLayout.visibleOrGone(false)
            binding.searchResultLayout.visibleOrGone(true)
            vm.searchChannel(binding.editTextSearchQuery.text.toString())
                .subIoObsMain()
                .subscribe({
                    searchChannelLayoutManager.scrollToPosition(0)
                }, {
                    try {
                        val exception = it as HttpException
                        if (exception.code() == 400) showToast(getString(R.string.search_need_more_word))
                    } catch (error: ClassCastException) {
                        Timber.e("Error not HttpException")
                    }

                    Timber.e(it)
                }).also { compositeDisposable.add(it) }

            true
        }

        binding.textSearchFilterAll.setOnClickListener {
            makeFilterLayoutGone()
            vm.setFilter(SearchFilter.ALL)
        }
        binding.textSearchFilterChannelName.setOnClickListener {
            makeFilterLayoutGone()
            vm.setFilter(SearchFilter.NAME)
        }
        binding.textSearchFilterChannelDes.setOnClickListener {
            makeFilterLayoutGone()
            vm.setFilter(SearchFilter.DESCRIPTION)
        }

        vm.observeSearchFilter().subscribe {
            when(it) {
                SearchFilter.ALL -> binding.textFilter.text = getString(R.string.search_search_filter_all)
                SearchFilter.NAME -> binding.textFilter.text = getString(R.string.search_search_filter_channel_name)
                SearchFilter.DESCRIPTION -> binding.textFilter.text = getString(R.string.search_search_filter_channel_des)
            }
        }

        vm.refreshRecommendChannel()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ channelList ->
                recommendChannelAdapter.channelList = channelList
                recommendChannelAdapter.notifyDataSetChanged()
            }, {
                Timber.e(it)
            }).also { compositeDisposable.add(it) }

        vm.observeSearchedChannel()
            .subIoObsMain()
            .subscribe ({ channelList ->
            searchChannelAdapter.channelList = channelList
            searchChannelAdapter.notifyDataSetChanged()
        }, {
            Timber.e(it)
        })

        binding.recyclerViewSearchedChannel
            .addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val totalChannelData = searchChannelAdapter.itemCount
                    val lastVisibleDataPosition = searchChannelLayoutManager.findLastCompletelyVisibleItemPosition()

                    if (lastVisibleDataPosition >= totalChannelData - 1) {
                        if (!vm.checkLoadable()) {
                            vm.loadNextSearchChannel().subIoObsMain()
                                .subscribe({},{
                                    Timber.e(it)
                                })
                                .also { compositeDisposable.add(it) }
                        }
                    }

                }
        })

    }

    override fun onDestroy() {
        compositeDisposable.clear()

        super.onDestroy()
    }

    private fun makeFilterLayoutGone() {
        binding.backgroundFilter.visibleOrGone(false)
        binding.listSearchFilter.visibleOrGone(false)
    }
}