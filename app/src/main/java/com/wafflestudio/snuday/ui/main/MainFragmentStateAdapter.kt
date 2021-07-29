package com.wafflestudio.snuday.ui.main

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.wafflestudio.snuday.ui.main.channel.ChannelFragment
import com.wafflestudio.snuday.ui.main.mypage.MypageFragment
import com.wafflestudio.snuday.ui.main.notification.NotificationFragment
import com.wafflestudio.snuday.ui.main.schedule.ScheduleFragment
import com.wafflestudio.snuday.ui.main.search.SearchFragment
import java.lang.IllegalStateException

class MainFragmentStateAdapter(mainFragment: MainFragment) : FragmentStateAdapter(mainFragment) {
    override fun getItemCount() = 5

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ScheduleFragment()
            1 -> NotificationFragment()
            2 -> SearchFragment()
            3 -> ChannelFragment()
            4 -> MypageFragment()
            else -> throw IllegalStateException("$position fragment is not allowed!")
        }
    }
}