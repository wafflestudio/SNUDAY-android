package com.wafflestudio.snuday.ui.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.wafflestudio.snuday.R
import com.wafflestudio.snuday.databinding.ActivityMainBinding
import com.wafflestudio.snuday.ui.main.channel.ChannelFragment
import com.wafflestudio.snuday.ui.main.mypage.MyPageFragment
import com.wafflestudio.snuday.ui.main.notification.NotificationFragment
import com.wafflestudio.snuday.ui.main.schedule.ScheduleFragment
import com.wafflestudio.snuday.ui.main.search.SearchFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val scheduleFragment: ScheduleFragment = ScheduleFragment()
    private val notificationFragment: NotificationFragment = NotificationFragment()
    private val searchFragment: SearchFragment = SearchFragment()
    private val channelFragment: ChannelFragment = ChannelFragment()
    private val myPageFragment: MyPageFragment = MyPageFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add(R.id.nav_host_fragment, scheduleFragment, TAG_SCHEDULE)
            addToBackStack(TAG_SCHEDULE)
        }

        binding.bottomNavView.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.navigation_my_schedules -> {
                    supportFragmentManager.commit {
                        replace(R.id.nav_host_fragment, scheduleFragment, TAG_SCHEDULE)
                        setReorderingAllowed(true)
                        addToBackStack(TAG_SCHEDULE)
                    }
                }
                R.id.navigation_notifications -> {
                    supportFragmentManager.commit {
                        replace(R.id.nav_host_fragment, notificationFragment, TAG_NOTIFICATION)
                        setReorderingAllowed(true)
                        addToBackStack(TAG_NOTIFICATION)
                    }
                }
                R.id.navigation_search -> {
                    supportFragmentManager.commit {
                        replace(R.id.nav_host_fragment, searchFragment, TAG_SEARCH)
                        setReorderingAllowed(true)
                        addToBackStack(TAG_SEARCH)
                    }
                }
                R.id.navigation_channel -> {
                    supportFragmentManager.commit {
                        replace(R.id.nav_host_fragment, channelFragment, TAG_CHANNEL)
                        setReorderingAllowed(true)
                        addToBackStack(TAG_CHANNEL)
                    }
                }
                R.id.navigation_my_page -> {
                    supportFragmentManager.commit {
                        replace(R.id.nav_host_fragment, myPageFragment, TAG_MY_PAGE)
                        setReorderingAllowed(true)
                        addToBackStack(TAG_MY_PAGE)
                    }
                }
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    companion object {

        private const val TAG_SCHEDULE = "schedule_fragment"
        private const val TAG_NOTIFICATION = "notification_fragment"
        private const val TAG_SEARCH = "search_fragment"
        private const val TAG_CHANNEL = "channel_fragment"
        private const val TAG_MY_PAGE = "my_page_fragment"

        fun createIntent(context : Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }



}