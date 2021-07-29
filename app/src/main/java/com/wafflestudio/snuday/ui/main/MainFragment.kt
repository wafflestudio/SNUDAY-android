package com.wafflestudio.snuday.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.wafflestudio.snuday.R
import com.wafflestudio.snuday.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint
import java.lang.IllegalStateException

@AndroidEntryPoint
class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var stateAdapter: MainFragmentStateAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        stateAdapter = MainFragmentStateAdapter(this)
        binding.viewPager.apply {
            adapter = stateAdapter
            isUserInputEnabled = false
        }

        TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager
        ) { tab: TabLayout.Tab, i: Int ->
            tab.icon = when (i) {
                0 -> ResourcesCompat.getDrawable(resources, R.drawable.ic_tabitem_schedule, null)
                1 -> ResourcesCompat.getDrawable(resources, R.drawable.ic_tabitem_notification, null)
                2 -> ResourcesCompat.getDrawable(resources, R.drawable.ic_tabitem_search, null)
                3 -> ResourcesCompat.getDrawable(resources, R.drawable.ic_tabitem_channel, null)
                4 -> ResourcesCompat.getDrawable(resources, R.drawable.ic_tabitem_schedule, null)
                else -> throw IllegalStateException("$i tab is not allowed!")
            }
            tab.text = when (i) {
                0 -> getString(R.string.root_schedule)
                1 -> getString(R.string.root_notification)
                2 -> getString(R.string.root_search)
                3 -> getString(R.string.root_channel)
                4 -> getString(R.string.root_mypage)
                else -> throw IllegalStateException("$i tab is not allowed!")
            }
        }.attach()

    }
}